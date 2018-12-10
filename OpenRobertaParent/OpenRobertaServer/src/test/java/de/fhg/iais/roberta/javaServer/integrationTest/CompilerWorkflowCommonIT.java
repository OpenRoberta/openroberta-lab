package de.fhg.iais.roberta.javaServer.integrationTest;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
 * In src/test/crossCompilerTests/common the directory "template" contains a XML templates for each robot. The directory "conf" contains a configuration for
 * each robot. The directory "prog" contains program fragment to be inserted into all templates together with matching configurations.<br>
 * <br>
 * Enjoy
 *
 * @author rbudde
 */
@Category(IntegrationTest.class)
@RunWith(MockitoJUnitRunner.class)
public class CompilerWorkflowCommonIT {
    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflowIT.class);
    private static final boolean SHOW_SUCCESS = false;
    private static final String RESOURCE_BASE = "/crossCompilerTests/common/";

    private static final String DEFAULT_DECL = Util1.readResourceContent(RESOURCE_BASE + "decl/default.xml");
    private static final List<String> results = new ArrayList<>();

    private static RobotCommunicator robotCommunicator;
    private static ServerProperties serverProperties;
    private static String tempDir;
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
        Properties baseServerProperties = Util1.loadProperties(null);
        baseServerProperties.put("plugin.resourcedir", "..");
        serverProperties = new ServerProperties(baseServerProperties);
        tempDir = serverProperties.getTempDir();
        // TODO: add a robotBasedir property to the openRoberta.properties. Make all pathes relative to that dir. Create special accessors. Change String to Path.
        robotCommunicator = new RobotCommunicator();
        pluginMap = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties);
        httpSessionState = HttpSessionState.init(robotCommunicator, pluginMap, serverProperties, 1);
    }

    @AfterClass
    public static void printResults() {
        LOG.info("XXXXXXXXXX results of common compilations XXXXXXXXXX");
        for ( String result : results ) {
            LOG.info(result);
        }

    }

    @Before
    public void setup() throws Exception {
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, robotCommunicator, serverProperties);
        this.restAdmin = new ClientAdmin(robotCommunicator, serverProperties);
        when(this.sessionFactoryWrapper.getSession()).thenReturn(this.dbSession);
        doNothing().when(this.dbSession).commit();
    }

    @Test
    public void testCommonPartOfNepo() throws Exception {
        boolean resultAcc = true;
        LOG.info("XXXXXXXXXX START of the NEPO common compilations XXXXXXXXXX");
        for ( RobotInfo info : RobotInfo.values() ) {
            final String templateWithConfig = getTemplateWithConfigReplaced(info);
            resultAcc = resultAcc & Util1.fileStreamOfResourceDirectory(RESOURCE_BASE + "prog"). //
                filter(f -> f.endsWith(".xml")).map(f -> compileAfterProgramGenerated(info, templateWithConfig, f)).reduce(true, (a, b) -> a && b);
        }
        if ( resultAcc ) {
            LOG.info("XXXXXXXXXX all of the NEPO common compilations succeeded XXXXXXXXXX");
        } else {
            LOG.error("XXXXXXXXXX at least one of the NEPO common compilations FAILED XXXXXXXXXX");
            fail();
        }
    }

    @Test
    public void testShowGeneratedProgram() {
        RobotInfo robot = RobotInfo.calliope2017;
        String templateName = "mathAndLists.xml";
        final String template = getTemplateWithConfigReplaced(robot);
        final String generatedProgram = generateFinalProgram(template, templateName);
        LOG.info("the generated program for robot " + robot + " with template " + templateName + " is:\n" + generatedProgram);
    }

    private boolean compileAfterProgramGenerated(RobotInfo info, String template, String progNameWithXmlSuffix) throws DbcException {
        String reason = "?";
        boolean result = false;
        String robotName = info.name();
        logStart(robotName, progNameWithXmlSuffix);
        String token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
        httpSessionState.setToken(token);
        template = generateFinalProgram(template, progNameWithXmlSuffix);
        try {
            setRobotTo(info.name());
            JSONObject cmd = JSONUtilForServer.mkD("{'cmd':'compileP','name':'prog','language':'de'}");
            cmd.getJSONObject("data").put("program", template);
            this.response = this.restProgram.command(httpSessionState, cmd);
            result = checkEntityRc(this.response, "ok", "PROGRAM_INVALID_STATEMETNS");
            reason = "response-info";
        } catch ( Exception e ) {
            LOG.error("ClientProgram failed", e);
            result = false;
            reason = "exception (" + e.getMessage() + ")";
        }
        log(result, robotName, progNameWithXmlSuffix, reason);
        return result;
    }

    private void logStart(String name, String fullResource) {
        String format = "[[[[[ =============== Robot: %-150s compile file: %-60s ===============";
        String msg = String.format(format, name, fullResource);
        LOG.info(msg);
    }

    private void log(boolean result, String name, String fullResource, String reason) {
        String format;
        if ( result ) {
            format = "      +++++++++++++++ Robot: %-15s compile file: %-60s succeeded =============== ]]]]]";
        } else {
            format = "      --------------- Robot: %-15s compile file: %-60s FAILED(" + reason + ") =============== ]]]]]";
            showFailingProgram(httpSessionState.getToken());
        }
        String msg = String.format(format, name, fullResource);
        if ( result ) {
            LOG.info(msg);
        } else {
            LOG.error(msg);
        }
        if ( result ) {
            if ( SHOW_SUCCESS ) {
                results.add(String.format("succ; %-15s; %-60s;", name, fullResource));
            }
        } else {
            results.add(String.format("fail; %-15s; %-60s;", name, fullResource));
        }
    }

    private void setRobotTo(String robot) throws Exception, JSONException {
        this.response = this.restAdmin.command(httpSessionState, this.dbSession, JSONUtilForServer.mkD("{'cmd':'setRobot','robot':'" + robot + "'}"), null);
        JSONUtilForServer.assertEntityRc(this.response, "ok", Key.ROBOT_SET_SUCCESS);
    }

    private static String getTemplateWithConfigReplaced(RobotInfo info) {
        String template = Util1.readResourceContent(RESOURCE_BASE + "template/" + info.dirName + ".xml");
        String defaultConfig = Util1.readResourceContent(RESOURCE_BASE + "conf/" + info.dirName + ".xml");
        final String templateWithConfig = template.replaceAll("\\[\\[conf\\]\\]", defaultConfig);
        return templateWithConfig;
    }

    private static String generateFinalProgram(String template, String progNameWithXmlSuffix) {
        String prog = read("prog", progNameWithXmlSuffix);
        Assert.assertNotNull(prog, "program not found: " + progNameWithXmlSuffix);
        template = template.replaceAll("\\[\\[prog\\]\\]", prog);
        String progFragment = read("progFragment", progNameWithXmlSuffix);
        template = template.replaceAll("\\[\\[progFragment\\]\\]", progFragment == null ? "" : progFragment);
        String decl = read("decl", progNameWithXmlSuffix);
        template = template.replaceAll("\\[\\[decl\\]\\]", decl == null ? DEFAULT_DECL : decl);
        return template;
    }

    private static String read(String directoryName, String progNameWithXmlSuffix) {
        try {
            return Util1.readResourceContent(RESOURCE_BASE + directoryName + "/" + progNameWithXmlSuffix);
        } catch ( Exception e ) {
            // this happens, if no decl or fragment is available for the program given. This is legel.
            return null;
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

    private static void showFailingProgram(String token) {
        try {
            String tokenDir = tempDir + token;
            Util1.fileStreamOfFileDirectory(tokenDir).forEach(f -> showFailingProgramFromTokenDir(tokenDir, f));
        } catch ( Exception e ) {
            LOG.error("could not show the failing program. Probably the generation failed.");
        }
    }

    private static void showFailingProgramFromTokenDir(String tokenDir, String dir) {
        String sourceDir = tokenDir + "/" + dir + "/source";
        Util1.fileStreamOfFileDirectory(sourceDir).forEach(f -> showFailingProgramFromSourceDir(sourceDir, f));
    }

    private static void showFailingProgramFromSourceDir(String sourceDir, String fileName) {
        String sourceFile = sourceDir + "/" + fileName;
        List<String> sourceLines = Util1.readFileLines(fileName);
        StringBuilder sb = new StringBuilder();
        int lineCounter = 1;
        for ( String sourceLine : sourceLines ) {
            sb.append(String.format("%-4d %s", lineCounter++, sourceLine)).append("\n");
        }
        LOG.error("failing source from file: " + sourceFile + " is:\n" + sb.toString());
    }

    private static enum RobotInfo {
        ev3lejosv1( "ev3" ),
        ev3lejosv0( "ev3" ),
        ev3dev( "ev3" ),
        nxt( "nxt" ),
        microbit( "microbit" ),
        uno( "ardu" ),
        nano( "ardu" ),
        mega( "ardu" ),
        nao( "nao" ),
        bob3( "bob3" ),
        calliope2017( "calliope" ),
        calliope2016( "calliope" );

        public final String dirName;

        private RobotInfo(String dirName) {
            this.dirName = dirName;
        }
    }
}