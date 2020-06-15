package de.fhg.iais.roberta.javaServer.integrationTest;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.testutil.HttpClientWrapper;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.Key;

/**
 * run a workflow concurrently against a docker container<br>
 * - expects a running container accessible by SERVER_URL<br>
 * - runs about MAX_TOTAL_USERS workflows, MAX_PARALLEL_USERS in concurrent<br>
 * <br>
 * this test should be committed only @Ignore-d
 *
 * @author rbudde
 */
@Ignore
public class DockerIT {
    private static final Logger LOG = LoggerFactory.getLogger("workflow");

    private static final int MAX_CONCURRENT_USERS = 20;
    private static final int MAX_TOTAL_USERS = 20000;

    private static final String CHARSET_FOR_RANDOM_STRING = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
    private static final int CHARSET_FOR_RANDOM_STRING_LENGTH = CHARSET_FOR_RANDOM_STRING.length();
    private static final String SERVER_URL = "http://localhost:1999/";
    // private static final String SERVER_URL = "http://ilya.iais.fraunhofer.de:1999/";

    @SuppressWarnings("unchecked")
    private static final Future<Boolean>[] futures = (Future<Boolean>[]) new Future<?>[DockerIT.MAX_CONCURRENT_USERS];

    private String calliProg;
    private ExecutorService executorService;
    private Random random;

    @Before
    public void setup() throws Exception {
        this.calliProg = Resources.toString(DockerIT.class.getResource("/restInterfaceTest/calliProg.xml"), Charsets.UTF_8);
        this.executorService = Executors.newFixedThreadPool(DockerIT.MAX_CONCURRENT_USERS + 10);
        random = new Random();
        LOG.info("max parallel users: " + DockerIT.MAX_CONCURRENT_USERS + "; total users: " + DockerIT.MAX_TOTAL_USERS);
        LOG.info("");
    }

    @Test
    public void runUsersConcurrent() throws Exception {
        int baseNumber = 0;
        for ( int i = 0; i < DockerIT.MAX_CONCURRENT_USERS; i++ ) {
            futures[i] = startWorkflow(baseNumber + i);
        }
        boolean success = true;
        int terminatedWorkflows = 0;
        int nextFreeUserNumber = baseNumber + DockerIT.MAX_CONCURRENT_USERS + 1;
        start: while ( terminatedWorkflows < DockerIT.MAX_TOTAL_USERS ) {
            for ( int i = 0; i < DockerIT.MAX_CONCURRENT_USERS; i++ ) {
                if ( futures[i].isDone() ) {
                    success = success && futures[i].get();
                    if ( !success ) {
                        break start;
                    }
                    terminatedWorkflows++;
                    if ( terminatedWorkflows < DockerIT.MAX_TOTAL_USERS ) {
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
                    LOG.info("" + userNumber + ";error;");
                    LoggerFactory.getLogger("workflowError").error("Workflow " + userNumber + " terminated with errors", e);
                    return false;
                }
            }
        });
    }

    /**
     * this method defines the workflow, that is executed, e.g. set a robot, compile a program, e.g.
     *
     * @param userNumber
     */
    private void workflow(final int userNumber) throws Exception {
        Clock clock = Clock.start();
        int thinkTimeInMillisec = 0;
        LOG.info("" + userNumber + ";start;");
        HttpClientWrapper httpClientWrapper = new HttpClientWrapper();

        // create a user, then think 1-4 sec
        final String randomString = getAlphaNumericString(10);
        String suffix = "-" + randomString + "-" + userNumber;
        JSONObject request = JSONUtilForServer.mkD("" + //
            "{'cmd':'createUser';" + //
            "'accountName':'pid-acc" + suffix + "';" + //
            "'userName':'pid-user" + suffix + "';" + //
            "'password':'dip" + suffix + "';" + //
            "'isYoungerThen14':0;" + //
            "'userEmail':'cavy" + suffix + "@home';" + //
            "'role':'TEACHER'}");
        thinkTimeInMillisec += step(httpClientWrapper, "user", request, Key.USER_CREATE_SUCCESS, 1, 4);

        // login with user "pid-*" and create 2 programs, think 2-6 sec
        request = JSONUtilForServer.mkD("{'cmd':'login';'accountName':'pid-acc" + suffix + "';'password':'dip" + suffix + "'}");
        thinkTimeInMillisec += step(httpClientWrapper, "user", request, Key.USER_GET_ONE_SUCCESS, 2, 6);
        request = JSONUtilForServer.mkD("{'cmd':'saveAsP';'programName':'p1" + randomString + "';'programText':'<program>...</program>'}");
        thinkTimeInMillisec += step(httpClientWrapper, "program", request, Key.PROGRAM_SAVE_SUCCESS, 2, 6);
        request = JSONUtilForServer.mkD("{'cmd':'saveAsP';'programName':'p2" + randomString + "';'programText':'<program>...</program>'}");
        thinkTimeInMillisec += step(httpClientWrapper, "program", request, Key.PROGRAM_SAVE_SUCCESS, 2, 6);

        // set robot type, then think 10-14 sec
        request = JSONUtilForServer.mkD("{'cmd':'setRobot';'robot':'calliope2017'}");
        thinkTimeInMillisec += step(httpClientWrapper, "admin", request, Key.ROBOT_SET_SUCCESS, 10, 14);

        // compile a program 5 times, think 5-10 sec
        request = JSONUtilForServer.mkD("{'cmd':'compileP';'name':'calliProg'}");
        request.getJSONObject("data").put("program", this.calliProg);
        for ( int i = 0; i < 5; i++ ) {
            thinkTimeInMillisec += step(httpClientWrapper, "program", request, Key.COMPILERWORKFLOW_SUCCESS, 5, 10);
        }

        httpClientWrapper.shutdown();
        LOG.info("" + userNumber + ";ok;" + clock.elapsedMsec() + ";" + thinkTimeInMillisec + ";");
    }

    private int step(HttpClientWrapper hw, String restEndpoint, JSONObject request, Key responseKey, int lowerBoundForThinking, int upperBoundForThinking)
        throws Exception //
    {
        String response = hw.post(SERVER_URL + restEndpoint, "application/json", request.toString(2), "application/json");
        JSONUtilForServer.assertEntityRc(response, "ok", responseKey);
        return think(random, lowerBoundForThinking, upperBoundForThinking);
    }

    /**
     * computes a random value between lowerBoundForThinking and upperBoundForThinking and waits that time. This is used to simulate the time, a user thinks,
     * before the next action takes place.
     *
     * @param random the random number generator
     * @param lowerBoundForThinking
     * @param upperBoundForThinking
     * @return the time waited before the method terminates (the time to think)
     */
    private int think(Random random, int lowerBoundForThinking, int upperBoundForThinking) throws Exception {
        int think = (lowerBoundForThinking + random.nextInt(upperBoundForThinking - lowerBoundForThinking)) * 1000;
        if ( think > 0 ) {
            Thread.sleep(think);
        }
        return think;
    }

    /**
     * generate a random string of length n
     *
     * @param n the length of the string
     * @return the random string
     */
    private String getAlphaNumericString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for ( int i = 0; i < n; i++ ) {
            sb.append(CHARSET_FOR_RANDOM_STRING.charAt(random.nextInt(CHARSET_FOR_RANDOM_STRING_LENGTH)));
        }
        return sb.toString();
    }
}
