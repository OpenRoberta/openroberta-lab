package de.fhg.iais.roberta.testPrototypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>Testing the generation of simulator code</b><br>
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class StackMachineJsonIT {
    private static final Logger LOG = LoggerFactory.getLogger(StackMachineJsonIT.class);

    private static final String ROBOT = "wedo";
    private static final String MARK = "**********";

    private static final String TEST_BASE = "./WeDoCI/";
    private static final String NODE_CALL = "node ./jsGenerated/runStackMachineJson.js" + " -d " + TEST_BASE;

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

    private Map<String, Boolean> resultsOfCompilation = new HashMap<>();
    private boolean successForCompilation = false;

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
    public void testDirectory() throws Exception {
        testGenerateStackMachineJsonFromBlocklyXmlIT();
        runInterpretStackMachineJsonInterpreter(NODE_CALL);
        showCompilationResultOverview(true); // a second call at the end to have a better overview
    }

    //@Ignore
    @Test
    public void testOneFile() throws Exception {
        String directory = "./WeDoCI/";
        String file = "functions";
        successForCompilation = runCompilation(directory, file);
        String nodeCall = "node ./jsGenerated/runStackMachineJson.js" + " " + directory + " " + file;
        runInterpretStackMachineJsonInterpreter(nodeCall);
        showCompilationResultOverview(true);
    }

    @Test
    @Ignore
    public void testGenerateStackMachineJsonFromBlocklyXmlIT() throws Exception {
        setRobotTo(ROBOT);
        Stream<String> filesFromDirectory = Util1.fileStreamOfFileDirectory(TEST_BASE);
        successForCompilation =
            filesFromDirectory.filter(f -> f.endsWith(".xml")).map(f -> f.substring(0, f.length() - 4)).map(s -> runCompilation(TEST_BASE, s)).reduce(
                true,
                (a, b) -> a && b);
        showCompilationResultOverview(false);
    }

    private void runInterpretStackMachineJsonInterpreter(String nodeCall) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(nodeCall).getInputStream(), Charset.forName("UTF-8")));
        br.lines().forEach(System.out::println);
    }

    private boolean runCompilation(String directory, String programName) {
        try {
            resultsOfCompilation.put(programName, false);
            String fullResource = directory + programName + ".xml";
            LOG.info(MARK + " robot: " + ROBOT + ", xml: " + fullResource + " " + MARK);
            String xmlText = Util1.readFileContent(fullResource);
            String programText = null;
            String configText = null;
            Export jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
            String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
            String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
            assertTrue(robotType1.equals(ROBOT) && robotType2.equals(ROBOT));
            programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
            configText = JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet());
            JSONObject cmd = mkD("{'cmd':'runPBack','name':" + programName + "','language':'de'}");
            cmd.getJSONObject("data").put("programText", programText);
            cmd.getJSONObject("data").put("configText", configText);
            this.response = this.restProgram.command(httpSessionState, cmd);
            JSONObject entity = (JSONObject) this.response.getEntity();
            assertEquals("ok", entity.optString("rc", ""));
            String javaScriptProgram = entity.getString("compiledCode");
            Util1.writeFile(directory + programName + ".json", javaScriptProgram);
            resultsOfCompilation.put(programName, true);
            return true;
        } catch ( Exception e ) {
            LOG.error(MARK + " Test failed for " + programName + " " + MARK, e);
            return false;
        }
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        this.response = this.restAdmin.command(httpSessionState, this.dbSession, mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        assertEntityRc(this.response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    private void showCompilationResultOverview(boolean syso) {
        assertTrue("some programs failed when being compiled", successForCompilation);
        for ( Entry<String, Boolean> e : resultsOfCompilation.entrySet() ) {
            String result = String.format(MARK + " compilation of %-30s : %-8s " + MARK, '"' + e.getKey() + '"', e.getValue() ? "success" : "ERROR");
            if ( syso ) {
                System.out.println(result);
            } else {
                LOG.info(result);
            }
        }
    }

    /**
     * see {JSONUtilForServer}
     */
    private static JSONObject mkD(String s) throws JSONException {
        return new JSONObject("{'data':" + s + ",'log':[]}".replaceAll("'", "\""));
    }

    /**
     * copy from JSONUtilForServer
     */
    private static void assertEntityRc(Response response, String rc, Key message) throws JSONException {
        JSONObject entity = (JSONObject) response.getEntity();
        Assert.assertEquals(rc, entity.getString("rc"));
        String responseKey = entity.optString("message");
        Assert.assertEquals(message.getKey(), responseKey);
    }
}