package de.fhg.iais.roberta.javaServer.integrationTest;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import de.fhg.iais.roberta.javaServer.restServices.all.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientProgram;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>Testing the generation of native code and the CROSSCOMPILER</b><br>
 * <br>
 * The tests in this class are integration tests. The front end is <b>not</b> tested. The tests deliver programs (either NEPO programs encoded in XML or native
 * programs encoded as expected by the crosscompilers) to the various crosscompilers and check whether the expected response is returned ("ok", "error").<br>
 * <br>
 * In src/test/crossCompilerTests/common the directory "template" contains a XML templates for each robot. The directory "conf" contains a configuration for
 * each robot. The directory "prog" contains program fragment to be inserted into all templates together with matching configurations.<br>
 * <br>
 * Enjoy
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowCommonIT {
    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflowIT.class);
    private static final boolean CROSSCOMPILER_CALL = true;
    private static final boolean SHOW_SUCCESS = false;
    private static final String RESOURCE_BASE = "/crossCompilerTests/common/";

    private static final String DEFAULT_DECL = Util1.readResourceContent(RESOURCE_BASE + "decl/default.xml");
    private static JSONObject ROBOTS;
    private static JSONObject PROGS;
    private static final List<String> RESULTS = new ArrayList<>();

    private static RobotCommunicator ROBOT_COMMUNICATOR;
    private static ServerProperties SERVER_PROPERTIES;
    private static Map<String, IRobotFactory> PLUGIN_MAP;
    private static HttpSessionState HTTP_SESSION_STATE;

    @Mock
    private DbSession dbSession;
    @Mock
    private SessionFactoryWrapper sessionFactoryWrapper;

    private ClientProgram restProgram;
    private ClientAdmin restAdmin;

    @BeforeClass
    public static void setupClass() throws Exception {
        Properties baseServerProperties = Util1.loadProperties(null);
        baseServerProperties.put("plugin.resourcedir", "..");
        SERVER_PROPERTIES = new ServerProperties(baseServerProperties);
        ROBOT_COMMUNICATOR = new RobotCommunicator();
        PLUGIN_MAP = ServerStarter.configureRobotPlugins(ROBOT_COMMUNICATOR, SERVER_PROPERTIES);
        HTTP_SESSION_STATE = HttpSessionState.init(ROBOT_COMMUNICATOR, PLUGIN_MAP, SERVER_PROPERTIES, 1);
        JSONObject testSpecification = Util1.loadYAML("classpath:/crossCompilerTests/common/testSpec.yml");
        ROBOTS = testSpecification.getJSONObject("robots");
        PROGS = testSpecification.getJSONObject("progs");
    }

    @AfterClass
    public static void printResults() {
        LOG.info("XXXXXXXXXX results of common compilations XXXXXXXXXX");
        for ( String result : RESULTS ) {
            LOG.info(result);
        }

    }

    @Before
    public void setup() throws Exception {
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, ROBOT_COMMUNICATOR, SERVER_PROPERTIES);
        this.restAdmin = new ClientAdmin(ROBOT_COMMUNICATOR, SERVER_PROPERTIES);
        when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        doNothing().when(this.dbSession).commit();
    }

    @Test
    public void testCommonPartOfNepo() throws Exception {
        LOG.info("XXXXXXXXXX START of the NEPO common compilations XXXXXXXXXX");
        boolean resultAcc = true;
        for ( String robotName : ROBOTS.keySet() ) {
            JSONObject robot = ROBOTS.getJSONObject(robotName);
            String robotDir = robot.getString("dir");
            String templateWithConfig = getTemplateWithConfigReplaced(robotDir);
            nextProg: for ( String progName : PROGS.keySet() ) {
                JSONObject prog = PROGS.getJSONObject(progName);
                JSONObject exclude = prog.optJSONObject("exclude");
                if ( exclude != null ) {
                    for ( String excludeRobot : exclude.keySet() ) {
                        if ( excludeRobot.equals(robotName) ) {
                            LOG.info("########## for " + robotName + " prog " + progName + " is excluded. Reason: " + exclude.getString(excludeRobot));
                            continue nextProg; // skip this prog due to exclusion and check the next program
                        }
                    }
                }
                resultAcc = resultAcc && compileAfterProgramGenerated(robotName, templateWithConfig, progName + ".xml");
            }
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all of the NEPO common compilations succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the NEPO common compilations FAILED XXXXXXXXXX");
            fail();
        }
    }

    @Ignore
    @Test
    public void testShowGeneratedProgram() {
        String robot = "calliope2017";
        String robotDir = ROBOTS.getJSONObject(robot).getString("dir");
        String templateName = "mathAndLists.xml";
        final String template = getTemplateWithConfigReplaced(robotDir);
        final String generatedProgram = generateFinalProgram(template, templateName);
        LOG.info("the generated program for robot " + robot + " with template " + templateName + " is:\n" + generatedProgram);
    }

    private boolean compileAfterProgramGenerated(String robotName, String template, String progNameWithXmlSuffix) throws DbcException {
        String reason = "?";
        boolean result = false;
        logStart(robotName, progNameWithXmlSuffix);
        String token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
        HTTP_SESSION_STATE.setToken(token);
        template = generateFinalProgram(template, progNameWithXmlSuffix);
        try {
            LOG.info("########## " + robotName + " - " + progNameWithXmlSuffix);
            if ( CROSSCOMPILER_CALL ) {
                setRobotTo(robotName);
                org.codehaus.jettison.json.JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileP','name':'prog','language':'de'}");
                cmd.getJSONObject("data").put("program", template);
                Response response = this.restProgram.command(HTTP_SESSION_STATE, cmd);
                result = checkEntityRc(response, "ok", "PROGRAM_INVALID_STATEMETNS");
                reason = "response-info";
            } else {
                result = true;
                reason = "crosscomiler not called";
            }
        } catch ( Exception e ) {
            LOG.error("ClientProgram failed", e);
            result = false;
            reason = "exception (" + e.getMessage() + ")";
        }
        log(result, robotName, progNameWithXmlSuffix, reason);
        return result;
    }

    private void setRobotTo(String robot) throws Exception {
        Response response =
            this.restAdmin.command(HTTP_SESSION_STATE, this.dbSession, JSONUtilForServer.mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    private void logStart(String name, String fullResource) {
        String format = "[[[[[ =============== Robot: %-150s compile file: %-60s ===============";
        String msg = String.format(format, name, fullResource);
        LOG.info(msg);
    }

    private void log(boolean result, String name, String fullResource, String reason) {
        String format;
        if ( result ) {
            format = "      +++++++++++++++ Robot: %-15s compile file: %-60s succeeded =============== ]]]]]";
        } else {
            format = "      --------------- Robot: %-15s compile file: %-60s FAILED(" + reason + ") =============== ]]]]]";
            showFailingProgram(HTTP_SESSION_STATE.getToken());
        }
        String msg = String.format(format, name, fullResource);
        if ( result ) {
            LOG.info(msg);
        } else {
            LOG.error(msg);
        }
        if ( result ) {
            if ( SHOW_SUCCESS ) {
                RESULTS.add(String.format("succ; %-15s; %-60s;", name, fullResource));
            }
        } else {
            RESULTS.add(String.format("fail; %-15s; %-60s;", name, fullResource));
        }
    }

    private static String getTemplateWithConfigReplaced(String robotDir) {
        String template = Util1.readResourceContent(RESOURCE_BASE + "template/" + robotDir + ".xml");
        String defaultConfig = Util1.readResourceContent(RESOURCE_BASE + "conf/" + robotDir + ".xml");
        final String templateWithConfig = template.replaceAll("\\[\\[conf\\]\\]", defaultConfig);
        return templateWithConfig;
    }

    private static String generateFinalProgram(String template, String progNameWithXmlSuffix) {
        String prog = read("prog", progNameWithXmlSuffix);
        Assert.assertNotNull(prog, "program not found: " + progNameWithXmlSuffix);
        template = template.replaceAll("\\[\\[prog\\]\\]", prog);
        String progFragment = read("progFragment", progNameWithXmlSuffix);
        template = template.replaceAll("\\[\\[progFragment\\]\\]", progFragment == null ? "" : progFragment);
        String decl = read("decl", progNameWithXmlSuffix);
        template = template.replaceAll("\\[\\[decl\\]\\]", decl == null ? DEFAULT_DECL : decl);
        return template;
    }

    private static String read(String directoryName, String progNameWithXmlSuffix) {
        try {
            return Util1.readResourceContent(RESOURCE_BASE + directoryName + "/" + progNameWithXmlSuffix);
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

    private static void showFailingProgram(String token) {
        try {
            String tokenDir = SERVER_PROPERTIES.getTempDir() + token;
            Util1.fileStreamOfFileDirectory(tokenDir).forEach(f -> showFailingProgramFromTokenDir(tokenDir, f));
        } catch ( Exception e ) {
            LOG.error("could not show the failing program. Probably the generation failed.");
        }
    }

    private static void showFailingProgramFromTokenDir(String tokenDir, String dir) {
        String sourceDir = tokenDir + "/" + dir + "/source";
        Util1.fileStreamOfFileDirectory(sourceDir).forEach(f -> showFailingProgramFromSourceDir(sourceDir, f));
    }

    private static void showFailingProgramFromSourceDir(String sourceDir, String fileName) {
        String sourceFile = sourceDir + "/" + fileName;
        List<String> sourceLines = Util1.readFileLines(fileName);
        StringBuilder sb = new StringBuilder();
        int lineCounter = 1;
        for ( String sourceLine : sourceLines ) {
            sb.append(String.format("%-4d %s", lineCounter++, sourceLine)).append("\n");
        }
        LOG.error("failing source from file: " + sourceFile + " is:\n" + sb.toString());
    }
}