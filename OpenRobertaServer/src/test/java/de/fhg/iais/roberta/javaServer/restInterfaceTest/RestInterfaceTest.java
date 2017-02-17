package de.fhg.iais.roberta.javaServer.restInterfaceTest;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * <b>Testing the REST interface of the OpenRoberta server</b><br>
 * <br>
 * The tests in this class are integration tests. The front end is <b>not</b> tested. But as the server is called by small encapsulated REST calls issued from
 * the front end, the tests in this class should be easy to understand (try it!) and they are closely related to typical user stories as <i>log in with a wrong
 * password, then with the correct one, then create 4 programs and update one of them</i>. The more user stories are modeled in this class, the more confident
 * one can be, that the server is ok and errors visible in a browser will be in the Javascript front end only. The ultimate aim of this is to reduce the
 * debugging time of the front end considerably.<br>
 * <br>
 * The test class incorporates a full configured test data base (stored in memory), setup with all tables (see class <code>DbSetup</code>). The object
 * <code>this.memoryDbSetup</code> can be used to run SQL against the data base to check the effects of REST calls, e.g. after calling a service to add a new
 * program to the data base, the number of rows of the <code>PROGRAM</code> table should have increased by 1 and a select using the primary key of the
 * <code>PROGRAM</code> table (name,owner,robot) in the <code>where</code> clause should return a matching single row. The core setup of the database is run
 * <b>once</b> for a JVM, i.e. it cannot be repeated. This is also true for tests from different classes using the same Junit runner. If tests need an
 * <b>empty</b> data base, the have to start with a call to <code>this.memoryDbSetup.deleteAllFromUserAndProgram()</code> <br>
 * The following conventions for REST calls should be used:<br>
 * - check the preconditions (typically using SQL),<br>
 * - call the REST service,<br>
 * - check the response object,<br>
 * - check the postconditions (typically using SQL).<br>
 * <br>
 * - to avoid code repetition, use simple wrappers for REST calls,<br>
 * - use the <code>this.sessionFactoryWrapper</code> object to create data base sessions and<br>
 * - save the response into this.response - use the two http sessions this.sPid and this.sMinscha for stories executed by the users "pid" and "minscha"
 *
 * @author rbudde
 */
public class RestInterfaceTest {

    private SessionFactoryWrapper sessionFactoryWrapper; // used by REST services to retrieve data base sessions
    private DbSetup memoryDbSetup; // use to query the test data base, change the data base at will, etc.

    private Response response; // store all REST responses here
    private HttpSessionState sPid; // reference user 1 is "pid"
    private HttpSessionState sMinscha; // reference user 2 is "minscha"

    // objects for specialized user stories
    private String connectionUrl;

    private RobotCommunicator brickCommunicator;

    private ClientUser restUser;
    private ClientProgram restProgram;

    private ClientAdmin restBlocks;
    private RobotDownloadProgram downloadJar;
    private RobotCommand brickCommand;

    @Before
    public void setup() throws Exception {
        Properties robertaProperties = Util1.loadProperties(null);
        RobertaProperties.setRobertaProperties(robertaProperties);

        this.connectionUrl = "jdbc:hsqldb:mem:performanceInMemoryDb";
        this.brickCommunicator = new RobotCommunicator();
        this.restUser = new ClientUser(this.brickCommunicator, null);
        this.restBlocks = new ClientAdmin(this.brickCommunicator);
        this.downloadJar = new RobotDownloadProgram(this.brickCommunicator);
        this.brickCommand = new RobotCommand(this.brickCommunicator);

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, this.brickCommunicator);
        Map<String, IRobotFactory> robotPlugins = new HashMap<>();
        loadPlugin(robotPlugins);
        this.sPid = HttpSessionState.init(this.brickCommunicator, robotPlugins, 1);
        this.sMinscha = HttpSessionState.init(this.brickCommunicator, robotPlugins, 2);
    }

    /**
     * Each method call of this test method is a separate test testing separate components of the server. The methods have to be called in sequence, because
     * they depend on database state (as: two user exist, etc), which is created by methods called before.
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
        createTwoUsers();
        updateUser();
        changeUserPassword();
        loginLogoutPid();
        pidCreateAndUpdate4Programs();
        minschaCreate2Programs();
        pidSharesProgramsMinschaCanAccessRW();
        pidDeletesProgramsMinschaCannotAccess();
        pidSharesProgram1MinschaCanDeleteTheShare();
        pidAndMinschaAccessConcurrently();

        // "pid" registers the robot with token "garzi" (and optionally many more ...); runs "p1"
        // registerToken(this.brickCommand, this.restBlocks, this.s1, this.sessionFactoryWrapper.getSession(), "garzi");
        // TODO: refactor downloadJar(this.downloadJar, this.restProgram, this.s1, "garzi", "p1");
    }

    /**
     * create two user:<br>
     * <b>PRE:</b> no user exists<br>
     * <b>POST:</b> two user exist, no user has logged in
     * <ul>
     * <li>USER table empty; create user "pid" -> success; USER table has 1 row
     * <li>create "pid" a second time -> error; USER table has 1 row
     * <li>create second user "minscha" -> success; USER table has 2 rows
     * </ul>
     */
    private void createTwoUsers() throws Exception {
        {
            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
            Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'pid';'userName':'cavy';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}",
                "ok",
                Key.USER_CREATE_SUCCESS);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'pid';'userName':'administrator';'password':'dip';'userEmail':'cavy1@home';'role':'STUDENT'}",
                "error",
                Key.USER_CREATE_ERROR_NOT_SAVED_TO_DB);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'minscha';'userName':'cavy';'password':'12';'userEmail':'cavy2@home';'role':'STUDENT'}",
                "ok",
                Key.USER_CREATE_SUCCESS);
            Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        }
    }

    /**
     * update user:<br>
     * <b>PRE:</b> two user exist, no user has logged in<br>
     * <b>POST:</b> two user exist, no user has logged in
     * <ul>
     * <li>USER table has 2 rows
     * <li>update of user "minscha" fails, because the user is not logged in
     * <li>login with "minscha" -> success
     * <li>update user name of "minscha" -> fail
     * <li>user "minscha" logs out -> success
     * </ul>
     */
    private void updateUser() throws Exception {

        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
        restUser(
            this.sMinscha,
            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home';'role':'STUDENT'}",
            "error",
            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);

        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());

        restUser(
            this.sMinscha,
            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home';'role':'STUDENT'}",
            "error",
            Key.USER_ERROR_EMAIL_USED);

        restUser(this.sMinscha, "{'cmd':'getUser';'accountName':'minscha'}", "ok", Key.USER_GET_ONE_SUCCESS);
        this.response.getEntity().toString().contains("cavy1231");
        restUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());

    }

    /**
     * change user password:<br>
     * <b>PRE:</b> two user exists, no user has logged in<br>
     * <b>POST:</b> two user exist, no user has logged in
     * <ul>
     * <li>password change of user "minscha" fails, because the user is not logged in
     * <li>user "minscha" logs in -> success
     * <li>password change of user "minscha" fails, because the old password supplied is wrong
     * <li>password change of user "minscha" with valid data -> success
     * <li>user "minscha" logs out -> success
     * <li>user "minscha" logs in with the new password -> success
     * <li>password change of user "minscha" with valid data (change the password to the old value) -> success
     * <li>user "minscha" logs out -> success
     * </ul>
     */
    private void changeUserPassword() throws Exception {

        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
        restUser(
            this.sMinscha,
            "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12';'newPassword':'12345'}",
            "error",
            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);

        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        restUser(
            this.sMinscha,
            "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'123';'newPassword':'12345'}",
            "error",
            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);
        restUser(this.sMinscha, "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12';'newPassword':'12345'}", "ok", Key.USER_UPDATE_SUCCESS);

        restUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());

        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12345'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        restUser(this.sMinscha, "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12345';'newPassword':'12'}", "ok", Key.USER_UPDATE_SUCCESS);

        restUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
    }

    /**
     * test login and logout<br>
     * <b>PRE:</b> two user exist, no user has logged in<br>
     * <b>POST:</b> two user exist, both user have logged in
     * <ul>
     * <li>login as "pid", use wrong password -> ERROR; pid-session tells that nobody is logged in
     * <li>login as "pid", use right password -> success; pid-session remembers the login
     * <li>login as "minscha" using the same session as for "pid" -> ERROR; pid-session has remembered the first login
     * <li>logout -> success; pid-session tells that nobody is logged in
     * <li>login as "pid", use right password -> success; pid-session remembers the login
     * <li>login as "minscha", use right password -> success; minscha-session remembers the login
     * </ul>
     */
    private void loginLogoutPid() throws Exception {
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'wrong'}", "error", Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "error", Key.COMMAND_INVALID); // because pid already has logged in
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'21'}", "error", Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
    }

    /**
     * test store and update of programs for "pid"<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in<br>
     * <b>PRE:</b> no program exists<br>
     * <b>POST:</b> "pid" owns four programs
     * <ul>
     * <li>save 4 programs -> success; 4 programs found
     * <li>saveAs to an existing program -> error; program must not exist
     * <li>save to a not existing program -> error; program must exist
     * </ul>
     */
    private void pidCreateAndUpdate4Programs() throws Exception {
        // PRE
        int pidId = this.sPid.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));

        // store (saveAs) 4 programs and check the count in the db
        saveAs(this.sPid, pidId, "p1", "<program>.1.pid</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveAs(this.sPid, pidId, "p2", "<program>.2.pid</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveAs(this.sPid, pidId, "p3", "<program>.3.pid</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveAs(this.sPid, pidId, "p4", "<program>.4.pid</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        // update (save) program 2 and check the effect in the data base
        save(this.sPid, pidId, "p2", -1, "<program>.2.pid.updated</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
        Assert.assertTrue(program.contains(".2.pid.updated"));

        // check that 4 programs are stored, check their names
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());

        // check correct server behavior: (1) the program to save exists (2) the program in saveAs doesn't exist
        saveAs(this.sPid, pidId, "p4", "<program>.4.pid</program>", "error", Key.PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS);
        save(this.sPid, pidId, "p5", 0, "<program>.5.pid</program>", "error", Key.PROGRAM_SAVE_ERROR_PROGRAM_TO_UPDATE_NOT_FOUND);

        // POST
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
    }

    /**
     * test store and update of programs for "minscha"<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs<br>
     * <b>PRE:</b> "minscha" owns no programs<br>
     * <b>POST:</b> "minscha" owns two programs
     * <ul>
     * <li>store 2 programs and check the count in the db
     * <li>check the content of program 'p2' in the db
     * </ul>
     */
    private void minschaCreate2Programs() throws Exception {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));

        saveAs(this.sMinscha, minschaId, "p1", "<program>.1.minscha</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveAs(this.sMinscha, minschaId, "p2", "<program>.2.minscha</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + minschaId + " and NAME = 'p2'");
        Assert.assertTrue(program.contains(".2.minscha"));
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
    }

    /**
     * share a programm and access it<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs, "minscha" owns two programs<br>
     * <b>PRE:</b> "minscha" can access his two programs, nothing is shared<br>
     * <b>POST:</b> "minscha" can access his two programs and two shared programs
     * <ul>
     * <li>"pid" shares her programs "p2" R and "p3" W with "minscha"
     * <li>"minscha" cannot modify "p2", but can modify "p3"
     * </ul>
     */
    private void pidSharesProgramsMinschaCanAccessRW() throws Exception {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2']");
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));

        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p2';'userToShare':'minscha';'right':'READ'}", "ok", Key.ACCESS_RIGHT_CHANGED);
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p3';'userToShare':'minscha';'right':'WRITE'}", "ok", Key.ACCESS_RIGHT_CHANGED);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
        JSONArray programListing = assertProgramListingAsExpected(this.sMinscha, "['p1','p2', 'p2', 'p3']");
        boolean ownershipOk = false;
        for ( int i = 0; i < programListing.length(); i++ ) {
            JSONArray programInfo = programListing.getJSONArray(i);
            if ( programInfo.getString(0).equals("p3") ) {
                Assert.assertEquals("p3 is owned by pid", "pid", programInfo.getString(1));
                ownershipOk = true;
                break;
            }
        }
        Assert.assertTrue(ownershipOk);

        restProgram(this.sPid, "{'cmd':'loadP';'name':'p2';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p2';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p2';'owner':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.minscha"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p3';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".3.pid"));

        save(this.sMinscha, pidId, "p2", -1, "<program>.2.minscha.update</program>", "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
        String p2Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
        Assert.assertTrue(p2Text.contains(".2.pid.updated"));
        save(this.sMinscha, pidId, "p3", -1, "<program>.3.minscha.update</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        String p3Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p3'");
        Assert.assertTrue(p3Text.contains(".3.minscha.update"));

        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2', 'p2', 'p3']");
    }

    /**
     * deleting a program removes the share<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in<br>
     * <b>PRE:</b> "minscha" can access his two programs and two shared programs<br>
     * <b>POST:</b> "minscha" can access his program "p1" and no shared programs
     * <ul>
     * <li>"minscha" deletes his program "p2" -> success; the programm cannot be loaded anymor, the programm cannot be deleted a second time, "minscha"
     * continues to see a program "p2" (the program shared with "pid")
     * <li>"pid" deletes her programs "p2" and "p3" -> success; nothing shared anymore, the visible programs match, neither owner nor sharer can load a deleted
     * program
     * </ul>
     */
    private void pidDeletesProgramsMinschaCannotAccess() throws Exception {
        int pidId = this.sPid.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2','p2','p3']"); // p2 is from "pid"!

        restProgram(this.sMinscha, "{'cmd':'deleteP';'name':'p2'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p2';'owner':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'deleteP';'name':'p2'}", "error", Key.PROGRAM_DELETE_ERROR);
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2','p3']"); // p2 is from "pid"!

        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));
        restProgram(this.sPid, "{'cmd':'deleteP';'name':'p2'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restProgram(this.sPid, "{'cmd':'deleteP';'name':'p3'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1']");
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));

        restProgram(this.sPid, "{'cmd':'loadP';'name':'p2';'owner':'pid'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p2';'owner':'pid'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p2';'owner':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p3';'owner':'pid'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);

        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p3'"));
    }

    /**
     * it is possible to delete the share. This doesn't delete the program :-)<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns program "p4"<br>
     * <b>PRE:</b> "minscha" can access his program "p1" and no shared programs<br>
     * <b>POST:</b> "minscha" can access his program "p1" and no shared programs
     * <ul>
     * <li>"pid" shares program "p4" W with "minscha"
     * <li>"minscha" can write it
     * <li>"pid" does see the change<br>
     * <br>
     * <li>"minscha" can delete the share
     * <li>"minscha" cannot write it anymore
     * <li>the program continues to exist (for "pid"), but it vanishes from the program list of "minscha"
     * </ul>
     */
    private void pidSharesProgram1MinschaCanDeleteTheShare() throws Exception, JSONException {
        {
            int pidId = this.sPid.getUserId();
            int minschaId = this.sMinscha.getUserId();
            Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
            assertProgramListingAsExpected(this.sPid, "['p1','p4']");
            assertProgramListingAsExpected(this.sMinscha, "['p1']");

            restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'userToShare':'minscha';'right':'WRITE'}", "ok", Key.ACCESS_RIGHT_CHANGED);
            assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");

            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
            String p4Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4Text.contains(".4.pid"));
            restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid"));
            save(this.sMinscha, pidId, "p4", -1, "<program>.4.minscha.update</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
            restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));

            restProgram(this.sMinscha, "{'cmd':'shareDelete';'programName':'p4';'owner':'pid'}", "ok", Key.ACCESS_RIGHT_DELETED);
            save(this.sMinscha, pidId, "p4", -1, "<program>.5.minscha.fail</program>", "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
            restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
            assertProgramListingAsExpected(this.sMinscha, "['p1']");
            assertProgramListingAsExpected(this.sPid, "['p1','p4']");
            String p4TextUpd2 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd2.contains(".4.minscha.update"));
            Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
        }
    }

    /**
     * the lastChanged-timestamp is used to guarantee an optimistic locking: - "pid" shares program "p4" W with "minscha"
     * <ul>
     * <li>"pid" reads "p4" (with lastChanged == timestamp X1
     * <li>"minscha" reads "p4" (with the same lastChanged == timestamp X1
     * <li>"pid" can write (lastChanged becomes timestamp X2)
     * <li>"minscha" cannot write (because lastChanged changed :-)
     * <li>Vice versa: if "minscha" writes back before "pid" writes back, "pid"s write fails
     * </ul>
     */
    private void pidAndMinschaAccessConcurrently() throws Exception, JSONException {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1']");

        restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        save(this.sPid, pidId, "p4", -1, "<program>.4.pId</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'userToShare':'minscha';'right':'WRITE'}", "ok", Key.ACCESS_RIGHT_CHANGED);
        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");

        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p1'"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p4'"));
        String programP4OfPid = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
        Assert.assertTrue(programP4OfPid.contains(".4.pId"));

        // scenario 1: minscha reads pid's p4, then he writes; pid doesn't use her program; the timestamp increases
        {
            restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pId"));
            long lastChanged1 = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
            save(this.sMinscha, pidId, "p4", lastChanged1, "<program>.4.minscha.update</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
            restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
            long lastChanged2 = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
            Assert.assertTrue(lastChanged2 > lastChanged1);
        }
        final Key LOCK_ERROR = Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING;
        // scenario 2: minscha reads pid's p4, then pid reads her p4; pid stores her program, but minscha can't (his timestamp is outdated)
        {
            restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long minschaReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
            restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long pidReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
            save(this.sPid, pidId, "p4", pidReadTimestamp, "<program>.4.pid.concurrentOk</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
            restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
            save(this.sMinscha, pidId, "p4", minschaReadTimestamp, "<program>.4.minscha.concurrentFail</program>", "error", LOCK_ERROR);
            restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(program.contains(".4.pid.concurrentOk"));
        }
        // scenario 3: minscha reads pid's p4, then pid reads her p4; minscha stores the shared program, but pid can't (her timestamp is outdated)
        {
            restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long minschaReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
            restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long pidReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
            save(this.sMinscha, pidId, "p4", minschaReadTimestamp, "<program>.4.minscha.concurrentOk</program>", "ok", Key.PROGRAM_SAVE_SUCCESS);
            restProgram(this.sMinscha, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
            save(this.sPid, pidId, "p4", pidReadTimestamp, "<program>.4.pid.concurrentFail</program>", "error", LOCK_ERROR);
            restProgram(this.sPid, "{'cmd':'loadP';'name':'p4';'owner':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(program.contains(".4.minscha.concurrentOk"));
        }
    }

    // small helpers
    private static final boolean _____helper_start_____ = true;

    /**
     * call a REST service for user-related commands. Store the response into <code>this.response</code>. Check whether the expected result and the expected
     * message key are found
     *
     * @param httpSession the session on which behalf the call is executed
     * @param jsonAsString the command (will be parsed to a JSON object)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void restUser(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws Exception {
        this.response = this.restUser.command(httpSession, this.sessionFactoryWrapper.getSession(), JSONUtilForServer.mkD(jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call a REST service for program-related commands. Store the response into <code>this.response</code>. Check whether the expected result and the expected
     * message key are found
     *
     * @param httpSession the session on which behalf the call is executed
     * @param jsonAsString the command (will be parsed to a JSON object)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void restProgram(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws JSONException, Exception {
        this.response = this.restProgram.command(httpSession, JSONUtilForServer.mkD(jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call the REST service responsible for storing NEW programs into the data base ("saveAs"). NEW programs are never shared ... .
     *
     * @param httpSession the session on which behalf the call is executed
     * @param owner the id of the owner of the program
     * @param name the name of the program
     * @param program the program text (XML)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void saveAs(HttpSessionState httpSession, int owner, String name, String program, String result, Key msgOpt) throws Exception //
    {
        String jsonAsString = "{'cmd':'saveAsP';'name':'" + name + "';'program':'" + program + "'}";
        this.response = this.restProgram.command(httpSession, JSONUtilForServer.mkD(jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call the REST service responsible for UPDATING programs in the data base (save). The program may be shared ... .
     *
     * @param httpSession the session on which behalf the call is executed
     * @param owner the id of the owner of the program
     * @param name the name of the program
     * @param timestamp the last changed timestamp. If the timestamp is -1, for convenience, it is read from the database.
     * @param program the program text (XML)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void save(HttpSessionState httpSession, int owner, String name, long timestamp, String program, String result, Key msgOpt) throws Exception //
    {
        boolean shared = httpSession.getUserId() != owner;
        if ( timestamp == -1 ) {
            // for update, first the timestamp is retrieved as it has to be shown to the persister (optimistic locking :-)
            Timestamp changed = this.memoryDbSetup.getOne("select LAST_CHANGED from PROGRAM where OWNER_ID = " + owner + " and NAME = '" + name + "'");
            timestamp = changed.getTime();
        }
        String jsonAsString = "{'cmd':'saveP';'shared':" + shared + ";'name':'" + name + "';'timestamp':" + timestamp + ";'program':'" + program + "'}";
        this.response = this.restProgram.command(httpSession, JSONUtilForServer.mkD(jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    private JSONArray assertProgramListingAsExpected(HttpSessionState session, String expectedProgramNamesAsJson) throws Exception, JSONException {
        this.response = this.restProgram.command(session, JSONUtilForServer.mkD("{'cmd':'loadPN'}"));
        JSONUtilForServer.assertEntityRc(this.response, "ok", Key.PROGRAM_GET_ALL_SUCCESS);
        JSONArray programListing = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        JSONUtilForServer.assertJsonEquals(expectedProgramNamesAsJson, programNames, false);
        return programListing;
    }

    private void loadPlugin(Map<String, IRobotFactory> robotPlugins) {
        try {
            @SuppressWarnings("unchecked")
            Class<IRobotFactory> factoryClass = (Class<IRobotFactory>) ServerStarter.class.getClassLoader().loadClass("de.fhg.iais.roberta.factory.EV3Factory");
            Constructor<IRobotFactory> factoryConstructor = factoryClass.getDeclaredConstructor(RobotCommunicator.class);
            robotPlugins.put("ev3", factoryConstructor.newInstance(this.brickCommunicator));
        } catch ( Exception e ) {
            throw new DbcException("robot plugin ev3 has an invalid factory. Check the properties. Server does NOT start", e);
        }
    }
}