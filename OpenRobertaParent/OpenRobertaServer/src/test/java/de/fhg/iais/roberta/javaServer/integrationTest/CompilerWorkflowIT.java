package de.fhg.iais.roberta.javaServer.integrationTest;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>Testing the generation of native code and the CROSSCOMPILER</b><br>
 * <br>
 * The tests in this class are integration tests. The front end is <b>not</b> tested. The tests deliver programs (either NEPO programs encoded in XML or native
 * programs encoded as expected by the crosscompilers) to the various crosscompilers and check whether the expected response is returned ("ok", "error").<br>
 * <br>
 * In src/test/compilerWorkflowTest for each robot "x" a directory with name "x" is expected. This directory contains all tests for this robot. The helper
 * methods "compileNepo" and "compileNative" are used to execute one test.<br>
 * <br>
 * TODO: add tests for generating simulation programs
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowIT {
    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflowIT.class);
    private static final boolean SHOW_SUCCESS = false;

    private String resourceBase;
    private static final List<String> results = new ArrayList<>();

    private static RobotCommunicator robotCommunicator;
    private static RobertaProperties robertaProperties;
    private static Map<String, IRobotFactory> pluginMap;
    private static HttpSessionState httpSessionState;

    @Mock
    private DbSession dbSession;
    @Mock
    private SessionFactoryWrapper sessionFactoryWrapper;

    private Response response; // store a REST response here

    private ClientProgram restProgram;
    private ClientAdmin restAdmin;

    @BeforeClass
    public static void setupClass() throws Exception {
        robertaProperties = new RobertaProperties(Util1.loadProperties(null));
        // TODO: add a robotBasedir property to the openRoberta.properties. Make all pathes relative to that dir. Create special accessors. Change String to Path.
        fixPropertyPathes();
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, robertaProperties);
        httpSessionState = HttpSessionState.init(robotCommunicator, pluginMap, robertaProperties, 1);
    }

    @AfterClass
    public static void printResults() {
        LOG.info("XXXXXXXXXX results of NEPO and NATIVE compilations XXXXXXXXXX");
        for ( String result : results ) {
            LOG.info(result);
        }

    }

    @Before
    public void setup() throws Exception {
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, robotCommunicator, robertaProperties);
        this.restAdmin = new ClientAdmin(robotCommunicator, robertaProperties);
        when(sessionFactoryWrapper.getSession()).thenReturn(dbSession);
        doNothing().when(dbSession).commit();
    }

    @Test
    public void testPlugins() throws Exception {
        Set<String> foundPlugins = pluginMap.keySet();
        for ( RobotInfo info : RobotInfo.values() ) {
            if ( !foundPlugins.contains(info.name()) ) {
                Assert.fail("Plugin not found: " + info.name());
            }
        }
    }

    @Test
    public void testNepo() throws Exception {
        resourceBase = "/crossCompilerTests/";
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NEPO compilations XXXXXXXXXX");
        for ( RobotInfo info : RobotInfo.values() ) {
            String resourceDirectory = resourceBase + info.dirName;
            resultAcc = resultAcc & Util1.fileStreamOfResourceDirectory(resourceDirectory).//
                filter(f -> f.endsWith(".xml")).map(f -> compileNepo(info, f)).reduce(true, (a, b) -> a && b);
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
        resourceBase = "/crossCompilerTests/";
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NATIVE compilations XXXXXXXXXX");
        for ( RobotInfo info : RobotInfo.values() ) {
            String resourceDirectory = resourceBase + "/" + info.dirName;
            resultAcc = resultAcc & Util1.fileStreamOfResourceDirectory(resourceDirectory). //
                filter(f -> f.endsWith(info.suffixNative)).map(f -> compileNative(info, f)).reduce(true, (a, b) -> a && b);
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all of the NATIVE compilations succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the NATIVE compilations FAILED XXXXXXXXXX");
            fail();
        }
    }

    private boolean compileNepo(RobotInfo info, String resource) throws DbcException {
        String expectResult = resource.startsWith("error") ? "error" : "ok";
        String fullResource = resourceBase + info.dirName + "/" + resource;
        try {
            log("start", info.name(), fullResource, null);
            setRobotTo(info.name());
            JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileP','name':'prog','language':'de'}");
            cmd.getJSONObject("data").put("program", Util1.readResourceContent(fullResource));
            response = this.restProgram.command(httpSessionState, cmd);
        } catch ( Exception e ) {
            log("fail", info.name(), fullResource, e);
            return false;
        }
        boolean result = checkEntityRc(this.response, expectResult, "PROGRAM_INVALID_STATEMETNS");
        if ( result ) {
            log("success", info.name(), fullResource, null);
        } else {
            log("fail", info.name(), fullResource, null);
        }
        return result;
    }

    private boolean compileNative(RobotInfo info, String resource) throws DbcException {
        String expectResult = resource.startsWith("error") ? "error" : "ok";
        resource = resource.endsWith(info.suffixNative) ? resource.substring(0, resource.length() - info.suffixNative.length()) : resource;
        String fullResource = resourceBase + info.dirName + "/" + resource + info.suffixNative;
        try {
            log("start", info.name(), fullResource, null);
            setRobotTo(info.name());
            JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileN','name':'" + resource + "','language':'de'}");
            String fileContent = Util1.readResourceContent(fullResource);
            cmd.getJSONObject("data").put("programText", fileContent);
            response = this.restProgram.command(httpSessionState, cmd);
        } catch ( Exception e ) {
            log("fail", info.name(), fullResource, e);
            return false;
        }
        boolean result = checkEntityRc(this.response, expectResult);
        if ( result ) {
            log("success", info.name(), fullResource, null);
        } else {
            log("fail", info.name(), fullResource, null);
        }
        return result;
    }

    private void log(String level, String name, String fullResource, Throwable thw) {
        String cause = thw == null ? "response-info" : "exception";
        String format;
        if ( level.equals("start") ) {
            format = "[[[[[ =============== Robot: %-150s compile file: %-60s ===============";
        } else if ( level.equals("success") ) {
            format = "      +++++++++++++++ Robot: %-15s compile file: %-60s succeeded =============== ]]]]]";
        } else if ( level.equals("fail") ) {
            format = "      --------------- Robot: %-15s compile file: %-60s FAILED(" + cause + ") =============== ]]]]]";
        } else {
            throw new DbcException("invalid level: " + level);
        }
        String msg = String.format(format, name, fullResource);
        if ( level.equals("fail") ) {
            LOG.error(msg);
        } else {
            LOG.info(msg);
        }
        if ( level.equals("fail") ) {
            results.add(String.format("fail; %-15s; %-60s;", name, fullResource));
        } else if ( SHOW_SUCCESS && level.equals("success") ) {
            results.add(String.format("succ; %-15s; %-60s;", name, fullResource));
        }
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        response = this.restAdmin.command(httpSessionState, dbSession, JSONUtilForServer.mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        JSONUtilForServer.assertEntityRc(this.response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    private static void fixPropertyPathes() {
        for ( int i = 0; i < 999; i++ ) {
            replace("robot.plugin." + i + ".compiler.resources.dir");
            replace("robot.plugin." + i + ".updateResources.dir");
            replace("robot.plugin." + i + ".generated.programs.build.xml");
        }
    }

    private static void replace(String key) {
        String path = robertaProperties.getStringProperty(key);
        if ( path != null ) {
            path = path.replaceFirst("OpenRobertaParent", "..");
            robertaProperties.getRobertaProperties().put(key, path);
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
        JSONObject entity = (JSONObject) response.getEntity();
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

    private static enum RobotInfo {
        ev3lejosv1( "ev3", ".java" ),
        ev3lejos( "ev3", ".java" ),
        ev3dev( "ev3", ".py" ),
        nxt( "nxt", ".nxc" ),
        microbit( "microbit", ".py" ),
        ardu( "ardu", ".ino" ),
        nao( "nao", ".py" ),
        bob3( "bob3", ".ino" ),
        calliope2017( "calliope", ".cpp" ),
        calliope2016( "calliope", ".cpp" );

        public final String dirName;
        public final String suffixNative;

        private RobotInfo(String dirName, String suffixNative) {
            this.dirName = dirName;
            this.suffixNative = suffixNative;
        }
    }
}