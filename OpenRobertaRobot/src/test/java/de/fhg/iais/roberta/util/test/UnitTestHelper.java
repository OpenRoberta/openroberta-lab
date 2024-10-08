package de.fhg.iais.roberta.util.test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.components.Project.Builder;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.XsltAndJavaTransformer;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.worker.IWorker;

public final class UnitTestHelper {

    private static final Logger LOG = LoggerFactory.getLogger(UnitTestHelper.class);

    private static String DEFAULT_PROGRAM_ARDUINO = //
        "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"arduino\" xmlversion=\"3.1\" description=\"\" tags=\"\">\n" +
        "<instance x=\"373\" y=\"50\">\n" +
        "<block type=\"robControls_start_ardu\" id=\"l9Lp`gNd]GQJ_0PBfA?z\" intask=\"true\" deletable=\"false\">\n" +
        "<mutation declare=\"false\"/>\n" +
        "<field name=\"DEBUG\"/>\n" +
        "</block>\n" +
        "<block type=\"robControls_loopForever_ardu\" id=\"`mIJB4z:(3D;T%:]?^Iz\" intask=\"true\" deletable=\"false\" movable=\"false\"/>\n" +
        "</instance>\n" +
        "</block_set>";

    private UnitTestHelper() {
    }

    public static Project.Builder setupWithNativeSource(RobotFactory factory, String nativeSource) {
        return new Project.Builder().setProgramNativeSource(nativeSource).setFactory(factory);
    }

    public static Project.Builder setupWithResetFirmware(RobotFactory factory) {
        return new Project.Builder().setCompiledProgramPath(factory.getFirmwareDefaultProgramName()).setFactory(factory);
    }

    public static Project.Builder setupWithExportXML(RobotFactory factory, String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[0];
        return setupWithConfigAndProgramXML(factory, program, configuration);
    }

    public static Project.Builder setupWithConfigAndProgramXML(RobotFactory factory, String programXmlAsString, String configurationXmlAsString) {
        return new Project.Builder().setConfigurationXml(configurationXmlAsString).setProgramXml(programXmlAsString).setFactory(factory);
    }

    public static Project.Builder setupWithConfigAndProgramXML(
        RobotFactory factory,
        XsltAndJavaTransformer xsltAndJavaTransformer,
        String programXmlAsString,
        String configurationXmlAsString) {
        programXmlAsString = xsltAndJavaTransformer.transformXslt(programXmlAsString);
        configurationXmlAsString = xsltAndJavaTransformer.transformXslt(configurationXmlAsString);
        return new Project.Builder().setConfigurationXml(configurationXmlAsString).setProgramXml(programXmlAsString).setFactory(factory);
    }

    public static Project.Builder setupWithProgramXMLWithDefaultConfig(RobotFactory factory, String programXmlAsString) {
        Builder builder = new Project.Builder().setProgramXml(programXmlAsString).setProgramName("Test");
        return builder.setConfigurationXml(factory.getConfigurationDefault()).setFactory(factory);
    }

    public static Project.Builder setupWithConfigXML(RobotFactory factory, String configXmlAsString) {
        Builder builder = new Project.Builder().setProgramXml(DEFAULT_PROGRAM_ARDUINO);
        return builder.setConfigurationXml(configXmlAsString).setFactory(factory).setProgramName("Test");
    }

    public static void checkConfigReverseTransformation(RobotFactory factory, String configBlocklyXmlFilename) throws SAXException, IOException {
        String configXml = Util.readResourceContent(configBlocklyXmlFilename);
        Project.Builder builder = setupWithConfigXML(factory, configXml);
        Project project = builder.build();
        XsltAndJavaTransformer.executeRegenerateNEPO(project);
        Assert.assertNull(runXmlUnit(configXml, project.getConfigurationAsBlocklyXML()));
    }

    public static void checkConfigAstEquality(RobotFactory factory, String expectedAst, String configBlocklyXmlFilename) {
        String generatedAst = getConfigAst(factory, configBlocklyXmlFilename).toString();
        checkAstEquality(generatedAst, expectedAst);
    }

    public static void checkAstEquality(String generatedAst, String expectedAst) {
        generatedAst = "BlockAST [project=" + generatedAst + "]";
        Assert.assertEquals(expectedAst.replaceAll("\\s+", ""), generatedAst.replaceAll("\\s+", ""));
    }

    public static void checkAstContains(String generatedAst, String[] expectedToBeInAst) {
        generatedAst = "BlockAST [project=" + generatedAst + "]";
        String preparedGeneratedAst = generatedAst.replaceAll("\\s+", "");
        for ( String s : expectedToBeInAst ) {
            Assert.assertTrue(preparedGeneratedAst.contains(s.replaceAll("\\s+", "")));
        }
    }

    // TODO merge this with "getAstOfFirstBlock" - would require generifying the projects' program ast
    public static <V> Phrase getGenericAstOfFirstBlock(RobotFactory factory, String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        Jaxb2ProgramAst transformer = new Jaxb2ProgramAst(factory);
        List<List<Phrase>> tree = transformer.blocks2ast(project).getTree();
        return tree.get(0).get(1);
    }

    public static List<List<Phrase>> getProgramAst(RobotFactory factory, String programBlocklyXmlFilename) {
        String programXml = Util.readResourceContent(programBlocklyXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        Project project = builder.build();
        return project.getProgramAst().getTree();
    }

    public static Collection<ConfigurationComponent> getConfigAst(RobotFactory factory, String configBlocklyXmlFilename) {
        String configXml = Util.readResourceContent(configBlocklyXmlFilename);
        Project.Builder builder = setupWithConfigXML(factory, configXml);
        Project project = builder.build();
        return project.getConfigurationAst().getConfigurationComponents().values();
    }

    /**
     * check whether the two XML Strings have differences
     *
     * @param expected
     * @param transformed
     * @return null, if no differences; the difference as String, if the XML differs
     */
    public static String runXmlUnit(String expected, String transformed) {
        Diff diff =
            DiffBuilder
                .compare(expected)
                .withTest(transformed)
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
                .ignoreWhitespace()
                .normalizeWhitespace()
                .ignoreElementContentWhitespace()
                .ignoreComments()
                .checkForSimilar()
                .build();
        if ( diff.hasDifferences() ) {
            return diff.toString();
        } else {
            return null;
        }
    }
}
