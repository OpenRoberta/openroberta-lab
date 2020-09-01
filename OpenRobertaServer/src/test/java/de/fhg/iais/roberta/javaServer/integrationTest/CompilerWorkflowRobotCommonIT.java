package de.fhg.iais.roberta.javaServer.integrationTest;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ProjectWorkflowRestController;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>1. Testing the generation of native code and the results of crosscompilers compiling that code<br>
 * 2. Testing the execution of all workflows as declared in robot plugin properties</b><br>
 * <br>
 * This class contains integration tests. The front end is <b>not</b> tested. The resource "/crossCompilerTests/testSpec.yml" contains the list of robots under
 * test and the list of programs under test. Please have a look at this resource file and the `_README.md` in the same directory.<br>
 * <br>
 * For all robots all programs are generated and used. The number of tests executes is 2*(# of robots)*(# of progs).<br>
 * <br>
 * The program "workflowTest" is used to execute all declared workflows for a robot. The number of tests executed is (# of robots)*(# of workflows).
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowRobotCommonIT {
    private static final Logger LOG = LoggerFactory.getLogger("COMMON-IT");
    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final String[] ARGS =
        {
            "server.log.level=DEBUG",
            "server.log.configfile=/logback-test.xml"
        };

    private static final String RESOURCE_BASE = "/crossCompilerTests/common/";
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');
    private static final String PARTIAL_SUCCESS_DEF = "disabled=\"true\"";
    private static final String WORKFLOWTESTPROG_NAME = "workflowTest";

    private static ServerProperties serverProperties;
    private static RobotCommunicator robotCommunicator;
    private static Map<String, IRobotFactory> pluginMap;
    private static HttpSessionState httpSessionState;

    private static boolean crosscompilerCall;
    private static boolean showSuccess;
    private static JSONObject robotsFromTestSpec;
    private static JSONObject progDeclsFromTestSpec;

    private static boolean resultAcc = true;

    private static List<String> resultsCrosscompilerCalls = new ArrayList<>();
    private static List<String> resultsGenSimulationCalls = new ArrayList<>();
    private static List<String> resultsWorkflowExecutions = new ArrayList<>();

    @Mock
    private DbSession dbSession;
    @Mock
    private SessionFactoryWrapper sessionFactoryWrapper;

    private ProjectWorkflowRestController restWorkflow;
    private ClientAdmin restAdmin;

    @BeforeClass
    public static void setupClass() throws Exception {
        ServerStarter.initLoggingBeforeFirstUse(ARGS);
        LOG.info("XXXXXXXXXX START of COMMON-IT XXXXXXXXXX");
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            fail();
        }
        Properties baseServerProperties = Util.loadProperties(null);
        serverProperties = new ServerProperties(baseServerProperties);
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
        httpSessionState = HttpSessionState.initOnlyLegalForDebugging("", pluginMap, serverProperties, 1);
        JSONObject testSpecification = Util.loadYAML("classpath:/crossCompilerTests/testSpec.yml");
        crosscompilerCall = testSpecification.getBoolean("crosscompilercall");
        showSuccess = testSpecification.getBoolean("showsuccess");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");
        progDeclsFromTestSpec = testSpecification.getJSONObject("progs");
        Set<String> robots = robotsFromTestSpec.keySet();
        Set<String> programs = progDeclsFromTestSpec.keySet();
        StringBuilder sb = new StringBuilder();
        sb.append("\nfrom ").append(robots.size()).append(" robots and ").append(programs.size()).append(" program we generate ");
        sb.append(2 * robots.size() * programs.size()).append(" programs.\nRobots are:\n    ");
        for ( String robot : robots ) {
            sb.append(robot).append("\n    ");
        }
        sb.append("\nPrograms are:\n    ");
        for ( String program : programs ) {
            sb.append(program).append("\n    ");
        }
        LOG.info(sb.toString());
    }

    @AfterClass
    public static void printResults() {
        logSummary();
        LOG.info("XXXXXXXXXX results of COMMON-IT crosscompilercall XXXXXXXXXX");
        for ( String result : resultsCrosscompilerCalls ) {
            LOG.info(result);
        }
        LOG.info("XXXXXXXXXX results of COMMON-IT generate simulation code XXXXXXXXXX");
        for ( String result : resultsGenSimulationCalls ) {
            LOG.info(result);
        }
        LOG.info("XXXXXXXXXX results of COMMON-IT workflow executions XXXXXXXXXX");
        for ( String result : resultsWorkflowExecutions ) {
            LOG.info(result);
        }
        logSummary();
        LOG.info("XXXXXXXXXX END of COMMON-IT XXXXXXXXXX");
    }

    @Before
    public void setupTest() throws Exception {
        this.restWorkflow = new ProjectWorkflowRestController(robotCommunicator);
        this.restAdmin = new ClientAdmin(robotCommunicator, serverProperties);
        when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        doNothing().when(this.dbSession).commit();
    }

    /**
     * <b>run the common integration tests</b><br>
     * <br>
     * - iterate about all robots<br>
     * - get the template for the robot, inject the configuration into the template<br>
     * - for a robot under test iterate about all programs<br>
     * - inject the program, optional variable and function declaration to generate the final program as blockly XML<br>
     * - run the cross compiler and check the return code<br>
     * - run the simulation code generator and check the return code<br>
     * - run all workflows for the robot under test<br>
     * - remember the results
     */
    @Test
    public void testCommonPart() throws Exception {
        try {
            final String[] robotNameArray = robotsFromTestSpec.keySet().toArray(new String[0]);
            Arrays.sort(robotNameArray);
            for ( String robotName : robotNameArray ) {
                JSONObject robotDeclFromTestSpec = robotsFromTestSpec.getJSONObject(robotName);
                String robotDir = robotDeclFromTestSpec.getString("template");
                String templateWithConfig = getTemplateWithConfigReplaced(robotDir, robotName);
                final String[] programNameArray = progDeclsFromTestSpec.keySet().toArray(new String[0]);
                Arrays.sort(programNameArray);
                nextProg: for ( String progName : programNameArray ) {
                    JSONObject progDeclFromTestSpec = progDeclsFromTestSpec.getJSONObject(progName);
                    JSONObject exclude = progDeclFromTestSpec.optJSONObject("exclude");
                    if ( exclude != null ) {
                        for ( String excludeRobot : exclude.keySet() ) {
                            if ( excludeRobot.equals(robotName) || excludeRobot.equals("ALL") ) {
                                LOG.info("########## for " + robotName + " prog " + progName + " is excluded. Reason: " + exclude.getString(excludeRobot));
                                continue nextProg;
                            }
                        }
                    }
                    String generatedXml = generateFinalProgram(templateWithConfig, progName, progDeclFromTestSpec);
                    resultAcc = (compileProgramViaRestService(robotName, progName, generatedXml) != Result.FAILURE) && resultAcc;
                    IRobotFactory factory = pluginMap.get(robotName);
                    if ( factory.hasSim() ) {
                        resultAcc = (genSimulationCodeViaRestService(robotName, progName, generatedXml) != Result.FAILURE) && resultAcc;
                    }
                }
                JSONObject workflowDeclFromTestSpec = progDeclsFromTestSpec.getJSONObject(WORKFLOWTESTPROG_NAME);
                if ( crosscompilerCall ) {
                    String generatedWorkflowXml = generateFinalProgram(templateWithConfig, WORKFLOWTESTPROG_NAME, workflowDeclFromTestSpec);
                    resultAcc = (executeAllWorkflows(robotName, generatedWorkflowXml)) && resultAcc;
                }
            }
        } catch ( Exception e ) {
            LOG.error("---------- test terminated with an unexpected exception ----------", e);
            resultAcc = false;
        }
    }

    /**
     * generate and compile a program for some robots given. May help testing ... . Usually @Ignore-d<br>
     * <br>
     * - supply the program name<br>
     * - supply the list of robots (you may copy from the console output)<br>
     * - decide whether to generate only or to generate and compile
     */
    @Ignore
    @Test
    public void testCompileOneProgram() {
        String progName = "controlFlowDecisons";
        Collection<String> robots = Arrays.asList("wedo");
        boolean generateOnly = true;
        for ( String robotName : robots ) {
            LOG.info("********** crosscompilation for program " + progName + " and robot " + robotName + " **********");
            String robotDir = robotsFromTestSpec.getJSONObject(robotName).getString("template");
            final String templateWithConfig = getTemplateWithConfigReplaced(robotDir, robotName);
            final String generatedProgramXml = generateFinalProgram(templateWithConfig, progName, progDeclsFromTestSpec.getJSONObject(progName));
            if ( generateOnly ) {
                LOG.info("********** generated source code for " + progName + " and robot " + robotName + " is: **********\n" + generatedProgramXml);
            } else {
                compileProgramViaRestService(robotName, progName, generatedProgramXml);
            }
        }
    }

    /**
     * generate simulation code for a robots given. May help testing ... . Usually @Ignore-d<br>
     * <br>
     * - supply the program name<br>
     * - supply the robot<br>
     * - decide whether to show the generated sim code or nor
     */
    @Ignore
    @Test
    public void testGenSimulationOneProgram() {
        String progName = "controlFlowDecisons";
        String robotName = "nxt";
        boolean showGeneratedSimCode = true;

        LOG.info("********** generate simluation code for program " + progName + " and robot " + robotName + " **********");
        String robotDir = robotsFromTestSpec.getJSONObject(robotName).getString("template");
        final String templateWithConfig = getTemplateWithConfigReplaced(robotDir, robotName);
        final String generatedProgramXml = generateFinalProgram(templateWithConfig, progName, progDeclsFromTestSpec.getJSONObject(progName));
        genSimulationCodeViaRestService(robotName, progName, generatedProgramXml);
        if ( showGeneratedSimCode ) {
            LOG.info("********** simluation code for " + progName + " for robot " + robotName + " **********:\n" + null);
        } else {
        }
    }

    /**
     * test a single workflow for a single robot with a robot specific program. May help testing ... . Usually @Ignore-d<br>
     * - supply the workflow name<br>
     * - supply the robot<br>
     * - supply the program (usable for the robot given)<br>
     * the showsource workflow is always run first, to get the generated target program, then the workflow is run
     */
    @Ignore
    @Test
    public void testSingleWorkflow() throws Exception {
        String workflowName = "reset";
        final String robotName = "festobionic";
        String pathToProgramFile = "robotSpecific/festobionic/sensors.xml"; // relative to OpenRobertaServer/src/test/resources/crossCompilerTests
        String programFileName = "sensors";
        String fullResource = "/crossCompilerTests/" + pathToProgramFile + "/" + programFileName + ".xml";
        String xmlText = Util.readResourceContent(fullResource);
        Pair<Result, String> showSourceResult = executeWorkflowShowSource(programFileName, xmlText, robotName);
        if ( showSourceResult.getFirst() == Result.FAILURE ) {
            fail();
        }
        Result result = executeWorkflow(workflowName, robotName, programFileName, xmlText, showSourceResult.getSecond());
        if ( result == Result.FAILURE ) {
            fail();
        }
    }

    /**
     * call the REST service, which calls the crosscompiler
     *
     * @param robotName ...
     * @param progName ...
     * @param programAndConfigXml ...
     * @return Result.SUCCESS, if the crosscompiler call succeeded, Result.FAILURE otherwise
     */
    private Result compileProgramViaRestService(String robotName, String progName, String programAndConfigXml) {
        String reason = "?";
        Result result;
        logStart(robotName, "crosscompile", progName);
        try {
            String token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
            httpSessionState.setToken(token);
            if ( crosscompilerCall ) {
                setRobotTo(robotName);
                JSONObject cmd = JSONUtilForServer.mkD("{'programName':'prog','language':'de'}");
                cmd.getJSONObject("data").put("progXML", programAndConfigXml);
                Response response = this.restWorkflow.compileProgram(FullRestRequest.make(cmd));
                result = checkEntityRc(response, "ok", "PROGRAM_INVALID_STATEMETNS");
                reason = "response-info";
            } else {
                result = Result.SUCCESS;
                reason = "crosscompiler not called";
            }
        } catch ( Exception e ) {
            LOG.error("ProjectWorkflowRestController failed", e);
            result = Result.FAILURE;
            reason = "exception (" + e.getMessage() + ")";
        }
        logResult(resultsCrosscompilerCalls, result, robotName, progName, reason);
        return result;
    }

    /**
     * call the REST service, which generates simulation code
     *
     * @param robotName ...
     * @param progName ...
     * @param programAndConfXml ...
     * @return Result.SUCCESS, if the simulation code was generated, Result.FAILURE otherwise
     */
    private Result genSimulationCodeViaRestService(String robotName, String progName, String programAndConfXml) {
        String reason = "?";
        Result result;
        logStart(robotName, "genSimCode", progName);
        try {
            String token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
            httpSessionState.setToken(token);
            setRobotTo(robotName);
            JSONObject cmd = JSONUtilForServer.mkD("{'programName':'prog','language':'de'}");
            Export jaxbImportExport = JaxbHelper.xml2Element(programAndConfXml, Export.class);
            String programXml = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
            cmd.getJSONObject("data").put("progXML", programXml);
            Response response = this.restWorkflow.getSimulationVMCode(FullRestRequest.make(cmd));
            result = checkEntityRc(response, "ok", "PROGRAM_INVALID_STATEMETNS");
            reason = "response-info";
        } catch ( Exception e ) {
            LOG.error("ProjectWorkflowRestController failed", e);
            result = Result.FAILURE;
            reason = "exception (" + e.getMessage() + ")";
        }
        logResult(resultsGenSimulationCalls, result, robotName, progName, reason);
        return result;

    }

    /**
     * for a robot under test execute all workflows, showsource workflow is executed as the first one to get the generated source
     *
     * @param robotName ...
     * @param programXml ...
     * @return
     */
    private boolean executeAllWorkflows(String robotName, String programXml) {
        Pair<Result, String> showSourceResult = executeWorkflowShowSource(WORKFLOWTESTPROG_NAME, programXml, robotName);
        if ( showSourceResult.getFirst() == Result.FAILURE ) {
            LOG.error("Could not generate source code for robot {}", robotName);
            return false;
        }

        Set<String> allWorkflowsOfRobot = pluginMap.get(robotName).getWorkflows();
        List<String> workflowsWithoutShowSource = allWorkflowsOfRobot.stream().filter(s -> !s.equals("showsource")).collect(Collectors.toList());
        boolean resultAcc = true;
        for ( String workflow : workflowsWithoutShowSource ) {
            resultAcc = (executeWorkflow(workflow, robotName, WORKFLOWTESTPROG_NAME, programXml, showSourceResult.getSecond()) != Result.FAILURE) && resultAcc;
        }
        return resultAcc;
    }

    /**
     * execute one workflow. Special handling is needed for the workflows "native" and "reset".
     *
     * @param workflow the workflow to be tested
     * @param robotName the name of the robot, for which the workflow is defined
     * @param programName the name of the program to be used in the workflow
     * @param programXml the XML of the program
     * @param sourceCode the sourcecode of the program as geneated by the "showsource" workflow
     * @return
     */
    private Result executeWorkflow(String workflow, String robotName, String programName, String programXml, String sourceCode) {
        String reason = "?";
        Result result;
        logStart(robotName, "workflow", workflow);
        try {
            String token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
            IRobotFactory factory = pluginMap.get(robotName);
            Project.Builder builder;
            if ( workflow.contains("native") ) {
                builder = UnitTestHelper.setupWithNativeSource(factory, sourceCode);
            } else if ( workflow.endsWith("reset") ) {
                builder = UnitTestHelper.setupWithResetFirmware(factory);
            } else {
                builder = UnitTestHelper.setupWithExportXML(factory, programXml);
            }
            builder.setRobot(robotName).setProgramName("NEPOprog").setSSID("test").setPassword("test").setRobotCommunicator(robotCommunicator);
            builder.setToken(token).setLanguage(Language.ENGLISH);
            Project project = builder.build();

            ProjectService.executeWorkflow(workflow, project);
            result =
                project.hasSucceeded() || project.getResult() == Key.ROBOT_NOT_CONNECTED
                    ? (programXml.contains(PARTIAL_SUCCESS_DEF) ? Result.PARTIAL_SUCCESS : Result.SUCCESS)
                    : Result.FAILURE;
            reason = String.valueOf(project.getResult());
        } catch ( Exception e ) {
            LOG.error("Executing workflow failed", e);
            result = Result.FAILURE;
            reason = "exception (" + e.getMessage() + ")";
        }
        logResult(resultsWorkflowExecutions, result, robotName, workflow, reason);
        return result;
    }

    /**
     * use the show source workflow to generate a source program from blockly XML
     *
     * @param programName name of the program, not null
     * @param programXml XML of the program, not null
     * @param robotName name of the robot, not null
     * @return a pair of (Result enumeration, the generated source)
     */
    private Pair<Result, String> executeWorkflowShowSource(String programName, String programXml, String robotName) {
        String reason = "?";
        Result result = Result.FAILURE;
        String sourceCode = "";
        logStart(robotName, "workflow", "showsource");
        try {
            Project.Builder builder = UnitTestHelper.setupWithExportXML(pluginMap.get(robotName), programXml);
            builder.setRobot(robotName).setProgramName("NEPOprog").setSSID("test").setPassword("test").setLanguage(Language.ENGLISH);
            Project showSourceProject = builder.build();

            // Every robot needs at least a show source workflow
            ProjectService.executeWorkflow("showsource", showSourceProject);
            sourceCode = showSourceProject.getSourceCode().toString();
            result = showSourceProject.hasSucceeded() ? (programXml.contains(PARTIAL_SUCCESS_DEF) ? Result.PARTIAL_SUCCESS : Result.SUCCESS) : Result.FAILURE;
            reason = String.valueOf(showSourceProject.getResult());
        } catch ( Exception e ) {
            LOG.error("Executing workflow failed", e);
            result = Result.FAILURE;
            reason = "exception (" + e.getMessage() + ")";
        }
        logResult(resultsWorkflowExecutions, result, robotName, "showsource", reason);
        return Pair.of(result, sourceCode);
    }

    /**
     * setup the parameter for the REST service, which remembers a (new) robot selection of the user, and then call the REST service.<br>
     * If the set fails, an exception is thrown
     *
     * @param robotName ...
     */
    private void setRobotTo(String robotName) throws Exception {
        Response response = this.restAdmin.setRobot(JSONUtilForServer.mkFRR("{'cmd':'setRobot','robot':'" + robotName + "'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    /**
     * log the start of an activity
     *
     * @param robotName ...
     * @param activity name of the logging activity
     * @param programName ...
     */
    private void logStart(String robotName, String activity, String programName) {
        String format = "[[[[[[[[[[ robot: %-15s %-15s: %s";
        String msg = String.format(format, robotName, activity, programName);
        LOG.info(msg);
    }

    private static void logSummary() {
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX COMMON-IT succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one test of COMMON-IT FAILED XXXXXXXXXX");
            fail();
        }
    }

    /**
     * remember the result of a cross compilation or workflow execution
     *
     * @param resultList the result is appended here
     * @param result of a cross compilation, never null
     * @param robotName name of robot for which the crosscompiler was called, never null
     * @param progName name of prog that was crosscompiled, never null
     * @param reason reason of failure, may be null
     */
    private void logResult(List<String> resultList, Result result, String robotName, String progName, String reason) {
        String format;
        if ( result == Result.SUCCESS ) {
            format = "++++++++++ Robot: %-15s success for: %s";
        } else {
            format = "---------- Robot: %-15s FAILED (" + reason + ") for: %s";
            logProgram(httpSessionState.getToken());
        }
        LOG.info(String.format(format, robotName, progName));
        LOG.info("]]]]]]]]]]");
        if ( result == Result.SUCCESS ) {
            if ( showSuccess ) {
                resultList.add(String.format("succ; %-15s; %-60s;", robotName, progName));
            }
        } else {
            resultList.add(String.format("fail; %-15s; %-60s;", robotName, progName));
        }
    }

    /**
     * logs generated programs, when called. Generated programs (usually one :-) are written into a sub-directory of a directory, whose name is the token
     * string, that in turn identifies a robot connected to the lab. Thus the parameter of this method is called "token". All program are logged.
     *
     * @param token the name of the token directory
     */
    private void logProgram(String token) {
        try {
            String tokenDir = serverProperties.getTempDir() + token;
            Util.fileStreamOfFileDirectory(tokenDir).forEach(f -> logProgramFromTokenDir(tokenDir, f));
        } catch ( Exception e ) {
            LOG.error("could not show the failing program. Probably the generation failed.");
        }
    }

    /**
     * logs generated programs, when called. In a token directory many programs with many source files can be stored (usually there is only one program with
     * only one source file). All sources are logged.
     *
     * @param tokenDir the directory containing all programs generated for this token
     * @param dir the program name, that is used as directory name for all artefacts, that belong to the program.
     */
    private static void logProgramFromTokenDir(String tokenDir, String programNameUsedAsDirectoryName) {
        String sourceDir = tokenDir + "/" + programNameUsedAsDirectoryName + "/source";
        Util.fileStreamOfFileDirectory(sourceDir).forEach(f -> logProgramFromSourceDir(sourceDir, f));
    }

    /**
     * logs generated programs, when called. The file from the directory is logged
     *
     * @param sourceDir the directory, in which a source file is stored
     * @param fileName the name of the file, that should be logged.
     */
    private static void logProgramFromSourceDir(String sourceDir, String fileName) {
        String sourceFile = sourceDir + "/" + fileName;
        List<String> sourceLines = Util.readFileLines(fileName);
        StringBuilder sb = new StringBuilder();
        int lineCounter = 1;
        for ( String sourceLine : sourceLines ) {
            sb.append(String.format("%-4d %s", lineCounter++, sourceLine)).append("\n");
        }
        LOG.error("failing source from file: " + sourceFile + " is:\n" + sb.toString());
    }

    private static String getTemplateWithConfigReplaced(String robotDir, String robotName) {
        String template = Util.readResourceContent(RESOURCE_BASE + "template/" + robotDir + ".xml");
        Properties robotProperties = Util.loadProperties("classpath:/" + robotName + ".properties");
        String defaultConfigurationURI = robotProperties.getProperty("robot.configuration.default");
        String defaultConfig = Util.readResourceContent(defaultConfigurationURI);
        final String templateWithConfig = template.replaceAll("\\[\\[conf\\]\\]", defaultConfig);
        return templateWithConfig;
    }

    /**
     * generate from a template a final program by replacing the parameters for the<br>
     * - the program [[prog]]<br>
     * - the variable declarations [[decl]]<br>
     * - the function declarations [[fragment]]<br>
     * by data retrieved from the test specification
     *
     * @param template ...
     * @param progName to retrieve the program "kernel" by reading a resource file
     * @param progDeclFromTestSpec specification for variables and functions
     * @return the final program
     */
    private static String generateFinalProgram(String template, String progName, JSONObject progDeclFromTestSpec) {
        String progSource = read("prog", progName + ".xml");
        Assert.assertNotNull(progSource, "program not found: " + progName);
        template = template.replaceAll("\\[\\[prog\\]\\]", progSource);
        String progFragmentName = progDeclFromTestSpec.optString("fragment");
        String progFragment = progFragmentName == null ? "" : read("fragment", progFragmentName + ".xml");
        template = template.replaceAll("\\[\\[fragment\\]\\]", progFragment == null ? "" : progFragment);
        String declName = progDeclFromTestSpec.optString("decl");
        Assert.assertNotNull(declName, "decl for program not found: " + progName);
        String decl = read("decl", declName + ".xml");
        template = template.replaceAll("\\[\\[decl\\]\\]", decl);
        return template;
    }

    /**
     * given a response object, that contains a JSON entity with the property "rc" (return code"), check if the value is as expected
     *
     * @param response the JSON object to check
     * @param rc the return code expected
     * @param acceptableErrorCodes the codes, that are acceptable, if the rc is equal "error". In this case the test passes.
     * @return true, if result is as expected, false otherwise
     */
    private static Result checkEntityRc(Response response, String rc, String... acceptableErrorCodes) {
        de.fhg.iais.roberta.util.dbc.Assert.nonEmptyString(rc);
        JSONObject entity = new JSONObject((String) response.getEntity());
        String returnCode = entity.optString("rc", "");
        if ( rc.equals(returnCode) ) {
            return Result.SUCCESS;
        } else if ( rc.equals("error") ) {
            String errorCode = entity.optString("cause", "");
            for ( String acceptableErrorCode : acceptableErrorCodes ) {
                if ( errorCode.equals(acceptableErrorCode) ) {
                    return Result.SUCCESS;
                }
            }
            return Result.FAILURE;
        } else {
            return Result.FAILURE;
        }
    }

    private static String read(String directoryName, String progNameWithXmlSuffix) {
        try {
            return Util.readResourceContent(RESOURCE_BASE + directoryName + "/" + progNameWithXmlSuffix);
        } catch ( Exception e ) {
            // this happens, if no decl or fragment is available for the program given. This is legal.
            return null;
        }
    }

    private static enum Result {
        SUCCESS, // everything worked as expected
        PARTIAL_SUCCESS, // everything worked, but the program contains disabled blocks
        FAILURE // the workflow failed
    }

}