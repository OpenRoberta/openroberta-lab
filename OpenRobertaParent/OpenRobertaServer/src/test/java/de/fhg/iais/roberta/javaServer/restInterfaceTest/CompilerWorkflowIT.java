package de.fhg.iais.roberta.javaServer.restInterfaceTest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.fhg.iais.roberta.blockly.generated.Export;
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
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>Testing the generation of native code and the CROSSCOMPILER</b><br>
 * <br>
 * The tests in this class are integration tests. The front end is <b>not</b> tested. The tests deliver programs (either NEPO programs encoded in XML or native
 * programs encoded as expected by the crosscompilers) to the various crosscompilers and check whether the expected response is returned ("ok", "error").<br>
 * <br>
 * In src/test/compilerWorkflowTest for each robot "x" a directory with name "x" is expected. This directory contains all tests for this robot. The helper
 * methods "compileNepo" and "compileNative" are used to execute one test.
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowIT {
    private static final List<String> EXPECTED_PLUGINS =
        Arrays.asList("ev3lejosv1", "ev3lejos", "ev3dev", "nxt", "microbit", "ardu", "nao", "bob3", "calliope2017", "calliope2016");

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
        robertaProperties = new RobertaProperties(Util1.loadProperties("classpath:openRoberta.properties"));
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, robertaProperties);
        httpSessionState = HttpSessionState.init(robotCommunicator, pluginMap, robertaProperties, 1);
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
        Assert.assertEquals(EXPECTED_PLUGINS.size(), foundPlugins.size());
        for ( String plugin : EXPECTED_PLUGINS ) {
            if ( !foundPlugins.contains(plugin) ) {
                Assert.fail("Plugin not found: " + plugin);
            }
        }
    }

    @Test
    public void testNative() throws Exception {
        compileNative(false, "nxt", "error", "nxc");

        compileNative(true, "ardu", "drive", "ino");
        compileNative(true, "bob3", "eye", "ino");
        compileNative(true, "calliope2016", "led", "cpp");
        compileNative(true, "calliope2017", "led", "cpp");
        compileNative(true, "ev3dev", "drive", "py");
        compileNative(true, "ev3lejos", "Drive", "java");
        compileNative(true, "ev3lejosv1", "Drive", "java");
        compileNative(true, "microbit", "pic", "py");
        compileNative(true, "nao", "move", "py");
        compileNative(true, "nxt", "drive", "nxc");
        compileNative(true, "sim", "Drive", "java");
    }

    @Test
    public void testNepo() throws Exception {
        compileNepo(false, "nxt", "error.xml");

        compileNepo(true, "ardu", "drive.xml");
        compileNepo(true, "bob3", "eye.xml");
        compileNepo(true, "calliope2016", "led.xml");
        compileNepo(true, "calliope2017", "led.xml");
        compileNepo(true, "ev3dev", "drive.xml");
        compileNepo(true, "ev3lejos", "drive.xml");
        compileNepo(true, "ev3lejosv1", "drive.xml");
        compileNepo(true, "microbit", "pic.xml");
        compileNepo(true, "nao", "move.xml");
        compileNepo(true, "nxt", "drive.xml");
        compileNepo(true, "sim", "drive.xml");
    }

    // small helpers
    @SuppressWarnings("unused")
    private static final boolean _____helper_start_____ = true;

    private void compileNative(boolean expectResultOk, String robot, String name, String suffix) throws Exception {
        setRobotTo(robot);
        JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileN','name':'" + name + "','language':'de'}");
        String fileContent = Util1.readResourceContent("/compilerWorkflowTest/" + robot + "/" + name + "." + suffix);
        cmd.getJSONObject("data").put("programText", fileContent);
        response = this.restProgram.command(httpSessionState, cmd);
        assertEntityRc(this.response, expectResultOk ? "ok" : "error");
    }

    private void compileNepo(boolean expectResultOk, String robot, String resource) throws Exception {
        setRobotTo(robot);
        JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileP','name':'prog','language':'de'}");
        setProgConf(cmd.getJSONObject("data"), Util1.readResourceContent("/compilerWorkflowTest/" + robot + "/" + resource));
        response = this.restProgram.command(httpSessionState, cmd);
        assertEntityRc(this.response, expectResultOk ? "ok" : "error");

    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        response = this.restAdmin.command(httpSessionState, dbSession, JSONUtilForServer.mkD("{'cmd':'setRobot','robot':'" + robot + "'}"));
        JSONUtilForServer.assertEntityRc(this.response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    private static void setProgConf(JSONObject data, String exportedProgramAsXmlString) throws Exception {
        Export jaxb = JaxbHelper.xml2Element(exportedProgramAsXmlString, Export.class);
        data.put("programText", JaxbHelper.blockSet2xml(jaxb.getProgram().getBlockSet()));
        data.put("configurationText", JaxbHelper.blockSet2xml(jaxb.getConfig().getBlockSet()));
    }

    /**
     * given a response object, that contains a JSON entity with the property "rc" (return code"), assert that the value is as expected
     *
     * @param response the JSON object to check
     * @param rc the return code expected
     * @throws JSONException
     */
    public static void assertEntityRc(Response response, String rc) throws JSONException {
        JSONObject entity = (JSONObject) response.getEntity();
        Assert.assertEquals(rc, entity.getString("rc"));
    }
}