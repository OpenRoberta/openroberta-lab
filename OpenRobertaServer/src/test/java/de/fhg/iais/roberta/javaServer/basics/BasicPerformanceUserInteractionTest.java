package de.fhg.iais.roberta.javaServer.basics;

import static de.fhg.iais.roberta.testutil.JSONUtil.assertEntityRc;
import static de.fhg.iais.roberta.testutil.JSONUtil.assertJsonEquals;
import static de.fhg.iais.roberta.testutil.JSONUtil.downloadJar;
import static de.fhg.iais.roberta.testutil.JSONUtil.mkD;
import static de.fhg.iais.roberta.testutil.JSONUtil.registerToken;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.brick.Templates;
import de.fhg.iais.roberta.javaServer.resources.BrickCommand;
import de.fhg.iais.roberta.javaServer.resources.DownloadJar;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestBlocks;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.IntegrationTest;
import de.fhg.iais.roberta.util.Util;

@Category(IntegrationTest.class)
public class BasicPerformanceUserInteractionTest {
    private static final Logger LOG = LoggerFactory.getLogger("workflow");

    private static final int MAX_PARALLEL_USERS = 30;
    private static final int MAX_TOTAL_USERS = 400;

    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private BrickCommunicator brickCommunicator;

    private String buildXml;
    private String connectionUrl;
    private String crosscompilerBasedir;
    private String robotResourcesDir;

    private CompilerWorkflow compilerWorkflow;

    private RestUser restUser;
    private RestProgram restProgram;
    private RestBlocks restBlocks;
    private DownloadJar downloadJar;
    private BrickCommand brickCommand;

    private String theProgramOfAllUserLol;
    private ExecutorService executorService;

    @Before
    public void setup() throws Exception {
        Properties properties = Util.loadProperties("classpath:openRoberta-basicPerformanceUserInteraction.properties");
        this.buildXml = properties.getProperty("crosscompiler.build.xml");
        this.connectionUrl = properties.getProperty("hibernate.connection.url");
        this.crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");
        this.robotResourcesDir = properties.getProperty("robot.resources.dir");

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-testConcurrent-cfg.xml", this.connectionUrl);
        this.memoryDbSetup = new DbSetup(this.sessionFactoryWrapper.getNativeSession());
        this.memoryDbSetup.runDefaultRobertaSetup();
        this.brickCommunicator = new BrickCommunicator();
        this.compilerWorkflow = new CompilerWorkflow(this.crosscompilerBasedir, this.robotResourcesDir, this.buildXml);
        this.restUser = new RestUser(this.brickCommunicator);
        this.restProgram = new RestProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);
        this.restBlocks = new RestBlocks(new Templates(), this.brickCommunicator);
        this.downloadJar = new DownloadJar(this.brickCommunicator, this.crosscompilerBasedir);
        this.brickCommand = new BrickCommand(this.brickCommunicator);
        this.theProgramOfAllUserLol =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource("/ast/actions/action_BrickLight.xml"), Charsets.UTF_8);
        this.executorService = Executors.newFixedThreadPool(MAX_PARALLEL_USERS + 10);
    }

    @Test
    @Ignore
    public void runUsersConcurrent() throws Exception {
        int baseNumber = 0;
        LOG.info("max parallel users: " + MAX_PARALLEL_USERS + "; total users: " + MAX_TOTAL_USERS);
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
            Thread.sleep(1000);
        }
        assertEquals("not all user workflow have been executed successfully", true, success);
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
        LOG.info("" + userNumber + ";start;");
        Random random = new Random(userNumber);

        HttpSessionState s = HttpSessionState.init();
        assertTrue(!s.isUserLoggedIn());

        // create user "pid-*" with success
        thinkTimeInMillisec += think(random, 1, 4);
        Response response =
            this.restUser.command(s, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'createUser';'accountName':'pid-"
                + userNumber
                + "';'password':'dip-"
                + userNumber
                + "';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertEntityRc(response, "ok");

        // login with user "pid", create 2 programs
        thinkTimeInMillisec += think(random, 2, 6);
        response = //
            this.restUser.command( //
                s,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid-" + userNumber + "';'password':'dip-" + userNumber + "'}"));
        assertEntityRc(response, "ok");
        assertTrue(s.isUserLoggedIn());
        int sId = s.getUserId();
        response = this.restProgram.command(s, mkD("{'cmd':'saveP';'name':'p1';'program':'<program>...</program>'}"));
        assertEntityRc(response, "ok");
        response = this.restProgram.command(s, mkD("{'cmd':'saveP';'name':'p2';'program':'<program>...</program>'}"));
        assertEntityRc(response, "ok");
        assertEquals(2, this.memoryDbSetup.getOneInt("select count(*) from PROGRAM where OWNER_ID = " + sId));

        // "pid" updates p2, has 2 programs, get list of programs, assert that the names match
        thinkTimeInMillisec += think(random, 0, 6);
        JSONObject fullRequest = new JSONObject("{\"log\":[];\"data\":{\"cmd\":\"saveP\";\"name\":\"p2\"}}");
        fullRequest.getJSONObject("data").put("program", this.theProgramOfAllUserLol);
        response = this.restProgram.command(s, fullRequest);
        assertEntityRc(response, "ok");
        assertEquals(2, this.memoryDbSetup.getOneInt("select count(*) from PROGRAM where OWNER_ID = " + sId));
        response = this.restProgram.command(s, mkD("{'cmd':'loadPN'}"));
        assertEntityRc(response, "ok");
        JSONArray programListing = ((JSONObject) response.getEntity()).getJSONArray("programNames");
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        assertJsonEquals("['p1','p2']", programNames, false);

        // user "pid" registers the robot with token "garzi-?" ; runs "p2"
        thinkTimeInMillisec += think(random, 6, 10);
        registerToken(this.brickCommand, this.restBlocks, s, this.sessionFactoryWrapper.getSession(), "garzi-" + userNumber);
        downloadJar(this.downloadJar, this.restProgram, s, "garzi-" + userNumber, "p2");
        LOG.info("" + userNumber + ";ok;" + clock.elapsedMsec() + ";" + thinkTimeInMillisec + ";");
    }

    private int think(Random random, int lowerBoundForThinking, int upperBoundForThinking) throws Exception {
        int think = (lowerBoundForThinking + random.nextInt(upperBoundForThinking - lowerBoundForThinking)) * 1000;
        if ( think > 0 ) {
            Thread.sleep(think);
        }
        return think;
    }
}
