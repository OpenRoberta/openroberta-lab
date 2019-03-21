package de.fhg.iais.roberta.javaServer.integrationTest;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
 * In src/test/crossCompilerTests/robotSpecific for each robot "x" a directory with name "x" is expected. This directory contains all tests for this robot. The
 * helper methods "compileNepo" and "compileNative" are used to execute tests.<br>
 * <br>
 * TODO: add tests for generating simulation programs
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowRobotSpecificIT {
    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflowRobotSpecificIT.class);

    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final boolean CROSSCOMPILER_CALL = true;
    private static final boolean SHOW_SUCCESS = true;

    private JSONObject robots;
    private String resourceBase;

    private final List<String> results = new ArrayList<>();

    private RobotCommunicator robotCommunicator;
    private ServerProperties serverProperties;
    private Map<String, IRobotFactory> pluginMap;
    private HttpSessionState httpSessionState;

    @Mock
    private DbSession dbSession;
    @Mock
    private SessionFactoryWrapper sessionFactoryWrapper;

    private ClientProgram restProgram;
    private ClientAdmin restAdmin;

    @Before
    public void setup() throws Exception {
        Properties baseServerProperties = Util1.loadProperties(null);
        baseServerProperties.put("plugin.resourcedir", "..");
        serverProperties = new ServerProperties(baseServerProperties);
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
        httpSessionState = HttpSessionState.init(robotCommunicator, pluginMap, serverProperties, 1);

        resourceBase = "/crossCompilerTests/robotSpecific/";
        JSONObject testSpecification = Util1.loadYAML("classpath:" + resourceBase + "testSpec.yml");
        robots = testSpecification.getJSONObject("robots");

        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, robotCommunicator, serverProperties);
        this.restAdmin = new ClientAdmin(robotCommunicator, serverProperties);
        when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        doNothing().when(this.dbSession).commit();
    }

    @After
    public void tearDownAndPrintResults() {
        LOG.info("XXXXXXXXXX results of NEPO and NATIVE compilations XXXXXXXXXX");
        for ( String result : results ) {
            LOG.info(result);
        }
    }

    @Test
    public void testPlugins() throws Exception {
        Set<String> foundPlugins = pluginMap.keySet();
        for ( String robot : robots.keySet() ) {
            if ( !foundPlugins.contains(robot) ) {
                Assert.fail("Plugin not found: " + robot);
            }
        }
    }

    @Test
    public void testNepo() throws Exception {
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NEPO compilations XXXXXXXXXX");
        for ( final String robotName : robots.keySet() ) {
            final String robotDir = robots.getJSONObject(robotName).getString("dir");
            final String resourceDirectory = resourceBase + robotDir;
            Boolean resultNext = Util1.fileStreamOfResourceDirectory(resourceDirectory).//
                filter(f -> f.endsWith(".xml")).map(f -> compileNepo(robotName, robotDir, f)).reduce(true, (a, b) -> a && b);
            resultAcc = resultAcc && resultNext;
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all of the NEPO compilations succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the NEPO compilations FAILED XXXXXXXXXX");
            fail();
        }
    }

    @Test
    public void testNative() throws Exception {
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NATIVE compilations XXXXXXXXXX");
        for ( final String robotName : robots.keySet() ) {
            final JSONObject robot = robots.getJSONObject(robotName);
            final String resourceDirectory = resourceBase + "/" + robot.getString("dir");
            Boolean resultNext = Util1.fileStreamOfResourceDirectory(resourceDirectory). //
                map(f -> compileNative(robotName, robot, f)).reduce(true, (a, b) -> a && b);
            resultAcc = resultAcc && resultNext;
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all of the NATIVE compilations succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the NATIVE compilations FAILED XXXXXXXXXX");
            fail();
        }
    }

    private boolean compileNepo(String robotName, String robotDir, String resource) {
        httpSessionState.setToken(RandomUrlPostfix.generate(12, 12, 3, 3, 3));
        String expectResult = resource.startsWith("error") ? "error" : "ok";
        String fullResource = resourceBase + robotDir + "/" + resource;
        try {
            logStart(robotName, fullResource);
            setRobotTo(robotName);
            boolean result = false;
            if ( CROSSCOMPILER_CALL ) {
                org.codehaus.jettison.json.JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileP','name':'prog','language':'de'}");
                cmd.getJSONObject("data").put("program", Util1.readResourceContent(fullResource));
                Response response = this.restProgram.command(httpSessionState, cmd);
                result = checkEntityRc(response, expectResult, "PROGRAM_INVALID_STATEMETNS");
            } else {
                result = true;
            }
            log(result, robotName, fullResource, null);
            return result;
        } catch ( Exception e ) {
            log(false, robotName, fullResource, e);
            return false;
        }
    }

    private boolean compileNative(String robotName, JSONObject robot, String resource) throws DbcException {
        final String suffix = robot.getString("suffix");
        if ( !resource.endsWith(suffix) ) {
            return true;
        }
        String expectResult = resource.startsWith("error") ? "error" : "ok";
        resource = resource.substring(0, resource.length() - suffix.length());
        String fullResource = resourceBase + robot.getString("dir") + "/" + resource + suffix;
        boolean result = false;
        Exception exc = null;
        try {
            logStart(robotName, fullResource);
            setRobotTo(robotName);
            if ( CROSSCOMPILER_CALL ) {
                org.codehaus.jettison.json.JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileN','name':'" + resource + "','language':'de'}");
                String fileContent = Util1.readResourceContent(fullResource);
                cmd.getJSONObject("data").put("programText", fileContent);
                Response response = this.restProgram.command(httpSessionState, cmd);
                result = checkEntityRc(response, expectResult);
            } else {
                result = true;
            }
        } catch ( Exception e ) {
            result = false;
            exc = e;
        }
        log(result, robotName, fullResource, exc);
        return result;
    }

    private void logStart(String name, String fullResource) {
        String format = "[[[[[[[[[[ Robot: %-15s compile file: %s";
        String msg = String.format(format, name, fullResource);
        LOG.info(msg);
    }

    private void log(boolean result, String name, String fullResource, Throwable thw) {
        String cause = thw == null ? "response-info" : "exception: ";
        String format;
        if ( result ) {
            format = "++++++++++ Robot: %-15s succeeded compile file: %s";
        } else {
            format = "---------- Robot: %-15s FAILED (" + cause + ") compile file: %s";
        }
        LOG.info(String.format(format, name, fullResource));
        LOG.info("]]]]]]]]]]");
        if ( result ) {
            if ( SHOW_SUCCESS ) {
                results.add(String.format("succ; %-15s; %-60s;", name, fullResource));
            }
        } else {
            results.add(String.format("fail; %-15s; %-60s;", name, fullResource));
        }
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        Response response = this.restAdmin.command(httpSessionState, this.dbSession, JSONUtilForServer.mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.ROBOT_SET_SUCCESS);
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
}