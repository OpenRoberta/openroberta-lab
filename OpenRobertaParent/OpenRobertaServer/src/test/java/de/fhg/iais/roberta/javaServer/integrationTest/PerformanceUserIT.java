package de.fhg.iais.roberta.javaServer.integrationTest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientProgram;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientUser;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotCommand;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotDownloadProgram;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

/**
 * TODO: make it executable in a docker conatiner; add real crosscompiler compilation; generate data for evaluation<br>
 * TODO: it seems, that we loose db connections. Check, whether this is a problem of this test setup or a general (PROD) problem!
 *
 * @author rbudde
 */
@Ignore
@Category(IntegrationTest.class)
public class PerformanceUserIT {
    private static final Logger LOG = LoggerFactory.getLogger("workflow");

    private static final int MAX_PARALLEL_USERS = 30;
    private static final int MAX_TOTAL_USERS = 400;

    private RobertaProperties robertaProperties;

    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private RobotCommunicator robotCommunicator;

    private String connectionUrl;

    private ClientUser restUser;
    private ClientProgram restProgram;
    private ClientAdmin restBlocks;
    private RobotDownloadProgram downloadJar;
    private RobotCommand brickCommand;

    private String theProgramOfAllUserLol;
    private ExecutorService executorService;
    private Map<String, IRobotFactory> robotPlugins = new HashMap<>();

    @Before
    public void setupTest() throws Exception {
        this.robertaProperties = new RobertaProperties(Util1.loadProperties(null));
        this.robertaProperties.getRobertaProperties().put("server.public", "false");
        this.connectionUrl = "jdbc:hsqldb:mem:performanceTestInMemoryDb";

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-testConcurrent-cfg.xml", this.connectionUrl);
        this.memoryDbSetup = new DbSetup(this.sessionFactoryWrapper.getNativeSession());
        this.memoryDbSetup.createEmptyDatabase();
        this.robotCommunicator = new RobotCommunicator();

        this.restUser = new ClientUser(this.robotCommunicator, robertaProperties, null);
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, this.robotCommunicator, robertaProperties);
        this.restBlocks = new ClientAdmin(this.robotCommunicator, robertaProperties);
        this.downloadJar = new RobotDownloadProgram(this.robotCommunicator, robertaProperties);
        this.brickCommand = new RobotCommand(this.robotCommunicator);
        this.theProgramOfAllUserLol = Resources.toString(PerformanceUserIT.class.getResource("/restInterfaceTest/action_BrickLight.xml"), Charsets.UTF_8);
        this.executorService = Executors.newFixedThreadPool(PerformanceUserIT.MAX_PARALLEL_USERS + 10);

        this.robotPlugins = ServerStarter.configureRobotPlugins(robotCommunicator, robertaProperties);
    }

    @Test
    public void runUsersConcurrent() throws Exception {
        int baseNumber = 0;
        PerformanceUserIT.LOG.info("max parallel users: " + PerformanceUserIT.MAX_PARALLEL_USERS + "; total users: " + PerformanceUserIT.MAX_TOTAL_USERS);
        PerformanceUserIT.LOG.info("");
        @SuppressWarnings("unchecked")
        Future<Boolean>[] futures = (Future<Boolean>[]) new Future<?>[PerformanceUserIT.MAX_PARALLEL_USERS];
        for ( int i = 0; i < PerformanceUserIT.MAX_PARALLEL_USERS; i++ ) {
            futures[i] = startWorkflow(baseNumber + i);
        }
        boolean success = true;
        int terminatedWorkflows = 0;
        int nextFreeUserNumber = baseNumber + PerformanceUserIT.MAX_PARALLEL_USERS + 1;
        start: while ( terminatedWorkflows < PerformanceUserIT.MAX_TOTAL_USERS ) {
            for ( int i = 0; i < PerformanceUserIT.MAX_PARALLEL_USERS; i++ ) {
                if ( futures[i].isDone() ) {
                    success = success && futures[i].get();
                    if ( !success ) {
                        break start;
                    }
                    terminatedWorkflows++;
                    if ( terminatedWorkflows < PerformanceUserIT.MAX_TOTAL_USERS ) {
                        futures[i] = startWorkflow(nextFreeUserNumber++);
                    }
                }
            }
            Thread.sleep(1000);
        }
        Assert.assertEquals("not all user workflow have been executed successfully", true, success);
    }

    private Future<Boolean> startWorkflow(final int userNumber) {
        return this.executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    workflow(userNumber);
                    return true;
                } catch ( Exception e ) {
                    PerformanceUserIT.LOG.info("" + userNumber + ";error;");
                    LoggerFactory.getLogger("workflowError").error("Workflow " + userNumber + " terminated with errors", e);
                    return false;
                }
            }
        });
    }

    private void workflow(int userNumber) throws Exception {
        Clock clock = Clock.start();
        int thinkTimeInMillisec = 0;
        PerformanceUserIT.LOG.info("" + userNumber + ";start;");
        Random random = new Random(userNumber);

        HttpSessionState s = HttpSessionState.init(this.robotCommunicator, this.robotPlugins, robertaProperties, 1);
        Assert.assertTrue(!s.isUserLoggedIn());

        // create user "pid-*" with success
        thinkTimeInMillisec += think(random, 1, 4);
        JSONObject request = JSONUtilForServer.mkD("" + //
            "{'cmd':'createUser';" + //
            "'accountName':'pid-acc-" + userNumber + "';" + //
            "'userName':'pid-user-" + userNumber + "';" + //
            "'password':'dip-" + userNumber + "';" + //
            "'isYoungerThen14':0;" + //
            "'userEmail':'cavy-" + userNumber + "@home';" + //
            "'role':'STUDENT'}");
        Response response = this.restUser.command(s, this.sessionFactoryWrapper.getSession(), request);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.USER_CREATE_SUCCESS);

        // login with user "pid-*" and create 2 programs
        thinkTimeInMillisec += think(random, 2, 6);
        response = //
            this.restUser.command( //
                s,
                this.sessionFactoryWrapper.getSession(),
                JSONUtilForServer.mkD("{'cmd':'login';'accountName':'pid-acc-" + userNumber + "';'password':'dip-" + userNumber + "'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(s.isUserLoggedIn());
        int sId = s.getUserId();
        response = this.restProgram.command(s, JSONUtilForServer.mkD("{'cmd':'saveAsP';'programName':'p1';'programText':'<program>...</program>'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_SAVE_SUCCESS);
        response = this.restProgram.command(s, JSONUtilForServer.mkD("{'cmd':'saveAsP';'programName':'p2';'programText':'<program>...</program>'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + sId));

        // "pid" updates p2, has 2 programs, get list of programs, assert that the names match
        thinkTimeInMillisec += think(random, 0, 6);
        Timestamp lastChanged = this.memoryDbSetup.getOne("select LAST_CHANGED from PROGRAM where OWNER_ID = " + sId + " and name = 'p2'");
        JSONObject fullRequest = new JSONObject("{\"log\":[];\"data\":{\"cmd\":\"saveP\";\"programName\":\"p2\"}}");
        fullRequest.getJSONObject("data").put("programText", this.theProgramOfAllUserLol).put("timestamp", lastChanged.getTime());
        response = this.restProgram.command(s, fullRequest);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + sId));
        response = this.restProgram.command(s, JSONUtilForServer.mkD("{'cmd':'loadPN'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_GET_ALL_SUCCESS);
        JSONArray programListing = ((JSONObject) response.getEntity()).getJSONArray("programNames");
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        JSONUtilForServer.assertJsonEquals("['p1','p2']", programNames, false);

        // user "pid" registers the robot with token "garzi-*" ; runs "p2"
        // TODO: generate the program from the sources, store it for a /download request from the robot, deliver it to the pseudo-robot
        //        thinkTimeInMillisec += think(random, 6, 10);
        //        JSONUtilForServer.registerToken(this.brickCommand, this.restBlocks, s, this.sessionFactoryWrapper.getSession(), "garzi-" + userNumber);
        //        JSONUtilForServer.downloadJar(this.downloadJar, this.restProgram, s, "garzi-" + userNumber, "p2");

        PerformanceUserIT.LOG.info("" + userNumber + ";ok;" + clock.elapsedMsec() + ";" + thinkTimeInMillisec + ";");
    }

    private int think(Random random, int lowerBoundForThinking, int upperBoundForThinking) throws Exception {
        int think = (lowerBoundForThinking + random.nextInt(upperBoundForThinking - lowerBoundForThinking)) * 1000;
        if ( think > 0 ) {
            Thread.sleep(think);
        }
        return think;
    }
}
