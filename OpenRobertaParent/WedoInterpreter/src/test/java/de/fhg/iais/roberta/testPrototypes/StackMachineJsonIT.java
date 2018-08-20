package de.fhg.iais.roberta.testPrototypes;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * <b>Testing the generation and execution of code.</b> At the moment exclusively for the WeDo robot. To be extended at least to the simulation.<br>
 * <br>
 * <i>Usage:</i><br>
 * Create a program in the lab. Put the expected result into the program documentation tab, e.g.<br>
 * <br>
 * <code>
 * ROBOT<br>
 * wedo<br>
 * START-RESULT<br>
 * show "1"<br><br>show "2"<br>
 * END-RESULT<br><br>
 * </code> The string after "show" refers to the string written by the "show text"/"zeige Text"-blockly-block in the program. Empty lines are ignored.<br>
 * Export the program into the directory "WedoInterpreter/WeDoCI".<br>
 * <br>
 * If this class {@linkplain StackMachineJsonIT} is run as an Junit4 test, first all blockly-xml-files found in the directory mentioned above are compiled.
 * Dependent on the robot this generates Java, Python, C(++) or a byte code in the ORA-format "StackMachineJson". The generated code is compiled using the
 * robot-dependent crosscompiler-chain.<br>
 * <br>
 * If the robot is "wedo", more is done: the JavaScript found in "WedoInterpreter/jsGenerated/runStackMachineJson.js" is executed with "node" (must be on the
 * path!). This runs the interpreter for "StackMachineJson". The results of this interpretation are assembled and compared with the expected results from the
 * program tab. Success and failure are logged.<br>
 * <i>Note:</i> all sources used for this interpretation are found in directory "WedoInterpreter/ts". They are written in TypeScript, transpiled to JavaScript
 * and saved into the directory "WedoInterpreter/jsGenerated". From this directory they are used by "node".
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class StackMachineJsonIT {
    private static final Logger LOG = LoggerFactory.getLogger(StackMachineJsonIT.class);

    private static final String ROBOT_DEFAULT = "wedo";
    private static final Pattern ROBOT_PATTERN = Pattern.compile(".*ROBOT(.*)START-RESULT");
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

    private Map<String, Boolean> resultsOfCompilation = new TreeMap<>();
    List<String> resultsOfInterpretation = new ArrayList<>();

    private boolean successForCompilation = false;
    private boolean successForInterpretation = false;

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
        generateStackMachineJsonFromBlocklyXmlIT();
        runInterpretStackMachineJsonInterpreter(NODE_CALL);
        showInterpretationResultOverview(); // must be the first "show" (otherwise refactor full output from overview output)
        showCompilationResultOverview();
        showAndEvaluateResult();
    }

    @Ignore
    @Test
    public void testOneFile() throws Exception {
        String directory = "./WeDoCI/"; // change as needed
        String file = "almost-all-blocks"; // change as needed

        String nodeCall = "node ./jsGenerated/runStackMachineJson.js" + " " + directory + " " + file;
        runCompilation(directory, file);
        runInterpretStackMachineJsonInterpreter(nodeCall);
        showAndEvaluateResult();
    }

    @Test
    @Ignore
    public void testGenerateStackMachineJsonFromBlocklyXmlITtest() throws Exception {
        generateStackMachineJsonFromBlocklyXmlIT();
        showCompilationResultOverview();
    }

    private void generateStackMachineJsonFromBlocklyXmlIT() throws Exception {
        Stream<String> filesFromDirectory = Util1.fileStreamOfFileDirectory(TEST_BASE);
        successForCompilation =
            filesFromDirectory
                .filter(f -> f.endsWith(".xml"))
                .map(f -> f.substring(0, f.length() - 4))
                .sorted() //
                .map(s -> {
                    runCompilation(TEST_BASE, s);
                    return successForCompilation;
                })
                .reduce(true, (a, b) -> a && b);
    }

    private void runCompilation(String directory, String programName) {
        successForCompilation = false;
        try {
            resultsOfCompilation.put(programName, false);
            String fullResource = directory + programName + ".xml";
            String xmlText = Util1.readFileContent(fullResource);
            Matcher lookupRobot = ROBOT_PATTERN.matcher(xmlText);
            String robot = lookupRobot.matches() ? lookupRobot.group(1) : ROBOT_DEFAULT;
            LOG.info(MARK + " robot: " + robot + ", xml: " + fullResource + " " + MARK);
            setRobotTo(robot);
            String programText = null;
            String configText = null;
            Export jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
            String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
            String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
            assertTrue(robotType1.equals(robot) && robotType2.equals(robot));
            programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
            configText = JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet());
            JSONObject cmd = mkD("{'cmd':'runPBack','name':" + programName + "','language':'de'}");
            cmd.getJSONObject("data").put("programText", programText);
            cmd.getJSONObject("data").put("configurationText", configText);
            this.response = this.restProgram.command(httpSessionState, cmd);
            JSONObject entity = (JSONObject) this.response.getEntity();
            if ( !"ok".equals(entity.optString("rc", "")) ) {
                String rc = entity.optString("rc", "???");
                String errorCounter = entity.optString("errorCounter", "???");
                String message = entity.optString("message", "???");
                String cause = entity.optString("cause", "???");
                LOG.error("compilation fails. rc: " + rc + ", errorCounter: " + errorCounter + ", message: " + message + ", cause: " + cause);
                throw new DbcException("compilation fails");
            }
            String javaScriptProgram = entity.getString("compiledCode");
            Util1.writeFile(directory + programName + ".json", javaScriptProgram);
            resultsOfCompilation.put(programName, true);
            successForCompilation = true;
        } catch ( Exception e ) {
            LOG.error(MARK + " compilation failed for " + programName + " " + MARK); // stacktrace is unusable
            successForCompilation = false;
        }
    }

    private void runInterpretStackMachineJsonInterpreter(String nodeCall) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(nodeCall).getInputStream(), Charset.forName("UTF-8")));
        resultsOfInterpretation = br.lines().collect(Collectors.toList());
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        this.response = this.restAdmin.command(httpSessionState, this.dbSession, mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        assertEntityRc(this.response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    private void showCompilationResultOverview() {
        for ( Entry<String, Boolean> e : resultsOfCompilation.entrySet() ) {
            String result = String.format(MARK + "    compilation of %-30s : %-10s " + MARK, '"' + e.getKey() + '"', e.getValue() ? "success" : "ERROR");
            LOG.info(result);
        }
    }

    private void showInterpretationResultOverview() {
        successForInterpretation = false;
        for ( String line : resultsOfInterpretation ) {
            if ( line.equals("********** result of all interpretations: success **********") ) {
                successForInterpretation = true;
            }
            LOG.info(line);
        }
    }

    private void showAndEvaluateResult() {
        if ( !successForCompilation ) {
            LOG.error("some programs failed when being compiled");
        }
        if ( !successForInterpretation ) {
            LOG.error("some programs failed when being interpreted");
        }
        if ( !successForCompilation || !successForInterpretation ) {
            fail();
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