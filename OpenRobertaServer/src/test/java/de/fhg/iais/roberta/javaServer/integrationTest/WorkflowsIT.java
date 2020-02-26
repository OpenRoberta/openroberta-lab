package de.fhg.iais.roberta.javaServer.integrationTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.FileUtils;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;
import static org.junit.Assert.fail;

/**
 * <b>Testing the generation of native code and the CROSSCOMPILER</b><br>
 * <br>
 * The tests in this class are integration tests. The front end is <b>not</b> tested. The tests deliver programs (either NEPO programs encoded in XML or native
 * programs encoded as expected by the crosscompilers) to the various crosscompilers and check whether the expected response is returned ("ok", "error").<br>
 * <br>
 * In src/test/crossCompilerTests/robotSpecific for each robot "x" a directory with name "x" is expected. This directory contains all tests for this robot. The
 * helper methods "compileNepo" and "compileNative" are used to execute tests.<br>
 * <br>
 * TODO: add tests for generating simulation programs
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class WorkflowsIT {

    private enum Result {
        SUCCESS, // everything worked as expected
        PARTIAL_SUCCESS, // everything worked, but the program contains disabled blocks
        FAILURE // the workflow failed
    }

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowsIT.class);

    private static final String TEST_SPEC_YAML = "testSpec.yml";

    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');

    private static final String PARTIAL_SUCCESS_DEF = "disabled=\"true\"";

    private static JSONObject robots;
    private static boolean showSuccess;

    private static String resourceBase;

    private static RobotCommunicator robotCommunicator;
    private static ServerProperties serverProperties;
    private static Map<String, IRobotFactory> pluginMap;
    private static HttpSessionState httpSessionState;

    private final List<String> results = new ArrayList<>();

    @BeforeClass
    public static void setupClass() throws Exception {
        ServerStarter.initLoggingBeforeFirstUse(new String[0]);
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            fail();
        }
        Properties baseServerProperties = Util.loadProperties(null);
        serverProperties = new ServerProperties(baseServerProperties);
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
        httpSessionState = HttpSessionState.initOnlyLegalForDebugging("", pluginMap, serverProperties, 1);

        resourceBase = "/crossCompilerTests/robotSpecific/";
        JSONObject testSpecification = Util.loadYAML("classpath:" + resourceBase + TEST_SPEC_YAML);
        robots = testSpecification.getJSONObject("robots");
        showSuccess = testSpecification.getBoolean("showsuccess");
    }

    @After
    public void tearDownAndPrintResults() {
        LOG.info("XXXXXXXXXX result of cross compiler test" + (results.size() == 1 ? "" : "s") + " XXXXXXXXXX");
        for ( String result : results ) {
            LOG.info(result);
        }
    }

    /**
     * test a single workflow for different robots. May help testing ... - supply the workflow name - supply the list of robots (you may copy from the console
     * output) - the showsource workflow is always run first
     */

    @Test
    public void testSingleWorkflow() throws Exception {
        final String robotName = "nxt";
        String workflowName = "compile";
        String programFileName = "sensors_default";
        String robotDir = robots.getJSONObject(robotName).getString("dir");
        String fullResource = resourceBase + robotDir + "/allBlocks/" + programFileName + ".xml";
        String xmlText = Util.readResourceContent(fullResource);
        Pair<Result, String> showSourceResult = generateSourceWithShowSource(programFileName, xmlText, robotName);
        if ( showSourceResult.getFirst() == Result.FAILURE ) {
            fail();
        }
        Result result = executeWorkflow(workflowName, robotName, programFileName, xmlText, showSourceResult.getSecond());
        if ( result == Result.FAILURE ) {
            fail();
        }
    }

    @Test
    public void testWorkflows() throws Exception {
        LOG.info("XXXXXXXXXX START of the workflow executions XXXXXXXXXX");
        AtomicBoolean resultAcc = new AtomicBoolean(true);
        try {
            robots.keySet().parallelStream().forEach(robotName -> {
                String robotDir = robots.getJSONObject(robotName).getString("dir") + "/allBlocks";
                LOG.info(resourceBase);
                LOG.info(robotDir);
                FileUtils
                    .fileStreamOfResourceDirectory(resourceBase + robotDir)
                    .filter(programFileName -> programFileName.endsWith(".xml"))
                    .forEach(programFileName -> {

                        String fullResource = resourceBase + robotDir + "/" + programFileName;
                        String xmlText = Util.readResourceContent(fullResource);

                        // test showsource workflow first, this should always work
                        Pair<Result, String> showSourceResult = generateSourceWithShowSource(programFileName, xmlText, robotName);
                        if ( showSourceResult.getFirst() == Result.FAILURE ) {
                            LOG.error("Could not generate source code for robot {}", robotName);
                            resultAcc.set(false);
                            return;
                        }

                        Set<String> allWorkflowsOfRobot = pluginMap.get(robotName).getWorkflows();
                        List<String> workflowsWithoutShowSource =
                            allWorkflowsOfRobot.stream().filter(s -> !s.equals("showsource")).collect(Collectors.toList());
                        for ( String workflow : workflowsWithoutShowSource ) {
                            Result resultNext = executeWorkflow(workflow, robotName, programFileName, xmlText, showSourceResult.getSecond());
                            resultAcc.set(resultAcc.get() && (resultNext != Result.FAILURE));
                        }
                    });
            });
        } catch ( Exception e ) {
            LOG.error("XXXXXXXXXX test terminated with an unexpected exception XXXXXXXXXX", e);
            resultAcc.set(false);
        }
        if ( resultAcc.get() ) {
            LOG.info("XXXXXXXXXX all of the workflow executions succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the workflow executions FAILED XXXXXXXXXX");
            fail();
        }
    }

    private Pair<Result, String> generateSourceWithShowSource(String programName, String program, String robotName) {
        String reason = "?";
        Result result = Result.FAILURE;
        String sourceCode = "";
        logStart(robotName, "showsource");
        try {
            Project.Builder builder = UnitTestHelper.setupWithExportXML(pluginMap.get(robotName), program);
            builder.setRobot(robotName);
            builder.setProgramName("NEPOprog");
            builder.setSSID("test");
            builder.setPassword("test");
            builder.setLanguage(Language.ENGLISH);
            Project showSourceProject = builder.build();

            // Every robot needs at least a show source workflow
            ProjectService.executeWorkflow("showsource", pluginMap.get(robotName), showSourceProject);
            sourceCode = showSourceProject.getSourceCode().toString();
            result = showSourceProject.hasSucceeded() ? (program.contains(PARTIAL_SUCCESS_DEF) ? Result.PARTIAL_SUCCESS : Result.SUCCESS) : Result.FAILURE;
            reason = String.valueOf(showSourceProject.getResult());
        } catch ( Exception e ) {
            LOG.error("Executing workflow failed", e);
            result = Result.FAILURE;
            reason = "exception (" + e.getMessage() + ")";
        }
        log(result, robotName, "showsource " + programName, reason);
        return Pair.of(result, sourceCode);
    }

    private Result executeWorkflow(String workflow, String robotName, String programName, String program, String sourceCode) {
        String reason = "?";
        Result result = Result.FAILURE;
        logStart(robotName, workflow);
        try {
            String token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
            IRobotFactory factory = pluginMap.get(robotName);
            Project.Builder builder;
            if ( workflow.contains("native") ) {
                builder = UnitTestHelper.setupWithNativeSource(factory, sourceCode);
            } else if ( workflow.endsWith("reset") ) {
                builder = UnitTestHelper.setupWithResetFirmware(factory);
            } else {
                builder = UnitTestHelper.setupWithExportXML(factory, program);
            }
            builder.setRobot(robotName);
            builder.setProgramName("NEPOprog");
            builder.setSSID("test");
            builder.setPassword("test");
            builder.setRobotCommunicator(robotCommunicator);
            builder.setToken(token);
            builder.setLanguage(Language.ENGLISH);
            Project project = builder.build();

            ProjectService.executeWorkflow(workflow, factory, project);
            if ( !project.hasSucceeded()
                && workflow.contains("run")
                && StringUtils.containsIgnoreCase(pluginMap.get(robotName).getConnectionType(), ("token")) ) {
                result = project.getResult() == Key.ROBOT_NOT_CONNECTED ? (program.contains(PARTIAL_SUCCESS_DEF) ? Result.PARTIAL_SUCCESS : Result.SUCCESS) : Result.FAILURE;
                reason = String.valueOf(project.getResult());
            } else {
                result = project.hasSucceeded() ? (program.contains(PARTIAL_SUCCESS_DEF) ? Result.PARTIAL_SUCCESS : Result.SUCCESS) : Result.FAILURE;
                reason = String.valueOf(project.getResult());
            }
        } catch ( Exception e ) {
            LOG.error("Executing workflow failed", e);
            result = Result.FAILURE;
            reason = "exception (" + e.getMessage() + ")";
        }
        log(result, robotName, workflow + " " + programName, reason);
        return result;
    }

    private void logStart(String name, String fullResource) {
        String format = "[[[[[[[[[[ Robot: %-15s running workflow: %s";
        String msg = String.format(format, name, fullResource);
        LOG.info(msg);
    }

    private void log(Result result, String name, String fullResource, String reason) {
        String format;
        if ( result == Result.SUCCESS || result == Result.PARTIAL_SUCCESS ) {
            format = "++++++++++ Robot: %-15s succeeded: %s";
        } else {
            format = "---------- Robot: %-15s FAILED (" + reason + "): %s";
            showFailingProgram(httpSessionState.getToken());
        }
        LOG.info(String.format(format, name, fullResource));
        LOG.info("]]]]]]]]]]");
        if ( result == Result.SUCCESS || result == Result.PARTIAL_SUCCESS ) {
            if ( showSuccess ) {
                if (result == Result.SUCCESS) {
                    results.add(String.format("succ; %-15s; %-60s;", name, fullResource));
                } else {
                    results.add(String.format("part; %-15s; %-60s;", name, fullResource));
                }
            }
        } else {
            results.add(String.format("fail; %-15s; %-60s;", name, fullResource));
        }
    }

    private void showFailingProgram(String token) {
        try {
            String tokenDir = serverProperties.getTempDir() + token;
            Util.fileStreamOfFileDirectory(tokenDir).forEach(f -> showFailingProgramFromTokenDir(tokenDir, f));
        } catch ( Exception e ) {
            LOG.error("could not show the failing program. Probably the generation failed.");
        }
    }

    private static void showFailingProgramFromTokenDir(String tokenDir, String dir) {
        String sourceDir = tokenDir + "/" + dir + "/source";
        Util.fileStreamOfFileDirectory(sourceDir).forEach(f -> showFailingProgramFromSourceDir(sourceDir, f));
    }

    private static void showFailingProgramFromSourceDir(String sourceDir, String fileName) {
        String sourceFile = sourceDir + "/" + fileName;
        List<String> sourceLines = Util.readFileLines(fileName);
        StringBuilder sb = new StringBuilder();
        int lineCounter = 1;
        for ( String sourceLine : sourceLines ) {
            sb.append(String.format("%-4d %s", lineCounter++, sourceLine)).append("\n");
        }
        LOG.error("failing source from file: " + sourceFile + " is:\n" + sb.toString());
    }
}