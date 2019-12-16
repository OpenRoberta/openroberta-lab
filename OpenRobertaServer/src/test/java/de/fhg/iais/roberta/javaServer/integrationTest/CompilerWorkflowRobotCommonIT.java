package de.fhg.iais.roberta.javaServer.integrationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ProjectWorkflowRestController;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * <b>Testing the generation of native code and the CROSSCOMPILER</b><br>
 * <br>
 * The tests in this class are integration tests. The front end is <b>not</b> tested. The tests deliver programs (either NEPO programs encoded in XML or native
 * programs encoded as expected by the crosscompilers) to the various crosscompilers and check whether the expected response is returned ("ok", "error").<br>
 * <br>
 * In src/test/crossCompilerTests/common the directory "template" contains a XML templates for each robot. Default configuration for each robot is taken from
 * each robot plugin. The directory "prog" contains program fragment to be inserted into all templates together with matching configurations.<br>
 * <br>
 * Enjoy
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowRobotCommonIT {
    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflowRobotCommonIT.class);
    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final String RESOURCE_BASE = "/crossCompilerTests/common/";
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');

    private static RobotCommunicator robotCommunicator;
    private static ServerProperties serverProperties;
    private static HttpSessionState httpSessionState;
    private static boolean crosscompilerCall;
    private static boolean showSuccess;

    private static JSONObject robotsFromTestSpec;
    private static JSONObject progsFromTestSpec;

    private static List<String> results = new ArrayList<>();

    @Mock
    private DbSession dbSession;
    @Mock
    private SessionFactoryWrapper sessionFactoryWrapper;

    private ProjectWorkflowRestController restWorkflow;
    private ClientAdmin restAdmin;

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
        Map<String, IRobotFactory> pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
        httpSessionState = HttpSessionState.initOnlyLegalForDebugging("", pluginMap, serverProperties, 1);
        JSONObject testSpecification = Util.loadYAML("classpath:/crossCompilerTests/common/testSpec.yml");
        crosscompilerCall = testSpecification.getBoolean("crosscompilercall");
        showSuccess = testSpecification.getBoolean("showsuccess");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");
        progsFromTestSpec = testSpecification.getJSONObject("progs");
        Set<String> robots = robotsFromTestSpec.keySet();
        Set<String> programs = progsFromTestSpec.keySet();
        StringBuilder sb = new StringBuilder();
        sb.append("\nXXXXXXXXXX from ").append(robots.size()).append(" robots and ").append(programs.size()).append(" program we generate ");
        sb.append(robots.size() * programs.size()).append(" programs. XXXXXXXXXX\nRobots are:\n    ");
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
        LOG.info("XXXXXXXXXX results of common compilations XXXXXXXXXX");
        for ( String result : results ) {
            LOG.info(result);
        }
    }

    @Before
    public void setup() throws Exception {
        this.restWorkflow = new ProjectWorkflowRestController(robotCommunicator);
        this.restAdmin = new ClientAdmin(robotCommunicator, serverProperties);
        when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        doNothing().when(this.dbSession).commit();
    }

    @Test
    public void testCommonPartOfNepo() throws Exception {
        LOG.info("XXXXXXXXXX START of the NEPO common compilations XXXXXXXXXX");
        boolean resultAcc = true;
        try {
            for ( String robotName : robotsFromTestSpec.keySet() ) {
                JSONObject robot = robotsFromTestSpec.getJSONObject(robotName);
                String robotDir = robot.getString("template");
                String templateWithConfig = getTemplateWithConfigReplaced(robotDir, robotName);
                nextProg: for ( String progName : progsFromTestSpec.keySet() ) {
                    JSONObject prog = progsFromTestSpec.getJSONObject(progName);
                    JSONObject exclude = prog.optJSONObject("exclude");
                    if ( exclude != null ) {
                        for ( String excludeRobot : exclude.keySet() ) {
                            if ( excludeRobot.equals(robotName) || excludeRobot.equals("ALL") ) {
                                LOG.info("########## for " + robotName + " prog " + progName + " is excluded. Reason: " + exclude.getString(excludeRobot));
                                continue nextProg;
                            }
                        }
                    }
                    boolean resultNext = generateAndCompileProgram(robotName, templateWithConfig, progName, prog);
                    resultAcc = resultAcc && resultNext;
                }
            }
        } catch ( Exception e ) {
            LOG.error("XXXXXXXXXX test terminated with an unexpected exception XXXXXXXXXX", e);
            resultAcc = false;
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all of the NEPO common compilations succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the NEPO common compilations FAILED XXXXXXXXXX");
            fail();
        }
    }

    /**
     * generate and compile a program for different robots. May help testing ...<br>
     * <br>
     * - supply the program name<br>
     * - supply the list of robots (you may copy from the console output) - decide whether to generate only or to generate and compile
     */
    @Ignore
    @Test
    public void testGenerateAndCompileSomeGeneratedPrograms() {
        String progName = "controlFlowDecisons";
        Collection<String> robots = Arrays.asList("wedo");
        boolean generateOnly = true;
        for ( String robotName : robots ) {
            LOG.info("********** process program " + progName + " for robot " + robotName + " **********");
            String robotDir = robotsFromTestSpec.getJSONObject(robotName).getString("template");
            final String templateWithConfig = getTemplateWithConfigReplaced(robotDir, robotName);
            if ( generateOnly ) {
                final String generatedProgram = generateFinalProgram(templateWithConfig, progName, progsFromTestSpec.getJSONObject(progName));
                LOG.info("********** program " + progName + " for robot " + robotName + " **********:\n" + generatedProgram);
            } else {
                generateAndCompileProgram(robotName, templateWithConfig, progName, progsFromTestSpec.getJSONObject(progName));
            }
        }
    }

    private boolean generateAndCompileProgram(String robotName, String template, String progName, JSONObject prog) throws DbcException {
        String reason = "?";
        boolean result = false;
        logStart(robotName, progName);
        try {
            String token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
            httpSessionState.setToken(token);
            template = generateFinalProgram(template, progName, prog);
            if ( crosscompilerCall ) {
                setRobotTo(robotName);
                org.codehaus.jettison.json.JSONObject cmd = JSONUtilForServer.mkD("{'programName':'prog','language':'de'}");
                cmd.getJSONObject("data").put("programBlockSet", template);
                Response response = this.restWorkflow.compileProgram(cmd);
                result = checkEntityRc(response, "ok", "PROGRAM_INVALID_STATEMETNS");
                reason = "response-info";
            } else {
                result = true;
                reason = "crosscompiler not called";
            }
        } catch ( Exception e ) {
            LOG.error("ProjectWorkflowRestController failed", e);
            result = false;
            reason = "exception (" + e.getMessage() + ")";
        }
        log(result, robotName, progName, reason);
        return result;
    }

    private void setRobotTo(String robot) throws Exception {
        Response response = this.restAdmin.command(this.dbSession, JSONUtilForServer.mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    private void logStart(String name, String fullResource) {
        String format = "[[[[[[[[[[ Robot: %-15s compile file: %s";
        String msg = String.format(format, name, fullResource);
        LOG.info(msg);
    }

    private void log(boolean result, String name, String fullResource, String reason) {
        String format;
        if ( result ) {
            format = "++++++++++ Robot: %-15s succeeded compile file: %s";
        } else {
            format = "---------- Robot: %-15s FAILED (" + reason + ") compile file: %s";
            showFailingProgram(httpSessionState.getToken());
        }
        LOG.info(String.format(format, name, fullResource));
        LOG.info("]]]]]]]]]]");
        if ( result ) {
            if ( showSuccess ) {
                results.add(String.format("succ; %-15s; %-60s;", name, fullResource));
            }
        } else {
            results.add(String.format("fail; %-15s; %-60s;", name, fullResource));
        }
    }

    private static String getTemplateWithConfigReplaced(String robotDir, String robotName) {
        String template = Util.readResourceContent(RESOURCE_BASE + "template/" + robotDir + ".xml");
        Properties robotProperties = Util.loadProperties("classpath:/" + robotName + ".properties");
        String defaultConfigurationURI = robotProperties.getProperty("robot.configuration.default");
        String defaultConfig = Util.readResourceContent(defaultConfigurationURI);
        final String templateWithConfig = template.replaceAll("\\[\\[conf\\]\\]", defaultConfig);
        return templateWithConfig;
    }

    private static String generateFinalProgram(String template, String progName, JSONObject prog) {
        String progSource = read("prog", progName + ".xml");
        Assert.assertNotNull(progSource, "program not found: " + progName);
        template = template.replaceAll("\\[\\[prog\\]\\]", progSource);
        String progFragmentName = prog.optString("fragment");
        String progFragment = progFragmentName == null ? "" : read("fragment", progFragmentName + ".xml");
        template = template.replaceAll("\\[\\[fragment\\]\\]", progFragment == null ? "" : progFragment);
        String declName = prog.optString("decl");
        Assert.assertNotNull(declName, "decl for program not found: " + progName);
        String decl = read("decl", declName + ".xml");
        template = template.replaceAll("\\[\\[decl\\]\\]", decl);
        return template;
    }

    private static String read(String directoryName, String progNameWithXmlSuffix) {
        try {
            return Util.readResourceContent(RESOURCE_BASE + directoryName + "/" + progNameWithXmlSuffix);
        } catch ( Exception e ) {
            // this happens, if no decl or fragment is available for the program given. This is legel.
            return null;
        }
    }

    /**
     * given a response object, that contains a JSON entity with the property "rc" (return code"), check if the value is as expected
     *
     * @param response the JSON object to check
     * @param rc the return code expected
     * @param acceptableErrorCodes the codes, that are acceptable, if the rc is equal "error". In this case the test passes.
     * @return true, if result is as expected, false otherwise
     */
    private static boolean checkEntityRc(Response response, String rc, String... acceptableErrorCodes) {
        de.fhg.iais.roberta.util.dbc.Assert.nonEmptyString(rc);
        org.codehaus.jettison.json.JSONObject entity = (org.codehaus.jettison.json.JSONObject) response.getEntity();
        String returnCode = entity.optString("rc", "");
        if ( rc.equals(returnCode) ) {
            return true;
        } else if ( rc.equals("error") ) {
            String errorCode = entity.optString("cause", "");
            for ( String acceptableErrorCode : acceptableErrorCodes ) {
                if ( errorCode.equals(acceptableErrorCode) ) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
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