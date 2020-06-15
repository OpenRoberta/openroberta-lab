package de.fhg.iais.roberta.javaServer.integrationTest;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
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
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.javaServer.basics.TestConfiguration;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientProgramController;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientUser;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotCommand;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotDownloadProgram;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
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

    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final int MAX_PARALLEL_USERS = 200;
    private static final int MAX_TOTAL_USERS = 3000;

    private ServerProperties serverProperties;

    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private RobotCommunicator robotCommunicator;

    private String connectionUrl;

    private ClientUser restUser;
    private ClientProgramController restProject;
    private ClientAdmin restBlocks;
    private RobotDownloadProgram downloadJar;
    private RobotCommand brickCommand;

    private String theProgramOfAllUserLol;
    private ExecutorService executorService;
    private Map<String, IRobotFactory> robotPlugins = new HashMap<>();

    @Before
    public void setupTest() throws Exception {
        this.serverProperties = new ServerProperties(Util.loadProperties(null));
        this.serverProperties.getserverProperties().put("server.public", "false");
        this.robotCommunicator = new RobotCommunicator();

        TestConfiguration tc = TestConfiguration.setup();
        this.sessionFactoryWrapper = tc.getSessionFactoryWrapper();
        this.memoryDbSetup = tc.getMemoryDbSetup();

        this.restUser = new ClientUser(this.robotCommunicator, serverProperties, null);
        this.restProject = new ClientProgramController(this.serverProperties);
        this.restBlocks = new ClientAdmin(this.robotCommunicator, serverProperties);
        this.downloadJar = new RobotDownloadProgram(this.robotCommunicator, serverProperties);
        this.brickCommand = new RobotCommand(this.robotCommunicator);
        this.theProgramOfAllUserLol = Resources.toString(PerformanceUserIT.class.getResource("/restInterfaceTest/action_BrickLight.xml"), Charsets.UTF_8);
        this.executorService = Executors.newFixedThreadPool(PerformanceUserIT.MAX_PARALLEL_USERS + 10);

        ServerStarter.initLoggingBeforeFirstUse(EMPTY_STRING_LIST.toArray(new String[0]));
        this.robotPlugins = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
    }

    @After
    public void showResourceUsage() throws Exception {
        Thread.sleep(20000);
        LOG.info("state of the db sessions:\n" + DbSession.getFullInfo());
        DbSession dbSession = newDbSession();
        BigInteger users = (BigInteger) dbSession.createSqlQuery("select count(*) from USER").uniqueResult();
        BigInteger programs = (BigInteger) dbSession.createSqlQuery("select count(*) from PROGRAM").uniqueResult();
        LOG.info("number of users:    " + users);
        LOG.info("number of programs: " + programs);
    }

    @Test
    public void runUsersConcurrent() throws Exception {
        // startDbSessionWatcherThread(); // log continously the state of db dession usage
        int baseNumber = 0;
        LOG.info("max parallel users: " + MAX_PARALLEL_USERS + "; total users: " + PerformanceUserIT.MAX_TOTAL_USERS);
        LOG.info("");
        @SuppressWarnings("unchecked")
        Future<Boolean>[] futures = (Future<Boolean>[]) new Future<?>[MAX_PARALLEL_USERS];
        for ( int i = 0; i < MAX_PARALLEL_USERS; i++ ) {
            futures[i] = startWorkflow(baseNumber + i);
        }
        boolean success = true;
        int terminatedWorkflows = 0;
        int nextFreeUserNumber = baseNumber + MAX_PARALLEL_USERS + 1;
        start: while ( terminatedWorkflows < MAX_TOTAL_USERS ) {
            for ( int i = 0; i < MAX_PARALLEL_USERS; i++ ) {
                if ( futures[i].isDone() ) {
                    success = success && futures[i].get();
                    if ( !success ) {
                        break start;
                    }
                    terminatedWorkflows++;
                    if ( terminatedWorkflows < MAX_TOTAL_USERS ) {
                        futures[i] = startWorkflow(nextFreeUserNumber++);
                    }
                }
            }
            Thread.sleep(10);
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
                    LOG.info("" + userNumber + ";error;");
                    LoggerFactory.getLogger("workflowError").error("Workflow " + userNumber + " terminated with errors", e);
                    return false;
                }
            }
        });
    }

    private void workflow(int userNumber) throws Exception {
        Clock clock = Clock.start();
        int thinkTimeInMillisec = 0;
        LOG.info("user: " + userNumber + "; start;");
        Random random = new Random(userNumber);

        String iTkn = "initToken-" + userNumber;
        HttpSessionState s = HttpSessionState.initOnlyLegalForDebugging(iTkn, this.robotPlugins, serverProperties, 1);
        Assert.assertTrue(!s.isUserLoggedIn());
        FullRestRequest request; // re-used to create various REST requests

        // create user "pid-*"

        String requestString =
            "{'cmd':'createUser';'accountName':'pid-acc-" + userNumber + "';'userName':'pid-user-" + userNumber + "';'password':'dip-" + userNumber + //
                "';'isYoungerThen14':0;'userEmail':'cavy-" + userNumber + "@home';'role':'STUDENT'}";
        request = mkFRR(iTkn, requestString);
        Response response = this.restUser.createUser(newDbSession(), request);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.USER_CREATE_SUCCESS);

        // login with user "pid-*" and create 2 programs

        thinkTimeInMillisec += think(random, 1, 4);
        request = mkFRR(iTkn, "{'cmd':'login';'accountName':'pid-acc-" + userNumber + "';'password':'dip-" + userNumber + "'}");
        response = this.restUser.login(newDbSession(), request);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(s.isUserLoggedIn());
        int sId = s.getUserId();
        response = this.restProject.saveProgram(newDbSession(), mkFRR(iTkn, "{'cmd':'saveAsP';'programName':'p1';'programText':'<program>...</program>'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_SAVE_SUCCESS);
        response = this.restProject.saveProgram(newDbSession(), mkFRR(iTkn, "{'cmd':'saveAsP';'programName':'p2';'programText':'<program>...</program>'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + sId));

        // "pid-*" updates p2, has 2 programs, gets the list of programs, assert that the names match

        thinkTimeInMillisec += think(random, 0, 6);
        Timestamp lastChanged = this.memoryDbSetup.getOne("select LAST_CHANGED from PROGRAM where OWNER_ID = " + sId + " and name = 'p2'");
        JSONObject requestAsJson = JSONUtilForServer.mkD(iTkn, "{'cmd':'save';'programName':'p2'}");
        requestAsJson.getJSONObject("data").put("progXML", this.theProgramOfAllUserLol).put("timestamp", lastChanged.getTime());
        request = FullRestRequest.make(requestAsJson);
        response = this.restProject.saveProgram(newDbSession(), request);
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + sId));
        response = this.restProject.getInfosOfProgramsOfLoggedInUser(newDbSession(), mkFRR(iTkn, "{'cmd':'loadPN'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_GET_ALL_SUCCESS);
        JSONArray programListing = ((JSONObject) response.getEntity()).getJSONArray("programNames");
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        JSONUtilForServer.assertJsonEquals("['p1','p2']", programNames, false);

        // user "pid" registers the robot with token "garzi-*" ; runs "p2"

        // TODO: extend the workflow by other commands like sharing, ...

        // TODO: generate the program from the sources, compile it and store the binaries for a /download request from the robot, deliver it to a pseudo-robot

        //        thinkTimeInMillisec += think(random, 6, 10);
        //        JSONUtilForServer.registerToken(this.brickCommand, this.restBlocks, s, this.sessionFactoryWrapper.getSession(), "garzi-" + userNumber);
        //        JSONUtilForServer.downloadJar(this.downloadJar, this.restProgram, s, "garzi-" + userNumber, "p2");

        LOG.info("user: " + userNumber + "; stop; elapsed: " + clock.elapsedMsec() + "; thinking: " + thinkTimeInMillisec + ";");
    }

    private int think(Random random, int lowerBoundForThinking, int upperBoundForThinking) throws Exception {
        int think = (lowerBoundForThinking + random.nextInt(upperBoundForThinking - lowerBoundForThinking)) * 1000;
        if ( think > 0 ) {
            Thread.sleep(think);
        }
        return think;
    }

    /**
     * every 5 seconds write a full info about the db sessions currently open, including an overview about the sql queries executed in that session
     */
    private void startDbSessionWatcherThread() {
        Runnable dbSessionWatcher = new Runnable() {
            @Override
            public void run() {
                while ( true ) {
                    try {
                        Thread.sleep(5000);
                    } catch ( InterruptedException e ) {
                        // do nothing. Ok in this debug setting
                    }
                    LOG.info(DbSession.getFullInfo());
                }
            }
        };
        new Thread(null, dbSessionWatcher, "dbSessionWatcher").start();
    }

    private DbSession newDbSession() {
        return this.sessionFactoryWrapper.getSession();
    }

    private static FullRestRequest mkFRR(String initTokenString, String cmdAsString) throws JSONException {
        return FullRestRequest.make(JSONUtilForServer.mkD(initTokenString, cmdAsString));
    }
}
