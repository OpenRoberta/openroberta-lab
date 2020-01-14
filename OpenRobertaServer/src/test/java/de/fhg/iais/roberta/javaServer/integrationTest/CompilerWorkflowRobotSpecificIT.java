package de.fhg.iais.roberta.javaServer.integrationTest;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;
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
import org.junit.BeforeClass;
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

    private static final String TEST_SPEC_YAML = "testSpec.yml";
    // private static final String TEST_SPEC_YAML = "testSpecOnlyWedo.yml";

    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');

    private static JSONObject robots;
    private static boolean crosscompilerCall;
    private static boolean showSuccess;

    private static String resourceBase;
    private static String generatedStackmachineProgramsDir;

    private static RobotCommunicator robotCommunicator;
    private static ServerProperties serverProperties;
    private static Map<String, IRobotFactory> pluginMap;
    private static HttpSessionState httpSessionState;

    private final List<String> results = new ArrayList<>();

    @Mock
    private DbSession dbSession;
    @Mock
    private SessionFactoryWrapper sessionFactoryWrapper;

    private ProjectWorkflowRestController restWorkflow;
    private ClientAdmin restAdmin;

    @BeforeClass
    public static void setupClass() throws Exception {
        ServerStarter.initLoggingBeforeFirstUse(new String[0]);
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            fail();
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
        JSONObject testSpecification = Util.loadYAML("classpath:" + resourceBase + TEST_SPEC_YAML);
        robots = testSpecification.getJSONObject("robots");
        crosscompilerCall = testSpecification.getBoolean("crosscompilercall");
        showSuccess = testSpecification.getBoolean("showsuccess");
    }

    @Before
    public void setup() throws Exception {
        this.restWorkflow = new ProjectWorkflowRestController(robotCommunicator);
        this.restAdmin = new ClientAdmin(robotCommunicator, serverProperties);
        when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        doNothing().when(this.dbSession).commit();
    }

    @After
    public void tearDownAndPrintResults() {
        LOG.info("XXXXXXXXXX result of cross compiler test" + (results.size() == 1 ? "" : "s") + " XXXXXXXXXX");
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
    public void testNepoPrograms() throws Exception {
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NEPO cross compiler tests XXXXXXXXXX");
        for ( final String robotName : robots.keySet() ) {
            JSONObject robot = robots.getJSONObject(robotName);
            final String robotDir = robot.getString("dir");
            final String resourceDirectory = resourceBase + robotDir;
            final boolean evalGeneratedProgram = robot.optBoolean("eval", false);
            setRobotTo(robotName);
            Boolean resultNext = FileUtils.fileStreamOfResourceDirectory(resourceDirectory).//
                filter(f -> f.endsWith(".xml")).map(f -> compileNepo(robotName, robotDir, evalGeneratedProgram, f)).reduce(true, (a, b) -> a && b);
            resultAcc = resultAcc && resultNext;
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all of the NEPO cross compiler tests succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the NEPO cross compiler tests FAILED XXXXXXXXXX");
            fail();
        }
    }

    @Ignore
    @Test
    public void testSingleNepoProgram() throws Exception {
        final String robotName = "wedo";
        final String programFileName = "ci_motor-and-tone";
        final String robotDir = robots.getJSONObject(robotName).getString("dir");
        final boolean evalGeneratedProgram = true;
        setRobotTo(robotName);
        boolean result = compileNepo(robotName, robotDir, evalGeneratedProgram, programFileName + ".xml");
        if ( !result ) {
            fail();
        }
    }

    @Test
    public void testNativePrograms() throws Exception {
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NATIVE compilations XXXXXXXXXX");
        for ( final String robotName : robots.keySet() ) {
            final JSONObject robot = robots.getJSONObject(robotName);
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

    private boolean compileNepo(String robotName, String robotDir, boolean evalGeneratedProgram, String resource) {
        httpSessionState.setToken(RandomUrlPostfix.generate(12, 12, 3, 3, 3));
        String expectResult = resource.startsWith("error") ? "error" : "ok";
        String fullResource = resourceBase + robotDir + "/" + resource;
        try {
            logStart(robotName, fullResource);
            boolean result = false;
            org.codehaus.jettison.json.JSONObject entity = null;
            if ( crosscompilerCall ) {
                org.codehaus.jettison.json.JSONObject cmd = JSONUtilForServer.mkD("{'programName':'prog','language':'de'}");

                String xmlText = Util.readResourceContent(fullResource);
                Export jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
                String programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
                cmd.getJSONObject("data").put("programBlockSet", xmlText);
                Response response = this.restWorkflow.compileProgram(cmd);
                entity = checkEntityRc(response, expectResult, "ORA_PROGRAM_INVALID_STATEMETNS");
                result = entity != null;
                if ( result && robotName.equals("wedo") && evalGeneratedProgram ) {
                    String compiledCode = entity.optString("compiledCode", null);
                    if ( compiledCode != null ) {
                        final String programName = resource.substring(0, resource.length() - 4);
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
            if ( crosscompilerCall ) {
                org.codehaus.jettison.json.JSONObject cmd = JSONUtilForServer.mkD("{'programName':'" + resource + "','language':'de'}");
                String fileContent = Util.readResourceContent(fullResource);
                cmd.getJSONObject("data").put("programText", fileContent);
                Response response = this.restWorkflow.compileNative(cmd);
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
            if ( showSuccess ) {
                results.add(String.format("succ; %-15s; %-60s;", name, fullResource));
            }
        } else {
            results.add(String.format("fail; %-15s; %-60s;", name, fullResource));
        }
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        Response response = this.restAdmin.command(JSONUtilForServer.mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    /**
     * given a response object, that contains a JSON entity with the property "rc" (return code"), check if the value is as expected
     *
     * @param response the JSON object to check
     * @param rc the return code expected
     * @param acceptableErrorCodes the codes, that are acceptable, if the rc is equal "error". In this case the test passes.
     * @return the entity attached to the response, if result is as expected, null otherwise
     */
    private static org.codehaus.jettison.json.JSONObject checkEntityRc(Response response, String rc, String... acceptableErrorCodes) {
        de.fhg.iais.roberta.util.dbc.Assert.nonEmptyString(rc);
        org.codehaus.jettison.json.JSONObject entity = (org.codehaus.jettison.json.JSONObject) response.getEntity();
        String returnCode = entity.optString("rc", "");
        if ( rc.equals(returnCode) ) {
            return entity;
        } else if ( rc.equals("error") ) {
            String errorCode = entity.optString("cause", "");
            for ( String acceptableErrorCode : acceptableErrorCodes ) {
                if ( errorCode.equals(acceptableErrorCode) ) {
                    return entity;
                }
            }
            return null;
        } else {
            return null;
        }
    }
}