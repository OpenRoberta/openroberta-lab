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
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.worker.IWorker;

public final class UnitTestHelper {

    private static final Logger LOG = LoggerFactory.getLogger(UnitTestHelper.class);

    private UnitTestHelper() {
    }

    public static void executeWorkflowMustSucceed(String workflowName, RobotFactory robotFactory, Project project) {
        Assert.assertNull(executeWorkflow(workflowName, robotFactory, project));
    }

    /**
     * execute a workflow
     *
     * @param workflowName
     * @param robotFactory
     * @param project
     * @return null, if the workflow succeeds; otherwise a String with an error message
     */
    public static String executeWorkflow(String workflowName, RobotFactory robotFactory, Project project) {
        List<IWorker> workflowPipe = robotFactory.getWorkerPipe(workflowName);
        if ( project.hasSucceeded() ) {
            for ( IWorker worker : workflowPipe ) {
                worker.execute(project);
                if ( !project.hasSucceeded() ) {
                    String workerClassName = worker.getClass().getSimpleName();
                    return "Worker " + workerClassName + " failed with " + project.getErrorCounter() + " errors: " + project.getErrorAndWarningMessages();
                }
            }
            return null;
        } else {
            return "Error in project. Messages: " + project.getErrorAndWarningMessages();
        }
    }

    public static void checkWorkers(RobotFactory factory, String expectedSource, String programXmlFilename, IWorker... workers) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        builder.setWithWrapping(false);
        Project project = builder.build();
        for ( IWorker worker : workers ) {
            worker.execute(project);
        }
        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
    }

    public static void checkWorkersWithConf(
        RobotFactory factory,
        ConfigurationAst configuration,
        String expectedSource,
        String programXmlFilename,
        IWorker... workers) //
    {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        builder.setConfigurationAst(configuration);
        builder.setWithWrapping(false);
        Project project = builder.build();
        for ( IWorker worker : workers ) {
            worker.execute(project);
        }
        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
    }

    public static Project checkWorkflow(RobotFactory factory, String workflow, String exportXmlFilename) {
        String xmlText = Util.readResourceContent(exportXmlFilename);
        Project.Builder builder = setupWithExportXML(factory, xmlText);
        Project project = builder.build();
        executeWorkflowMustSucceed(workflow, factory, project);
        return project;
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

    public static Project.Builder setupWithProgramXMLWithDefaultConfig(RobotFactory factory, String programXmlAsString) {
        Builder builder = new Project.Builder().setProgramXml(programXmlAsString).setProgramName("Test");
        return builder.setConfigurationXml(factory.getConfigurationDefault()).setFactory(factory);
    }

    public static Project.Builder setupWithConfigXML(RobotFactory factory, String configXmlAsString) {
        return new Project.Builder().setConfigurationXml(configXmlAsString).setFactory(factory).setProgramName("Test");
    }

    public static void checkProgramReverseTransformation(RobotFactory factory, String programBlocklyXmlFilename) throws SAXException, IOException {
        String programXml = Util.readResourceContent(programBlocklyXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        Project project = builder.build();
        Assert.assertNull(runXmlUnit(programXml, project.getAnnotatedProgramAsXml()));
    }

    public static void checkConfigReverseTransformation(RobotFactory factory, String configBlocklyXmlFilename) throws SAXException, IOException {
        String configXml = Util.readResourceContent(configBlocklyXmlFilename);
        Project.Builder builder = setupWithConfigXML(factory, configXml);
        Project project = builder.build();
        Assert.assertNull(runXmlUnit(configXml, project.getAnnotatedConfigurationAsXml()));
    }

    public static void checkProgramAstEquality(RobotFactory factory, String expectedAst, String programBlocklyXmlFilename) {
        String generatedAst = getProgramAst(factory, programBlocklyXmlFilename).toString();
        checkAstEquality(generatedAst, expectedAst);
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

    public static Phrase getAstOfFirstBlock(RobotFactory factory, String programBlocklyXmlFilename) {
        return getProgramAst(factory, programBlocklyXmlFilename).get(0).get(1);
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

    public static Phrase getProgramAstFromExportXml(RobotFactory factory, String xml) {
        Project.Builder builder = setupWithExportXML(factory, xml);
        Project project = builder.build();
        return project.getProgramAst().getTree().get(0).get(1);
    }

    public static Collection<ConfigurationComponent> getConfigAst(RobotFactory factory, String configBlocklyXmlFilename) {
        String configXml = Util.readResourceContent(configBlocklyXmlFilename);
        Project.Builder builder = setupWithConfigXML(factory, configXml);
        Project project = builder.build();
        return project.getConfigurationAst().getConfigurationComponents().values();
    }

    public static void checkGeneratedSourceEqualityWithExportXml(RobotFactory factory, String expectedSourceFilename, String exportedXmlFilename) {
        String exportedXml = Util.readResourceContent(exportedXmlFilename);
        Project.Builder builder = setupWithExportXML(factory, exportedXml);
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithExportXmlAndSourceAsString(RobotFactory factory, String expectedSource, String exportedXmlFilename) {
        String exportedXml = Util.readResourceContent(exportedXmlFilename);
        Project.Builder builder = setupWithExportXML(factory, exportedXml);
        checkGeneratedSourceEquality(factory, expectedSource, builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(RobotFactory factory, String expectedSourceFilename, String programXmlFilename) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(
        RobotFactory factory,
        String expectedSourceFilename,
        String programXmlFilename,
        ConfigurationAst configurationAst) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        builder.setConfigurationAst(configurationAst);
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(
        RobotFactory factory,
        String expectedSourceFilename,
        String programXmlFilename,
        String configurationXmlFilename) {
        String programXml = Util.readResourceContent(programXmlFilename);
        String configurationXml = Util.readResourceContent(configurationXmlFilename);
        Project.Builder builder = setupWithConfigAndProgramXML(factory, programXml, configurationXml);
        builder.setSSID("mySSID");
        builder.setPassword("myPassw0rd");
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
        RobotFactory factory,
        String expectedSource,
        String programXmlFilename,
        boolean withWrapping) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        builder.setWithWrapping(withWrapping);
        checkGeneratedSourceEquality(factory, expectedSource, builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
        RobotFactory factory,
        String expectedSource,
        String programXmlFilename,
        ConfigurationAst configurationAst,
        boolean withWrapping) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        builder.setConfigurationAst(configurationAst);
        builder.setWithWrapping(withWrapping);
        checkGeneratedSourceEquality(factory, expectedSource, builder.build());
    }

    public static String generateSourceWithProgramXml(
        RobotFactory factory,
        String programXmlFilename,
        ConfigurationAst configurationAst,
        boolean withWrapping) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXMLWithDefaultConfig(factory, programXml);
        builder.setConfigurationAst(configurationAst);
        builder.setWithWrapping(withWrapping);
        return generateSource(factory, builder.build());
    }

    public static String generateSource(RobotFactory factory, Project project) {
        executeWorkflowMustSucceed("showsource", factory, project);
        return project.getSourceCode().toString();
    }

    private static void checkGeneratedSourceEquality(RobotFactory factory, String expectedSource, Project project) {
        executeWorkflowMustSucceed("showsource", factory, project);
        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
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
