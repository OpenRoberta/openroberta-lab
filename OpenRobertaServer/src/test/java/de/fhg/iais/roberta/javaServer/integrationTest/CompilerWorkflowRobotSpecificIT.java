package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
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
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ProjectWorkflowRestController;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.FileUtils;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;
import static org.junit.Assert.fail;

/**
 * <b>Testing the generation of native code and the crosscompiler with robot-specific programs (i.e. sensors and actors)</b> The robots to be tested are found
 * in the test specification file 'testSpec.yml'. The 'prog'-property is not used here.<br>
 * <br>
 * The tests in this class are integration tests. The front end is <b>not</b> tested.<br>
 * <br>
 * In src/test/crossCompilerTests/robotSpecific for each robot "x" a directory with name "x" is expected. This directory contains all tests for this robot. The
 * helper methods "compileNepo" and "compileNative" are used to execute the tests.<br>
 * <br>
 * TODO: Currently some robot specific tests are prefixed with '___'. Probably these are redundant and can be removed. <br>
 * TODO: decide, whether the generation of simulation programs should be tested. Currently disabled.<br>
 * TODO: wedo programs are currently not evaluated, because new test programs freeze the evaluator
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowRobotSpecificIT {
    private static final Logger LOG = LoggerFactory.getLogger("SPECIFIC-IT");
    private static final boolean ENABLE_SIMULATOR_TESTS = false; // TODO: re-enable generation of simulation code

    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');

    private static final String[] ARGS =
        {
            "server.log.level=DEBUG",
            "server.log.configfile=/logback-test.xml"
        };

    private static final boolean CROSSCOMPILER_CALL = true;
    private static final boolean SHOW_SUCCESS = true;

    private static JSONObject robotsFromTestSpec;

    private static String resourceBase;
    private static String generatedStackmachineProgramsDir;

    private static RobotCommunicator robotCommunicator;
    private static ServerProperties serverProperties;
    private static Map<String, RobotFactory> pluginMap;
    private static HttpSessionState httpSessionState;

    private static final List<String> results = new ArrayList<>();

    @Mock
    private DbSession dbSession;
    @Mock
    private SessionFactoryWrapper sessionFactoryWrapper;
    private ProjectWorkflowRestController restWorkflow;
    private ClientAdmin restAdmin;

    @BeforeClass
    public static void setupClass() throws Exception {
        ServerStarter.initLoggingBeforeFirstUse(ARGS);
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            // fail();
        }
        Properties baseServerProperties = Util.loadProperties(null);
        serverProperties = new ServerProperties(baseServerProperties);
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
        httpSessionState = HttpSessionState.initOnlyLegalForDebugging("", pluginMap, serverProperties, 1);

        String tempDir = serverProperties.getTempDir().replace("\\", "/");
        generatedStackmachineProgramsDir = tempDir + "generatedStackmachinePrograms/";
        org.apache.commons.io.FileUtils.forceMkdir(new File(generatedStackmachineProgramsDir));

        resourceBase = "/crossCompilerTests/robotSpecific/";
        JSONObject testSpecification = Util.loadYAML("classpath:/crossCompilerTests/testSpec.yml");
        robotsFromTestSpec = testSpecification.getJSONObject("robots");
    }

    @Before
    public void setup() throws Exception {
        this.restWorkflow = new ProjectWorkflowRestController(robotCommunicator);
        this.restAdmin = new ClientAdmin(robotCommunicator, serverProperties);
        Mockito.when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        Mockito.doNothing().when(this.dbSession).commit();
    }

    @AfterClass
    public static void tearDownAndPrintResults() {
        LOG.info("XXXXXXXXXX result of robot specific test" + (results.size() == 1 ? "" : "s") + " XXXXXXXXXX");
        for ( String result : results ) {
            LOG.info(result);
        }
    }

    @Test
    public void testPlugins() throws Exception {
        Set<String> foundPlugins = pluginMap.keySet();
        for ( String robot : robotsFromTestSpec.keySet() ) {
            if ( !foundPlugins.contains(robot) ) {
                fail("Plugin not found: " + robot);
            }
        }
    }

    @Test
    public void testNepoPrograms() throws Exception {
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of robot specific tests XXXXXXXXXX");
        final String[] robotNameArray = robotsFromTestSpec.keySet().toArray(new String[0]);
        Arrays.sort(robotNameArray);
        for ( final String robotName : robotNameArray ) {
            JSONObject robot = robotsFromTestSpec.getJSONObject(robotName);
            final String robotDir = robot.getString("dir");
            final String resourceDirectory = resourceBase + robotDir;
            final boolean evalGeneratedProgram = robot.optBoolean("eval", false);
            setRobotTo(robotName);
            JSONArray programsToExcludeJA = robotsFromTestSpec.getJSONObject(robotName).optJSONArray("exclude");
            final List<String> excludedPrograms = new ArrayList<>(programsToExcludeJA == null ? 0 : programsToExcludeJA.length());
            if ( programsToExcludeJA != null ) {
                for ( Iterator<Object> it = programsToExcludeJA.iterator(); it.hasNext(); ) {
                    Object object = it.next();
                    excludedPrograms.add(object.toString());
                }
            }
            Boolean resultNext = FileUtils.fileStreamOfResourceDirectory(resourceDirectory). //
                filter(f -> f.endsWith(".xml")).map(f -> compileNepo(robotName, robotDir, evalGeneratedProgram, f, excludedPrograms)).reduce(true, (a, b) -> a && b);
            resultAcc = resultAcc && resultNext;
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all robot specific tests succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one robot specific test FAILED XXXXXXXXXX");
            fail();
        }
    }

    @Test
    public void testNativePrograms() throws Exception {
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NATIVE compilations XXXXXXXXXX");
        final String[] robotNameArray = robotsFromTestSpec.keySet().toArray(new String[0]);
        Arrays.sort(robotNameArray);
        for ( final String robotName : robotNameArray ) {
            final JSONObject robot = robotsFromTestSpec.getJSONObject(robotName);
            final String resourceDirectory = resourceBase + "/" + robot.getString("dir");
            Boolean resultNext = FileUtils.fileStreamOfResourceDirectory(resourceDirectory). //
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

    @Ignore
    @Test
    public void testSingleNepoProgram() throws Exception {
        final String robotName = "calliope2017NoBlue";
        final String programFileName = "actors_all_without_pins_and_callibot";
        final String robotDir = robotsFromTestSpec.getJSONObject(robotName).getString("dir");
        final boolean evalGeneratedProgram = true;
        setRobotTo(robotName);
        boolean result = compileNepo(robotName, robotDir, evalGeneratedProgram, programFileName + ".xml", Collections.emptyList());
        if ( !result ) {
            fail();
        }
    }

    private boolean compileNepo(String robotName, String robotDir, boolean evalGeneratedProgram, String resource, List<String> excludedPrograms) {
        httpSessionState.setToken(RandomUrlPostfix.generate(12, 12, 3, 3, 3));
        String expectRc = resource.startsWith("error") ? "error" : "ok";
        String pathToResource = resourceBase + robotDir + "/" + resource;
        try {
            logStart(robotName, pathToResource);
            boolean result = false;
            JSONObject entity = null;
            Response response = null;
            int index = resource.lastIndexOf(".xml");
            Assert.assertTrue(index > 0);
            String programName = resource.substring(0, index);
            if ( excludedPrograms.contains(programName) ) {
                result = true;
            } else if ( CROSSCOMPILER_CALL ) {
                String xmlText = Util.readResourceContent(pathToResource);

                JSONObject cmdCompile = JSONUtilForServer.mkD("{'programName':'prog','language':'de'}");
                cmdCompile.getJSONObject("data").put("progXML", xmlText).put("SSID", "1").put("password", "2");
                response = this.restWorkflow.compileProgram(null, FullRestRequest.make(cmdCompile));
                entity = checkEntityRc(response, expectRc);
                boolean compileSucceeded = entity != null;

                Export jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
                String programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
                String configText = JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet());

                if ( ENABLE_SIMULATOR_TESTS ) {
                    if ( pluginMap.get(robotName).hasSim() ) {
                        JSONObject cmdGenSim = JSONUtilForServer.mkD("{'programName':'prog','language':'de'}");
                        cmdGenSim.getJSONObject("data").put("progXML", programText).put("confXML", configText).put("SSID", "1").put("password", "2");
                        response = this.restWorkflow.getSimulationVMCode(null, FullRestRequest.make(cmdGenSim));
                        entity = checkEntityRc(response, expectRc);
                        boolean resultSimCode = entity != null;
                        result = compileSucceeded && resultSimCode;
                    } else {
                        result = compileSucceeded;
                    }
                } else {
                    result = compileSucceeded;
                }
                if ( evalGeneratedProgram && result && robotName.equals("wedo") ) {
                    String compiledCode = entity.optString("compiledCode", null);
                    if ( compiledCode != null ) {
                        StackMachineJsonRunner stackmachineRunner = new StackMachineJsonRunner(generatedStackmachineProgramsDir);
                        result = stackmachineRunner.run(programName, programText, compiledCode);
                    } else {
                        LOG.error("no compiled code found for program " + resource);
                        result = false;
                    }
                }
            } else {
                result = true;
            }
            log(result, robotName, pathToResource, null);
            return result;
        } catch ( Exception e ) {
            log(false, robotName, pathToResource, e);
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
                JSONObject cmd = JSONUtilForServer.mkD("{'programName':'" + resource + "','language':'de'}");
                String fileContent = Util.readResourceContent(fullResource);
                cmd.getJSONObject("data").put("progXML", fileContent);
                Response response = this.restWorkflow.compileNative(null, FullRestRequest.make(cmd));
                result = checkEntityRc(response, expectResult) != null;
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
            if ( thw != null ) {
                LOG.error("EXCEPTION", thw);
            }
            format = "---------- Robot: %-15s FAILED (" + cause + ") compile file: %s";
        }
        LOG.info(String.format(format, name, fullResource));
        LOG.info("]]]]]]]]]]");
        if ( result ) {
            if ( SHOW_SUCCESS ) {
                results.add(String.format("succ; %-15s; %-60s;", name, fullResource));
            }
        } else {
            results.add(String.format("FAIL; %-15s; %-60s;", name, fullResource));
        }
    }

    private void setRobotTo(String robot) throws Exception {
        Response response = this.restAdmin.setRobot(JSONUtilForServer.mkFRR("{'cmd':'setRobot','robot':'" + robot + "'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    /**
     * given a response object, that contains a JSON entity with the property "rc" (return code"), check if the value is as expected
     *
     * @param response the JSON object to check
     * @param expectedRc the return code expected
     * @param acceptableErrorCodes the codes, that are acceptable, if the rc is equal "error". In this case the test passes.
     * @return the entity attached to the response, if result is as expected, null otherwise
     */
    public static JSONObject checkEntityRc(Response response, String expectedRc, String... acceptableErrorCodes) {
        Assert.assertTrue("expectedRc is invalid", "ok".equals(expectedRc) || "error".equals(expectedRc));
        JSONObject entity = new JSONObject((String) response.getEntity());
        String receivedRc = entity.optString("rc", "");
        if ( receivedRc.equals(expectedRc) ) {
            return entity;
        } else if ( receivedRc.equals("error") ) {
            String errorCode = entity.optString("cause", "");
            for ( String acceptableErrorCode : acceptableErrorCodes ) {
                if ( errorCode.equals(acceptableErrorCode) ) {
                    return entity;
                }
            }
            // LOG.error("entity with error: " + entity.toString(2));
            return null;
        } else {
            LOG.error("entity returncode invalid: " + entity.toString(2));
            return null;
        }
    }
}