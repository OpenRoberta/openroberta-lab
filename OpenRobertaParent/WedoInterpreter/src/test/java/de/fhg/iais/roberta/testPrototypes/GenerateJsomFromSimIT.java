package de.fhg.iais.roberta.testPrototypes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>Testing the generation of suimulator code</b><br>
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class GenerateJsomFromSimIT {
    private static final Logger LOG = LoggerFactory.getLogger(GenerateJsomFromSimIT.class);
    private static final String TEST_BASE = "simulatorTests/";

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

    @Before
    public void setup() throws Exception {
        robertaProperties = new RobertaProperties(Util1.loadProperties("classpath:wedoOpenRoberta.properties"));
        // TODO: add a robotBasedir property to the openRoberta.properties. Make all pathes relative to that dir. Create special accessors. Change String to Path.
        fixPropertyPathes();
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, robertaProperties);
        httpSessionState = HttpSessionState.init(robotCommunicator, pluginMap, robertaProperties, 1);
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, robotCommunicator, robertaProperties);
        this.restAdmin = new ClientAdmin(robotCommunicator, robertaProperties);
        when(sessionFactoryWrapper.getSession()).thenReturn(dbSession);
        doNothing().when(dbSession).commit();
    }

    @Test
    public void testNepo() throws Exception {
        String base = "threeFors";
        String robot = "ev3lejosv1";
        String fullResource = TEST_BASE + base + ".xml";
        LOG.info("robot: " + robot + ", xml: " + fullResource);
        setRobotTo(robot);
        JSONObject cmd = mkD("{'cmd':'runPsim','name':'prog','language':'de'}");
        cmd.getJSONObject("data").put("programText", Util1.readFileContent(fullResource));
        response = this.restProgram.command(httpSessionState, cmd);
        JSONObject entity = (JSONObject) response.getEntity();
        assertEquals("ok", entity.optString("rc", ""));
        String javaScriptProgram = entity.getString("javaScriptProgram");
        Util1.writeFile(TEST_BASE + base + ".sim", javaScriptProgram);
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        response = this.restAdmin.command(httpSessionState, dbSession, mkD("{'cmd':'setRobot','robot':'" + robot + "'}"));
        assertEntityRc(this.response, "ok", Key.ROBOT_SET_SUCCESS);
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
     * see {@link JSONUtilForServer}
     */
    public static JSONObject mkD(String s) throws JSONException {
        return new JSONObject("{'data':" + s + ",'log':[]}".replaceAll("'", "\""));
    }

    /**
     * see {@link JSONUtilForServer}
     */
    public static void assertEntityRc(Response response, String rc, Key message) throws JSONException {
        JSONObject entity = (JSONObject) response.getEntity();
        Assert.assertEquals(rc, entity.getString("rc"));
        String responseKey = entity.optString("message");
        Assert.assertEquals(message.getKey(), responseKey);
    }

}