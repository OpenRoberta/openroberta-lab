package de.fhg.iais.roberta.testPrototypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

import de.fhg.iais.roberta.blockly.generated.Export;
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
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
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
    private static final String TEST_BASE = "xmlTests/";
    private static final String ROBOT = "wedo";

    private static final String[] NAME_OF_TESTS =
        {
            "x-fac"
        //            "sensoren",
        //            "aktoren",
        //            "fac",
        //            "control_if_elseif",
        //            "control_if_else",
        //            "control_repeat_indefinetly",
        //            "control_repeat_n_times_break_continue",
        //            "control_repeat_until_break",
        //            "control_wait",
        //            "control_all",
        //            "functions",
        //            "function_return",
        //            "function_no_return"
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

    public void runNepo(String programName) {
        try {
            String fullResource = TEST_BASE + programName + ".xml";
            LOG.info("***** robot: " + ROBOT + ", xml: " + fullResource + " *****");
            String xmlText = Util1.readFileContent(fullResource);
            String programText = null;
            String configText = null;
            try {
                Export jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
                String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
                String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
                assertTrue(robotType1.equals(ROBOT) && robotType2.equals(ROBOT));
                programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
                configText = JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet());
            } catch ( Exception e ) {
                LOG.info("got outdated xml: only blockset found. Please re-export the program from the lab");
                programText = xmlText;
            }
            JSONObject cmd = mkD("{'cmd':'runPBack','name':" + programName + "','language':'de'}");
            cmd.getJSONObject("data").put("programText", programText);
            cmd.getJSONObject("data").put("configText", configText);
            this.response = this.restProgram.command(httpSessionState, cmd);
            JSONObject entity = (JSONObject) this.response.getEntity();
            assertEquals("ok", entity.optString("rc", ""));
            String javaScriptProgram = entity.getString("compiledCode");
            Util1.writeFile(TEST_BASE + programName + ".json", javaScriptProgram);
        } catch ( Exception e ) {
            throw new DbcException("***** Test failed for " + programName + " *****", e);
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