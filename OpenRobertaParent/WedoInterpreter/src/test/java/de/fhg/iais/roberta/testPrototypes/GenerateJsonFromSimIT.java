package de.fhg.iais.roberta.testPrototypes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>Testing the generation of simulator code</b><br>
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class GenerateJsonFromSimIT {
    private static final Logger LOG = LoggerFactory.getLogger(GenerateJsonFromSimIT.class);
    private static final String TEST_BASE = "simulatorTests/";
    private static final String ROBOT = "wedo";

    private static final String[] NAME_OF_TESTS = {
    	"show-add",
        "if-then-else",
        //"wait-if",
        "assign-add-2",
        "assign-add",
        "simple",
        "threeFors",
        "while-assign",
        "motor",
        "motor-null",
        "motor-stop",
        "LED",
        "LED-off",
        "play-note",
        "play-tone"
    };

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
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, robertaProperties);
        httpSessionState = HttpSessionState.init(robotCommunicator, pluginMap, robertaProperties, 1);
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, robotCommunicator, robertaProperties);
        this.restAdmin = new ClientAdmin(robotCommunicator, robertaProperties);
        when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        doNothing().when(this.dbSession).commit();
    }

    @Test
    public void testNepo() throws Exception {
        setRobotTo(ROBOT);
        Arrays.stream(NAME_OF_TESTS).forEach(s -> runNepo(s));
    }

    public void runNepo(String nameOfTest) {
        try {
            String fullResource = TEST_BASE + nameOfTest + ".xml";
            LOG.info("robot: " + ROBOT + ", xml: " + fullResource);
            JSONObject cmd = mkD("{'cmd':'runPsim','name':'prog','language':'de'}");
            cmd.getJSONObject("data").put("programText", Util1.readFileContent(fullResource));
            this.response = this.restProgram.command(httpSessionState, cmd);
            JSONObject entity = (JSONObject) this.response.getEntity();
            assertEquals("ok", entity.optString("rc", ""));
            String javaScriptProgram = entity.getString("javaScriptProgram");
            Util1.writeFile(TEST_BASE + nameOfTest + ".json", javaScriptProgram);
        } catch ( Exception e ) {
            throw new DbcException("Test failed for " + nameOfTest, e);
        }
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        this.response = this.restAdmin.command(httpSessionState, this.dbSession, mkD("{'cmd':'setRobot','robot':'" + robot + "'}"));
        assertEntityRc(this.response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    /**
     * see {JSONUtilForServer}
     */
    public static JSONObject mkD(String s) throws JSONException {
        return new JSONObject("{'data':" + s + ",'log':[]}".replaceAll("'", "\""));
    }

    /**
     * copy from JSONUtilForServer
     */
    public static void assertEntityRc(Response response, String rc, Key message) throws JSONException {
        JSONObject entity = (JSONObject) response.getEntity();
        Assert.assertEquals(rc, entity.getString("rc"));
        String responseKey = entity.optString("message");
        Assert.assertEquals(message.getKey(), responseKey);
    }

}