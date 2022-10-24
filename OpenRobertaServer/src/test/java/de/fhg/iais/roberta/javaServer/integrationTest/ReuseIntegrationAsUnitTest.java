package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ValidationFileAssert;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ProjectWorkflowRestController;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.util.visitor.StackmachinePrettyPrinter;

public class ReuseIntegrationAsUnitTest {

    private static final boolean STORE_ALWAYS_DATA_INTO_FILES = true;
    private static final boolean COMPARE_EXPECTED_AND_ACTUAL = true;
    private static final boolean LOG_NAMES_ON_SUCCESS = false;
    private static final boolean TEST_SUCCEEDS_EVEN_IF_REGENERATION_FAILS = false;
    private static final boolean TEST_SUCCEEDS_EVEN_IF_CODE_GENERATION_FAILS = false;
    private static final boolean TEST_SUCCEEDS_EVEN_IF_COLLECTOR_TEST_FAILS = false;

    private static final Logger LOG = LoggerFactory.getLogger(ReuseIntegrationAsUnitTest.class);
    private static final String ROBOT_NAME_FOR_COMMON_TESTS = "ev3lejosv1";
    private static final String[] ROBOTS_FOR_TARGET_LANGUAGE_GENERATION =
        {
            "ev3lejosv1",
            "calliope2017NoBlue",
            "ev3dev",
            "wedo"
        };

    private static final String COMMON = "common/";
    private static final String ROBOT_SPECIFIC = "robotSpecific/";
    private static final String EXPECTED = "_expected/";

    private static final String TARGET_DIR = "target/unitTests/";
    private static final String CROSS_COMPILER_TESTS = "/crossCompilerTests/";
    public static final String TEST_SPEC_YML = "classpath:/crossCompilerTests/testSpec.yml";

    private static final String AST_GENERATED = "astGenerated/";
    private static final String PROG_GENERATED = "progGenerated/";
    private static final String PROG_REGENERATED = "progRegenerated/";
    private static final String CONFIG_GENERATED = "configGenerated/";
    private static final String CONFIG_REGENERATED = "configRegenerated/";
    private static final String TARGET_LANGUAGE_GENERATED = "targetLanguage/";
    private static final String COLLECTOR_RESULTS = "collectorResults/";
    private static final String STACKMACHINE_CODE_GENERATED = "stackmachineLanguage/";
    private static final String TARGET_LANGUAGE_SOURCE = "targetSource/";

    private static JSONObject progDeclsFromTestSpec;
    private static JSONObject robotsFromTestSpec;

    RobotFactory testFactory;

    private int errorCountRegeneration = 0;
    private int successCountRegeneration = 0;
    private int errorCountCodeGeneration = 0;
    private int successCountCodeGeneration = 0;
    private int successCountCollectorTest = 0;
    private int errorCountCollectorTest = 0;

    @BeforeClass
    public static void setupClass() throws IOException {
        AstFactory.loadBlocks();
        Path path = Paths.get(TARGET_DIR);
        Files.createDirectories(path);
        JSONObject testSpecification = Util.loadYAML(TEST_SPEC_YML);
        progDeclsFromTestSpec = testSpecification.getJSONObject("progs");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");

        ValidationFileAssert.VALIDATION_DIRECTORY = Paths.get("src/test/resources" + CROSS_COMPILER_TESTS + EXPECTED);
        ValidationFileAssert.OUTPUT_DIRECTORY = Paths.get("target/unitTests/" + EXPECTED);
    }

    @AfterClass
    public static void afterClass() {
        ValidationFileAssert.resetDirectories();
    }

    @Test
    public void testAllCommonProgramsAsUnitTests() throws Exception {
        LOG.info("========= testing common programs");
        final String[] programNameArray = progDeclsFromTestSpec.keySet().toArray(new String[0]);
        Arrays.sort(programNameArray);
        {
            ArrayList<String> pluginDefines = new ArrayList<>(); // maybe used later to add properties
            testFactory = Util.configureRobotPlugin(ROBOT_NAME_FOR_COMMON_TESTS, "", "", pluginDefines);
            String defaultConfigXml = testFactory.getConfigurationDefault();
            String template = Util.readResourceContent(CROSS_COMPILER_TESTS + COMMON + "template/commonAstUnit.xml");
            LOG.info("testing XML and AST_GENERATED consistency for " + progDeclsFromTestSpec.length() + " common programs");
            for ( String programName : programNameArray ) {
                runGenerateAndRegenerateForOneCommonProgram(defaultConfigXml, template, programName);
            }
        }
        {
            Set<String> simProgramHasBeenGeneratedForThisProgram = new HashSet<>();
            for ( String robotName : ROBOTS_FOR_TARGET_LANGUAGE_GENERATION ) {
                List<String> pluginDefines = new ArrayList<>(); // maybe used later to add properties
                testFactory = Util.configureRobotPlugin(robotName, "", "", pluginDefines);
                JSONObject robotDeclFromTestSpec = robotsFromTestSpec.getJSONObject(robotName);
                String robotDir = robotDeclFromTestSpec.getString("template");
                String template = getTemplateWithConfigReplaced(robotDir, robotName);
                nextProg:
                for ( String programName : programNameArray ) {
                    boolean runSim = testFactory.hasSim() && !simProgramHasBeenGeneratedForThisProgram.contains(programName);
                    runTargetAndSimulationLanguageTestForOneCommonProgramAndOneRobot(robotName, programName, runSim, template);
                }
            }
        }
        checkAndShowTestResult();
    }

    @Ignore
    @Test
    public void testOneCommonProgrammAsUnitTest() throws Exception {
        String programName = "mathAndLists";
        String[] robotNames = {"ev3lejosv1"}; // set to null, if all robots should be tested; otherwise put the robots under test into the array
        {
            List<String> pluginDefines = new ArrayList<>(); // maybe used later to add properties
            testFactory = Util.configureRobotPlugin(ROBOT_NAME_FOR_COMMON_TESTS, "", "", pluginDefines);
            String defaultConfigXml = testFactory.getConfigurationDefault();
            String template = Util.readResourceContent(CROSS_COMPILER_TESTS + COMMON + "template/commonAstUnit.xml");
            runGenerateAndRegenerateForOneCommonProgram(defaultConfigXml, template, programName);
        }
        {
            String[] robotsToTest = robotNames == null ? ROBOTS_FOR_TARGET_LANGUAGE_GENERATION : robotNames;
            for ( String robotName : robotsToTest ) {
                setupRobotFactoryForRobot(robotName);
                JSONObject robotDeclFromTestSpec = robotsFromTestSpec.getJSONObject(robotName);
                String robotDir = robotDeclFromTestSpec.getString("template");
                String template = getTemplateWithConfigReplaced(robotDir, robotName);
                runTargetAndSimulationLanguageTestForOneCommonProgramAndOneRobot(robotName, programName, testFactory.hasSim(), template);
            }
        }
        checkAndShowTestResult();
    }

    private void runGenerateAndRegenerateForOneCommonProgram(String defaultConfigXml, String template, String programName) {
        String msgSuffix = programName + " (test of xml regeneration)";
        logStart(msgSuffix);
        JSONObject progDeclFromTestSpec = progDeclsFromTestSpec.getJSONObject(programName);
        String generatedFragmentXml = generateFinalCommonProgram(template, programName, progDeclFromTestSpec);
        boolean regenerationOk = compareGenerateAndRegenerateForOneProgram(COMMON, "", programName, generatedFragmentXml, defaultConfigXml);
        if ( regenerationOk ) {
            successCountRegeneration++;
            logSucc(msgSuffix);
        } else {
            errorCountRegeneration++;
            logFail(msgSuffix);
        }
    }

    private void runTargetAndSimulationLanguageTestForOneCommonProgramAndOneRobot(String robotName, String progName, boolean generateSim, String template) {
        String msgSuffix = progName + " (code generation for " + robotName + ")";
        logStart(msgSuffix);
        JSONObject codeGenProgDeclFromTestSpec = progDeclsFromTestSpec.getJSONObject(progName);
        JSONObject exclude = codeGenProgDeclFromTestSpec.optJSONObject("exclude");
        if ( exclude != null ) {
            for ( String excludeRobot : exclude.keySet() ) {
                if ( excludeRobot.equals(robotName) || excludeRobot.equals("ALL") ) {
                    LOG.info("!!!  prog " + progName + " for robot " + robotName + " is excluded. Reason: " + exclude.getString(excludeRobot));
                    return;
                }
            }
        }
        String exportXml = generateFinalCommonProgram(template, progName, codeGenProgDeclFromTestSpec);
        boolean codeGenerationOk = compareGeneratedTargetLanguageForOneProgram(COMMON, robotName, progName, exportXml);
        if ( generateSim ) {
            Pair<String, String> progConfPair = ProjectWorkflowRestController.splitExportXML(exportXml);
            String programXml = progConfPair.getFirst();
            String configXml = progConfPair.getSecond();
            Project.Builder builder = UnitTestHelper.setupWithConfigAndProgramXML(testFactory, programXml, configXml);
            builder.setRobot(robotName).setProgramName("NEPOprog").setSSID("test").setPassword("test").setLanguage(Language.ENGLISH);
            Project project = builder.build();
            String msg = UnitTestHelper.executeWorkflow("getsimulationcode", testFactory, project);
            String stackMachineCode = StackmachinePrettyPrinter.prettyPrint(new JSONObject(project.getCompiledHex()), true, true);
            codeGenerationOk = codeGenerationOk &&
                compareExpectedToGenerated(
                    CROSS_COMPILER_TESTS + EXPECTED + COMMON + STACKMACHINE_CODE_GENERATED,
                    progName,
                    stackMachineCode,
                    "json");
            if ( STORE_ALWAYS_DATA_INTO_FILES || !codeGenerationOk ) {
                storeDataIntoFiles(
                    EXPECTED + COMMON + STACKMACHINE_CODE_GENERATED,
                    "",
                    progName,
                    stackMachineCode,
                    "json");
            }
        }
        if ( codeGenerationOk ) {
            successCountCodeGeneration++;
            logSucc(msgSuffix);
        } else {
            errorCountCodeGeneration++;
            logFail(msgSuffix);
        }
    }

    private static String generateFinalCommonProgram(String template, String progName, JSONObject progDeclFromTestSpec) {
        String progSource = read("prog", progName + ".xml");
        Assert.assertNotNull(progSource, "program not found: " + progName);
        template = template.replaceAll("\\[\\[prog\\]\\]", progSource);
        String progFragmentName = progDeclFromTestSpec.optString("fragment");
        String progFragment = progFragmentName.isEmpty() ? "" : read("fragment", progFragmentName + ".xml");
        template = template.replaceAll("\\[\\[fragment\\]\\]", progFragment);
        String declName = progDeclFromTestSpec.optString("decl");
        Assert.assertNotNull(declName, "decl for program not found: " + progName);
        String decl = read("decl", declName + ".xml");
        template = template.replaceAll("\\[\\[decl\\]\\]", decl);
        return template;
    }

    private static String getTemplateWithConfigReplaced(String robotDir, String robotName) {
        String template = Util.readResourceContent(CROSS_COMPILER_TESTS + COMMON + "template/" + robotDir + ".xml");
        Properties robotProperties = Util.loadProperties("classpath:/" + robotName + ".properties");
        String defaultConfigurationURI = robotProperties.getProperty("robot.configuration.default");
        String defaultConfig = Util.readResourceContent(defaultConfigurationURI);
        final String templateWithConfig = template.replaceAll("\\[\\[conf\\]\\]", defaultConfig);
        return templateWithConfig;
    }

    private static String read(String directoryName, String progNameWithXmlSuffix) {
        try {
            return Util.readResourceContent(CROSS_COMPILER_TESTS + COMMON + directoryName + "/" + progNameWithXmlSuffix);
        } catch ( Exception e ) {
            // this happens, if no decl or fragment is available for the program given. This is legal.
            return null;
        }
    }

    @Test
    public void testAllRobotSpecificProgramsAsUnitTests() throws Exception {
        LOG.info("========= testing robot specific programs");
        final String[] robotNameArray = robotsFromTestSpec.keySet().toArray(new String[0]);
        Arrays.sort(robotNameArray);
        for ( final String robotName : robotNameArray ) {
            LOG.info("========= processing robot: " + robotName);
            final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
            JSONArray programsToExcludeJA = robotsFromTestSpec.getJSONObject(robotName).optJSONArray("exclude");
            final List<String> excludedPrograms = new ArrayList<>(programsToExcludeJA == null ? 0 : programsToExcludeJA.length());
            if ( programsToExcludeJA != null ) {
                for ( Iterator<Object> it = programsToExcludeJA.iterator(); it.hasNext(); ) {
                    Object object = it.next();
                    excludedPrograms.add(object.toString());
                }
            }
            de.fhg.iais.roberta.util.FileUtils.fileStreamOfResourceDirectory(resourceDirectory). //
                filter(f -> f.endsWith(".xml"))
                .forEach(f -> runRegenerateAndCodeGenerationForOneRobotSpecificProgram(resourceDirectory, f, robotName, excludedPrograms));
        }
        checkAndShowTestResult();
    }

    @Ignore
    @Test
    public void testOneRobotSpecificProgramAsUnitTests() throws Exception {
        String robotName = "mbot2";
        String programName = "sensors";
        LOG.info("========= testing program " + programName + " for robot " + robotName);
        final String resourceDirectory = setupRobotFactoryAndGetResourceDirForRobotSpecificTests(robotName);
        runRegenerateAndCodeGenerationForOneRobotSpecificProgram(resourceDirectory, programName + ".xml", robotName, Collections.emptyList());
        checkAndShowTestResult();
    }

    private void runRegenerateAndCodeGenerationForOneRobotSpecificProgram(
        String resourceDirectoryWithPrograms, String fileNameWithRobotSpecificTestProgram,
        String robotName,
        List<String> excludedPrograms) //
    {
        int index = fileNameWithRobotSpecificTestProgram.lastIndexOf(".xml");
        Assert.assertTrue(index > 0);
        String programName = fileNameWithRobotSpecificTestProgram.substring(0, index);
        if ( "error".equals(programName) || excludedPrograms.contains(programName) ) {
            LOG.info("ignoring excluded program \"" + programName + "\"");
            return;
        }
        String msgSuffix = robotName + "/" + programName;
        logStart("regeneration and code generation for " + msgSuffix);
        String programFileName = resourceDirectoryWithPrograms + fileNameWithRobotSpecificTestProgram;
        String exportXmlText = Util.readResourceContent(programFileName);
        Pair<String, String> progConfPair = ProjectWorkflowRestController.splitExportXML(exportXmlText);
        String programXml = progConfPair.getFirst();
        String configXml = progConfPair.getSecond();
        boolean regenerationOk = compareGenerateAndRegenerateForOneProgram(ROBOT_SPECIFIC, robotName, programName, programXml, configXml);
        if ( regenerationOk ) {
            successCountRegeneration++;
            logSucc("regeneration " + msgSuffix);
        } else {
            errorCountRegeneration++;
            logFail("regeneration " + msgSuffix);
        }
        boolean codeGenerationOk = compareGeneratedTargetLanguageForOneProgram(ROBOT_SPECIFIC, robotName, programName, exportXmlText);
        if ( codeGenerationOk ) {
            successCountCodeGeneration++;
            logSucc("code generation " + msgSuffix);
        } else {
            errorCountCodeGeneration++;
            logFail("code generation " + msgSuffix);

        }

        boolean collectorResultsOk = compareCollectorResultsForOneProgram(ROBOT_SPECIFIC, robotName, programName, exportXmlText);
        if ( collectorResultsOk ) {
            successCountCollectorTest++;
            logSucc("collector results " + msgSuffix);
        } else {
            errorCountCollectorTest++;
            logFail("collector results " + msgSuffix);
        }
    }

    /**
     * accept xmlversion 3.1 program and config<br>
     * - generate the AST_GENERATED and compare it with the expected one (compare has to be implemented)
     * - generate the program and config ast embedded in a Project object.<br>
     * - from this Project object regenerate the program and config XML ("backtransformation")<br>
     * - check that the original and regenerated program are similar using XmlUnit<br>
     * - check that the original and regenerated config are similar using XmlUnit (deactivated, because they are VERY different<br>
     *
     * @param directory common or robotSpecific
     * @param robotName may be "" for common tests
     * @param programName
     * @param programXml
     * @param configXml
     * @param robotFactory
     * @return true, if everything went fine
     */
    private boolean compareGenerateAndRegenerateForOneProgram(
        String directory,
        String robotName,
        String programName,
        String programXml,
        String configXml) //
    {
        if ( programName.equals("error") ) {
            LOG.info("ignoring program error");
            return false;
        }
        String robotSubDirectory = robotName.equals("") ? "" : robotName + "/";
        BlockSet blockSet = null;
        try {
            blockSet = JaxbHelper.xml2BlockSet(programXml);
        } catch ( JAXBException e ) {
            LOG.error("invalid program (AST_GENERATED could not be generated)" + programName);
            return false;
        }
        if ( !"3.1".equals(blockSet.getXmlversion()) ) {
            LOG.error("program XML has NOT version 3.1: " + programName);
            return false;
        }
        Jaxb2ProgramAst transformer = new Jaxb2ProgramAst(testFactory);
        ProgramAst generatedAst = transformer.blocks2ast(blockSet);
        List<Phrase> blocks = generatedAst.getTree().get(0);
        StringBuilder sb = new StringBuilder();
        for ( int i = 2; i < blocks.size(); i++ ) {
            sb.append(blocks.get(i).toString()).append("\n");
        }
        String generatedAstAsString = sb.toString();
        if ( STORE_ALWAYS_DATA_INTO_FILES ) {
            storeDataIntoFiles(EXPECTED + directory + AST_GENERATED, robotSubDirectory, programName, generatedAstAsString, "ast");
        }

        boolean thisUnitTestIsOk = true;
        if ( COMPARE_EXPECTED_AND_ACTUAL ) {
            thisUnitTestIsOk =
                compareExpectedToGenerated(
                    CROSS_COMPILER_TESTS + EXPECTED + directory + AST_GENERATED + robotSubDirectory,
                    programName,
                    generatedAstAsString,
                    "ast");
        }
        Project.Builder builder = UnitTestHelper.setupWithConfigAndProgramXML(testFactory, programXml, configXml);
        Project project = builder.build();

        String regeneratedProgramXml = project.getAnnotatedProgramAsXml();
        String regeneratedConfigXml = project.getAnnotatedConfigurationAsXml();

        String diffProg;
        if ( COMPARE_EXPECTED_AND_ACTUAL ) {
            diffProg = UnitTestHelper.runXmlUnit(programXml, regeneratedProgramXml);
            // String diffConfig = UnitTestHelper.runXmlUnit(configXml, regeneratedConfigXml);
        } else {
            diffProg = null; // simulate ok
        }

        if ( diffProg != null ) { // || diffConfig != null
            if ( diffProg != null ) {
                LOG.error("error in program " + programName + " is: " + diffProg);
            }
            // if ( diffConfig != null ) {
            //    LOG.error(diffConfig);
            // }
            thisUnitTestIsOk = false;
        }
        if ( STORE_ALWAYS_DATA_INTO_FILES || !thisUnitTestIsOk ) {
            storeDataIntoFiles(directory + PROG_GENERATED, robotSubDirectory, programName, programXml, "xml");
            storeDataIntoFiles(directory + PROG_REGENERATED, robotSubDirectory, programName, regeneratedProgramXml, "xml");

            storeDataIntoFiles(directory + CONFIG_GENERATED, robotSubDirectory, programName, configXml, "xml");
            storeDataIntoFiles(directory + CONFIG_REGENERATED, robotSubDirectory, programName, regeneratedConfigXml, "xml");
        }
        return thisUnitTestIsOk;
    }

    /**
     * accept xmlversion 3.1 program and config<br>
     * - generate code for the test factory supplied (main languages are Java, C++, Python and the SimulationStackMachine<br>
     * - check that the program generated is the same as the expected one
     * <br>
     * if an error is detected, the data is written to the directory 'target/unitTests' for debugging
     *
     * @param directory Used to select the target directory for storing
     * @param programXml
     * @param configXml
     * @param programName
     * @param robotFactory
     */
    private boolean compareCollectorResultsForOneProgram(String directory, String robotName, String programName, String exportXml) //
    {
        Pair<String, String> progConfPair = ProjectWorkflowRestController.splitExportXML(exportXml);
        String programXml = progConfPair.getFirst();
        String configXml = progConfPair.getSecond();
        Project.Builder builder = UnitTestHelper.setupWithConfigAndProgramXML(testFactory, programXml, configXml);
        builder.setRobot(robotName).setProgramName("NEPOprog").setSSID("test").setPassword("test").setLanguage(Language.ENGLISH);
        Project project = builder.build();

        try {
            UnitTestHelper.executeWorkflow("showsource", testFactory, project);

            UsedHardwareBean usedHardwareBean = project.getWorkerResult(UsedHardwareBean.class);
            UsedMethodBean usedMethodBean = project.getWorkerResult(UsedMethodBean.class);

            List<Enum<?>> usedMethodsSorted = usedMethodBean.getUsedMethods().stream().sorted(Comparator.comparing(Enum::toString)).collect(Collectors.toList());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Sensors: \n");
            stringBuilder.append(usedHardwareBean.getUsedSensors());
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append("Actors: \n");
            stringBuilder.append(usedHardwareBean.getUsedActors());
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append("Methods: \n");
            stringBuilder.append(usedMethodsSorted);

            try {
                ValidationFileAssert.assertThat(stringBuilder.toString()).isEqualToValidationFile(directory + COLLECTOR_RESULTS + robotName + "/" + programName + ".txt");
                return true;
            } catch ( AssertionError e ) {
                LOG.error("collector results doesn't match for " + programName + " with error" + e.getMessage(), e);
                return false;
            }
        } catch ( Exception e ) {
            LOG.error("showsource workflow failed for " + programName + " with Exception " + e.getMessage(), e);
            return false;
        }
    }

    private boolean compareGeneratedTargetLanguageForOneProgram(String directory, String robotName, String programName, String exportXml) //
    {
        Pair<String, String> progConfPair = ProjectWorkflowRestController.splitExportXML(exportXml);
        String programXml = progConfPair.getFirst();
        String configXml = progConfPair.getSecond();
        Project.Builder builder = UnitTestHelper.setupWithConfigAndProgramXML(testFactory, programXml, configXml);
        builder.setRobot(robotName).setProgramName("NEPOprog").setSSID("test").setPassword("test").setLanguage(Language.ENGLISH);
        Project project = builder.build();

        boolean thisUnitTestIsOk;
        try {
            String msg = UnitTestHelper.executeWorkflow("showsource", testFactory, project);
            if ( msg != null ) {
                LOG.error("showsource workflow failed for " + programName + " with message " + msg);
                thisUnitTestIsOk = false;
                storeExportOfXml(directory, programName, robotName, programXml, configXml);
            } else {
                String generatedProgramSource = project.getSourceCode().toString();
                thisUnitTestIsOk =
                    compareExpectedToGenerated(
                        CROSS_COMPILER_TESTS + EXPECTED + directory + TARGET_LANGUAGE_GENERATED + robotName + "/",
                        programName,
                        generatedProgramSource,
                        testFactory.getSourceCodeFileExtension());
                if ( STORE_ALWAYS_DATA_INTO_FILES ) {
                    storeExportOfXml(directory, programName, robotName, programXml, configXml);
                    storeDataIntoFiles(
                        EXPECTED + directory + TARGET_LANGUAGE_GENERATED,
                        robotName,
                        programName,
                        generatedProgramSource,
                        testFactory.getSourceCodeFileExtension());
                }
            }
        } catch ( Exception e ) {
            LOG.error("showsource workflow failed for " + programName + " with Exception " + e.getMessage(), e);
            thisUnitTestIsOk = false;
        }
        return thisUnitTestIsOk;
    }

    private static final Pattern BACKSLASH_R = Pattern.compile("\\r");
    private static final Pattern BACKSLASH_N_PLUS_LINEEND = Pattern.compile("\\n+$");
    private static final Pattern BACKSLASH_N = Pattern.compile("\\n");
    private static final Pattern BACKSLASH_S_PLUS = Pattern.compile("\\s+");

    private static String replace(String source) {
        String target = BACKSLASH_R.matcher(source).replaceAll("");
        target = BACKSLASH_N_PLUS_LINEEND.matcher(target).replaceAll("");
        target = BACKSLASH_N.matcher(target).replaceAll("<NL>");
        target = BACKSLASH_S_PLUS.matcher(target).replaceAll("");
        return target;
    }

    private static boolean compareExpectedToGenerated(String resourceBase, String programName, String generated, String extension) {
        if ( COMPARE_EXPECTED_AND_ACTUAL ) {
            try {
                String expected = replace(Util.readResourceContent(resourceBase + programName + "." + extension));
                generated = replace(generated);
                boolean equals = generated.equals(expected);
                if ( !equals ) {
                    LOG.error("generated and expected differ for " + programName + "." + extension);
                }
                return equals;
            } catch ( Exception e ) {
                LOG.error("expected " + resourceBase + programName + "." + extension + " could not be read");
                return false;
            }
        } else {
            return true;
        }
    }

    private String setupRobotFactoryAndGetResourceDirForRobotSpecificTests(String robotName) {
        setupRobotFactoryForRobot(robotName);
        JSONObject robot = robotsFromTestSpec.getJSONObject(robotName);
        final String robotDir = robot.getString("dir");
        return CROSS_COMPILER_TESTS + ROBOT_SPECIFIC + robotDir + (robotDir.endsWith("/") ? "" : "/");
    }

    private void setupRobotFactoryForRobot(String robotName) {
        List<String> pluginDefines = new ArrayList<>(); // maybe used later to add properties
        testFactory = Util.configureRobotPlugin(robotName, "", "", pluginDefines);
    }

    private static void storeExportOfXml(String directory, String programName, String robotName, String programXml, String configXml) {
        String export = "<?xml version=\"1.0\"?>\n<export xmlns=\"http://de.fhg.iais.roberta.blockly\">\n<program>\n";
        export += programXml;
        export += "\n</program>\n<config>\n";
        export += configXml;
        export += "\n</config>\n</export>\n";
        storeDataIntoFiles(directory + TARGET_LANGUAGE_SOURCE, robotName, programName, export, "xml");
    }

    private static void storeDataIntoFiles(String directory, String robotName, String programName, String source, String suffix) {
        try {
            String robotSubDirectory = robotName.equals("") ? "" : robotName + "/";
            String filename = TARGET_DIR + directory + robotSubDirectory + programName + "." + suffix;
            if ( "xml".equals(suffix) ) {
                prettyPrintXml(source, filename);
            } else {
                FileUtils.writeStringToFile(new File(filename), source, StandardCharsets.UTF_8.displayName());
            }
        } catch ( Exception e ) {
            Assert.fail("Storing " + programName + " into directory " + TARGET_DIR + " failed");
        }
    }

    private static void prettyPrintXml(String xmlString, String filename) throws Exception {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        file.createNewFile(); // if file already exists will do nothing
        FileOutputStream oFile = new FileOutputStream(file, false);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        xmlString = xmlString.replaceAll("[\r\t\n]+ *<", "<");
        Document document = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));
        try (OutputStream os = new FileOutputStream(file)) {
            Result result = new StreamResult(os);
            Source source = new DOMSource(document);
            transformer.transform(source, result);
            os.flush();
        }
    }

    private void checkAndShowTestResult() {
        LOG.info("succeeding regeneration tests: " + successCountRegeneration);
        if ( errorCountRegeneration > 0 ) {
            LOG.error("regeneration ERRORS found: " + errorCountRegeneration);
        }
        LOG.info("succeeding code generation tests: " + successCountCodeGeneration);
        if ( errorCountCodeGeneration > 0 ) {
            LOG.error("code generation ERRORS found: " + errorCountCodeGeneration);
        }
        LOG.info("succeeding collector tests: " + successCountCollectorTest);
        if ( errorCountCollectorTest > 0 ) {
            LOG.error("collector ERRORS found: " + errorCountCollectorTest);
        }
        boolean regenerationSucceeds = errorCountRegeneration == 0 || TEST_SUCCEEDS_EVEN_IF_REGENERATION_FAILS;
        boolean codeGenerationSucceeds = errorCountCodeGeneration == 0 || TEST_SUCCEEDS_EVEN_IF_CODE_GENERATION_FAILS;
        boolean collectorTestsSucceeds = errorCountCollectorTest == 0 || TEST_SUCCEEDS_EVEN_IF_COLLECTOR_TEST_FAILS;
        Assert.assertTrue("test errors", regenerationSucceeds && codeGenerationSucceeds && collectorTestsSucceeds);
    }

    private static void logStart(String msg) {
        if ( LOG_NAMES_ON_SUCCESS ) {
            LOG.info("=== program " + msg);
        }
    }

    private static void logSucc(String msg) {
        if ( LOG_NAMES_ON_SUCCESS ) {
            LOG.info("+++ program " + msg);
        }
    }

    private static void logFail(String msg) {
        LOG.info("--- FAIL program " + msg);
    }
}
