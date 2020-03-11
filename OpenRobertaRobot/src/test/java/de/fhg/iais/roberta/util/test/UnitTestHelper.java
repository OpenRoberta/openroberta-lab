package de.fhg.iais.roberta.util.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.xml.sax.SAXException;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.worker.IWorker;

public final class UnitTestHelper {

    private UnitTestHelper() {
    }

    private static boolean executeWorkflow(String workflowName, IRobotFactory robotFactory, Project project) {
        List<IWorker> workflowPipe = robotFactory.getWorkerPipe(workflowName);
        if ( project.hasSucceeded() ) {
            for ( IWorker worker : workflowPipe ) {
                worker.execute(project);
                Assert.assertTrue("Worker " + worker.getClass().getSimpleName() + " failed with " + project.getErrorCounter() + " errors", project.hasSucceeded());
                if ( !project.hasSucceeded() ) {
                    break;
                }
            }
        }
        return project.hasSucceeded();
    }

    public static void checkWorkers(IRobotFactory factory, String expectedSource, String programXmlFilename, IWorker... workers) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setWithWrapping(false);
        Project project = builder.build();
        for ( IWorker worker : workers ) {
            worker.execute(project);
        }
        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
    }

    public static Project checkWorkflow(IRobotFactory factory, String workflow, String exportXmlFilename) {
        String xmlText = Util.readResourceContent(exportXmlFilename);
        Project.Builder builder = setupWithExportXML(factory, xmlText);
        Project project = builder.build();
        executeWorkflow(workflow, factory, project);
        return project;
    }

    public static Project.Builder setupWithNativeSource(IRobotFactory factory, String nativeSource) {
        return new Project.Builder().setProgramNativeSource(nativeSource).setFactory(factory);
    }

    public static Project.Builder setupWithResetFirmware(IRobotFactory factory) {
        return new Project.Builder().setCompiledProgramPath(factory.getFirmwareDefaultProgramName()).setFactory(factory);
    }

    public static Project.Builder setupWithExportXML(IRobotFactory factory, String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[0];
        return setupWithConfigurationAndProgramXML(factory, program, configuration);
    }

    public static Project.Builder setupWithConfigurationAndProgramXML(IRobotFactory factory, String programXmlAsString, String configurationXmlAsString) {
        return new Project.Builder().setConfigurationXml(configurationXmlAsString).setProgramXml(programXmlAsString).setFactory(factory);
    }

    public static Project.Builder setupWithProgramXML(IRobotFactory factory, String programXmlAsString) {
        return new Project.Builder().setProgramXml(programXmlAsString).setFactory(factory).setProgramName("Test");
    }

    public static void checkProgramReverseTransformation(IRobotFactory factory, String programBlocklyXmlFilename) throws SAXException, IOException {
        String programXml = Util.readResourceContent(programBlocklyXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        Project project = builder.build();
        String annotatedProgramXml = project.getAnnotatedProgramAsXml();
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(programXml, annotatedProgramXml);
        Assert.assertTrue(diff.identical());
    }

    public static void checkProgramAstEquality(IRobotFactory factory, String expectedAst, String programBlocklyXmlFilename) {
        String generatedAst = getAst(factory, programBlocklyXmlFilename).toString();
        generatedAst = "BlockAST [project=" + generatedAst + "]";
        Assert.assertEquals(expectedAst.replaceAll("\\s+", ""), generatedAst.replaceAll("\\s+", ""));
    }

    public static Phrase<Void> getAstOfFirstBlock(IRobotFactory factory, String programBlocklyXmlFilename) {
        return getAst(factory, programBlocklyXmlFilename).get(0).get(1);
    }

    public static ArrayList<ArrayList<Phrase<Void>>> getAst(IRobotFactory factory, String programBlocklyXmlFilename) {
        String programXml = Util.readResourceContent(programBlocklyXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        Project project = builder.build();
        return project.getProgramAst().getTree();
    }

    // TODO merge this with the method above
    public static <V> Phrase<V> generateAst(IRobotFactory factory, String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        Jaxb2ProgramAst<V> transformer = new Jaxb2ProgramAst<>(factory);
        transformer.transform(project);
        ArrayList<ArrayList<Phrase<V>>> tree = transformer.getTree();
        return tree.get(0).get(1);
    }

    public static void checkGeneratedSourceEqualityWithExportXml(IRobotFactory factory, String expectedSourceFilename, String exportedXmlFilename) {
        String exportedXml = Util.readResourceContent(exportedXmlFilename);
        Project.Builder builder = setupWithExportXML(factory, exportedXml);
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(IRobotFactory factory, String expectedSourceFilename, String programXmlFilename) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(
        IRobotFactory factory,
        String expectedSourceFilename,
        String programXmlFilename,
        ConfigurationAst configurationAst) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setConfigurationAst(configurationAst);
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(
        IRobotFactory factory,
        String expectedSourceFilename,
        String programXmlFilename,
        String configurationXmlFilename) {
        String programXml = Util.readResourceContent(programXmlFilename);
        String configurationXml = Util.readResourceContent(configurationXmlFilename);
        Project.Builder builder = setupWithConfigurationAndProgramXML(factory, programXml, configurationXml);
        builder.setSSID("mySSID");
        builder.setPassword("myPassw0rd");
        checkGeneratedSourceEquality(factory, Util.readResourceContent(expectedSourceFilename), builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
        IRobotFactory factory,
        String expectedSource,
        String programXmlFilename,
        boolean withWrapping) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setWithWrapping(withWrapping);
        checkGeneratedSourceEquality(factory, expectedSource, builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
        IRobotFactory factory,
        String expectedSource,
        String programXmlFilename,
        ConfigurationAst configurationAst,
        boolean withWrapping) {
        String programXml = Util.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setConfigurationAst(configurationAst);
        builder.setWithWrapping(withWrapping);
        checkGeneratedSourceEquality(factory, expectedSource, builder.build());
    }

    private static void checkGeneratedSourceEquality(IRobotFactory factory, String expectedSource, Project project) {
        executeWorkflow("showsource", factory, project);
        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
    }
}
