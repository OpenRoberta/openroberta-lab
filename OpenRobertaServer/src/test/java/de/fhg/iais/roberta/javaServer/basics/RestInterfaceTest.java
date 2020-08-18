package de.fhg.iais.roberta.javaServer.basics;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientConfiguration;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientProgramController;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientUser;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.XsltTransformer;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * <b>Testing the REST interface of the OpenRoberta server</b><br>
 * <br>
 * The tests in this class are essentially <b>integration tests</b> (in spite of the fact, that the front end is <i>not</i> tested. But as the server is called
 * by small encapsulated REST calls issued from the front end, the tests in this class should be easy to understand and they are closely related to user stories
 * as <i>log in with a wrong password, then with the correct one, then create 4 programs and update one of them</i>. The more user stories are modeled in this
 * class, the more confident one can be, that the server is ok and errors visible in a browser will be in the Javascript front end only.<br>
 * <i>The ultimate aim of this is to reduce the debugging time of the front end considerably.</i><br>
 * <br>
 * This integration runs as a unit test (as the class name indicates :-). This is possible, because the database can be run perfectly in-memory and the server
 * is an embedded jetty, that can run without any installation (on CI server, for instance).<br>
 * <br>
 * The test class incorporates a full configured test data base (stored in memory), setup with all tables (see class <code>DbSetup</code>). The object
 * <code>this.memoryDbSetup</code> can be used to run SQL against the data base to check the effects of REST calls, e.g. after calling a service to add a new
 * program to the data base, the number of rows of the <code>PROGRAM</code> table should have increased by 1 and a select using the primary key of the
 * <code>PROGRAM</code> table (name,owner,robot) in the <code>where</code> clause should return a matching single row. The core setup of the database is run
 * <b>once</b> for a JVM, i.e. it cannot be repeated. This is also true for tests from different classes using the same Junit runner. If tests need an
 * <b>empty</b> data base, they have to start with a call to <code>this.memoryDbSetup.deleteAllFromUserAndProgram()</code> <br>
 * The following conventions for REST calls should be used:<br>
 * - check the preconditions (in most cases using SQL),<br>
 * - call the REST service,<br>
 * - check the response object,<br>
 * - check the postconditions (in most cases using SQL).<br>
 * <br>
 * - to avoid code repetition, use simple wrappers for REST calls,<br>
 * - use the <code>this.sessionFactoryWrapper</code> object to create data base sessions and<br>
 * - save the response into this.response - use the two http sessions this.sPid and this.sMinscha for stories executed by the users "pid" and "minscha"
 *
 * @author rbudde
 */
public class RestInterfaceTest {
    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();

    private static final String PROG_PRE =
        "<block_set xmlns=\\\"http://de.fhg.iais.roberta.blockly\\\" robottype=\\\"ev3\\\" xmlversion=\\\"2.0\\\" description=\\\"\\\" tags=\\\"\\\">"
            + "<instance x=\\\"512\\\" y=\\\"50\\\">"
            + "<block type=\\\"robControls_start\\\" id=\\\"RDF[XZ?y7bn;Z{?V}Q)(\\\" intask=\\\"true\\\" deletable=\\\"false\\\">"
            + "<mutation declare=\\\"false\\\"></mutation>"
            + "<field name=\\\"DEBUG\\\">FALSE</field>"
            + "<comment w=\\\"0\\\" h=\\\"0\\\" pinned=\\\"false\\\">";
    private static final String PROG_POST = "</comment>" + "</block></instance></block_set>";
    private static final String CONF_PRE =
        "<block_set xmlns=\\\"http://de.fhg.iais.roberta.blockly\\\" robottype=\\\"ev3\\\" xmlversion=\\\"2.0\\\" description=\\\"\\\" tags=\\\"\\\">"
            + "<instance x=\\\"213\\\" y=\\\"213\\\">"
            + "<block type=\\\"robBrick_EV3-Brick\\\" id=\\\"1\\\" intask=\\\"true\\\" deletable=\\\"false\\\">"
            + "<comment w=\\\"0\\\" h=\\\"0\\\" pinned=\\\"false\\\">";
    private static final String CONF_POST =
        "</comment>"
            + "<value name=\\\"S1\\\"><block type=\\\"robBrick_touch\\\" id=\\\"2\\\" intask=\\\"true\\\"></block></value>"
            + "</block></instance></block_set>";

    private SessionFactoryWrapper sessionFactoryWrapper; // used by REST services to retrieve data base sessions
    private XsltTransformer xsltTransformer;
    private DbSetup memoryDbSetup; // use to query the test data base, etc.

    private Response response; // store a REST response here
    private HttpSessionState sPid; // reference user 1 is "pid"
    private HttpSessionState sMinscha; // reference user 2 is "minscha"

    // objects for specialized user stories
    private String connectionUrl;

    private RobotCommunicator robotCommunicator;

    private ServerProperties serverProperties;
    private ClientUser restUser;
    private ClientProgramController restProject;
    private ClientConfiguration restConfiguration;

    @BeforeClass
    public static void setupClass() throws Exception {
        ServerStarter.initLoggingBeforeFirstUse(new String[0]);
    }

    @Before
    public void setup() throws Exception {
        this.serverProperties = new ServerProperties(Util.loadProperties(null));
        this.serverProperties.getserverProperties().put("server.public", "true"); // not dangerous! For this.restUser the mail management is set to null

        this.robotCommunicator = new RobotCommunicator();
        this.restUser = new ClientUser(this.robotCommunicator, serverProperties, null);

        TestConfiguration tc = TestConfiguration.setup();
        this.sessionFactoryWrapper = tc.getSessionFactoryWrapper();
        this.xsltTransformer = new XsltTransformer();
        this.memoryDbSetup = tc.getMemoryDbSetup();

        this.restProject = new ClientProgramController(this.serverProperties);
        this.restConfiguration = new ClientConfiguration(this.robotCommunicator);
        Map<String, IRobotFactory> robotPlugins = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
        this.sPid = HttpSessionState.initOnlyLegalForDebugging("pid", robotPlugins, serverProperties, 1);
        this.sMinscha = HttpSessionState.initOnlyLegalForDebugging("minscha", robotPlugins, serverProperties, 2);
    }

    /**
     * Each method call of this test method is a separate test testing separate components of the server. The methods have to be called in sequence, because
     * they depend on database state (e.g.: login expects an existing user, which is created by methods called before).
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
        createTwoUsers();
        activateUser();
        updateUser();
        changeUserPassword();
        loginLogoutPid();
        pidCreatesAndUpdates4Programs();
        minschaCreates1ConfAnd2Programs();
        pidSharesProgramsMinschaCanAccessRW();
        pidDeletesProgramsMinschaCannotAccess();
        pidSharesProgram1MinschaCanDeleteTheShare();
        pidAndMinschaAccessConcurrently();
        saveProgramsAndConfigurations();
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
                "{'cmd':'createUser';'accountName':'pid';'userName':'cavy';'password':'dip';'userEmail':'cavy1@home';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
                "error",
                Key.USER_ACTIVATION_SENT_MAIL_FAIL);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'pid';'userName':'secondTry';'password':'dip';'userEmail':'cavy1@home';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
                "error",
                Key.USER_CREATE_ERROR_NOT_SAVED_TO_DB);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            restUser(
                this.sPid,
                "{'cmd':'createUser';'accountName':'minscha';'userName':'cavy';'password':'12';'userEmail':'';'role':'STUDENT', 'isYoungerThen14': true, 'language': 'de'}",
                "ok",
                Key.USER_CREATE_SUCCESS);
            Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        }
    }

    /**
     * activate user account:<br>
     * <b>PRE:</b> two user exists with no activated accounts<br>
     * <b>POST:</b> one user with activated account, no user has logged in
     * <ul>
     * </ul>
     */
    private void activateUser() throws Exception {
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PENDING_EMAIL_CONFIRMATIONS"));

        String url = this.memoryDbSetup.getOne("select URL_POSTFIX from PENDING_EMAIL_CONFIRMATIONS").toString();
        restUser(this.sPid, "{'cmd':'activateUser'; 'userActivationLink': '" + url + "';}", "ok", Key.USER_ACTIVATION_SUCCESS);
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PENDING_EMAIL_CONFIRMATIONS"));
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
            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
            "error",
            Key.USER_ERROR_NOT_LOGGED_IN);

        restUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12' }", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());

        restUser(
            this.sMinscha,
            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
            "ok",
            Key.USER_DEACTIVATION_SUCCESS);

        restUser(this.sMinscha, "{'cmd':'getUser'}", "ok", Key.USER_GET_ONE_SUCCESS);
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
    private void pidCreatesAndUpdates4Programs() throws Exception {
        // PRE
        int pidId = this.sPid.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));

        // saveAs 4 programs and check the count in the db
        saveProgramAs(this.sPid, "pid", "pid", "p1", ".1.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sPid, "pid", "pid", "p2", ".2.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sPid, "pid", "pid", "p3", ".3.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sPid, "pid", "pid", "p4", ".4.pid", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        // update (save) program 2 and check the effect in the data base
        saveProgram(this.sPid, pidId, "pid", -1, "p2", ".2.pid.updated", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
        Assert.assertTrue(program.contains(".2.pid.updated"));

        // check that 4 programs are stored, check their names
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());

        // check correct server behavior: (1) the program to save exists (2) the program in saveAs doesn't exist
        saveProgramAs(this.sPid, "pid", "pid", "p4", ".4.pid", null, null, "error", Key.PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS);
        saveProgram(this.sPid, pidId, "pid", 0, "p5", ".5.pid", null, null, "error", Key.PROGRAM_SAVE_ERROR_PROGRAM_TO_UPDATE_NOT_FOUND);

        // POST
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
    }

    /**
     * test store and update of programs for "minscha"<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs<br>
     * <b>PRE:</b> "minscha" owns no programs and no configurations<br>
     * <b>POST:</b> "minscha" owns two programs and one configuration attached to both programs
     * <ul>
     * <li>store a named configuration "c1" and check the db
     * <li>store two programs "p1" and "p2" with default configuration and check the db
     * <li>change the configuration of the programs to "c1" and check the db
     * <li>change the configuration "c1" and check whether the other program is updated implicitly
     * <li>change the configuration of one program to anonymous and check, that this has no effect to the other
     * </ul>
     */
    private void minschaCreates1ConfAnd2Programs() throws Exception {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));

        saveConfigAs(this.sMinscha, minschaId, "c1", "c1.1.conf.minscha", "ok", Key.CONFIGURATION_SAVE_SUCCESS);
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
        String getConfigSql =
            "select d.CONFIGURATION_TEXT from CONFIGURATION c, CONFIGURATION_DATA d where c.CONFIGURATION_HASH = d.CONFIGURATION_HASH and OWNER_ID = "
                + minschaId
                + " and NAME = 'c1'";
        String config = this.memoryDbSetup.getOne(getConfigSql);
        Assert.assertTrue(config.contains("c1.1.conf.minscha"));
        saveConfig(this.sMinscha, minschaId, "c1", CONF_PRE + "c1.2.conf.minscha" + CONF_POST, "ok", Key.CONFIGURATION_SAVE_SUCCESS);
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
        config = this.memoryDbSetup.getOne(getConfigSql);
        Assert.assertTrue(config.contains("c1.2.conf.minscha"));

        saveProgramAs(this.sMinscha, "minscha", "minscha", "p1", ".1.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sMinscha, "minscha", "minscha", "p2", ".2.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));

        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        JSONObject responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertTrue(responseJson.has("confXML"));
        saveProgram(this.sMinscha, minschaId, "minscha", -1, "p1", ".1.1.minscha", "c1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertEquals("c1", responseJson.getString("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));
        saveProgram(this.sMinscha, minschaId, "minscha", -1, "p2", "p2.2.1.minscha", "c1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertEquals("c1", responseJson.getString("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));
        saveProgram(
            this.sMinscha,
            minschaId,
            "minscha",
            -1,
            "p1",
            ".1.2.minscha",
            null,
            CONF_PRE + "p1.3.conf.minscha" + CONF_POST,
            "ok",
            Key.PROGRAM_SAVE_SUCCESS);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertFalse(responseJson.has("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("p1.3.conf.minscha"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        responseJson = new JSONObject((String) this.response.getEntity());
        Assert.assertEquals("c1", responseJson.getString("configName"));
        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));

        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + minschaId + " and NAME = 'p2'");
        Assert.assertTrue(program.contains("p2.2.1.minscha"));

        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
    }

    /**
     * activate user account<br>
     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs<br>
     * <b>PRE:</b> "minscha" owns no programs<br>
     * <b>POST:</b> "minscha" owns two programs
     * <ul>
     * <li>store 2 programs and check the count in the db
     * <li>check the content of program 'p2' in the db
     * </ul>
     */
    private void activateUserAccount() throws Exception {
        int pidId = this.sPid.getUserId();
        int minschaId = this.sMinscha.getUserId();
        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));

        saveProgramAs(this.sMinscha, "minscha", "minscha", "p1", ".1.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        saveProgramAs(this.sMinscha, "minscha", "minscha", "p2", ".2.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
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

        String shareRead = "{'type':'User';'label':'minscha';'right':'READ'}";
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p2';'shareData':" + shareRead + "}", "ok", Key.ACCESS_RIGHT_CHANGED);
        String shareWrite = "{'type':'User';'label':'minscha';'right':'WRITE'}";
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p3';'shareData':" + shareWrite + "}", "ok", Key.ACCESS_RIGHT_CHANGED);
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

        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".2.1.minscha"));
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p3';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains(".3.pid"));

        saveProgram(this.sMinscha, pidId, "pid", -1, "p2", ".2.minscha.update", null, null, "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
        String p2Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
        Assert.assertTrue(p2Text.contains(".2.pid.updated"));
        saveProgram(this.sMinscha, pidId, "pid", -1, "p3", ".3.minscha.update", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
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

        restProgram(this.sMinscha, "{'cmd':'deleteP';'programName':'p2';'author':'minscha'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restProgram(
            this.sMinscha,
            "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'robot':'ev3';'author':'minscha'}",
            "error",
            Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'deleteP';'programName':'p2';'author':'minscha'}", "error", Key.PROGRAM_DELETE_ERROR);
        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p2','p3']"); // p2 is from "pid"!

        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));
        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p2';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p3';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1']");
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));

        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p3';'owner':'pid';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);

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

            String shareWrite = "{'type':'User';'label':'minscha';'right':'WRITE'}";
            restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'shareData':" + shareWrite + "}", "ok", Key.ACCESS_RIGHT_CHANGED);
            assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");

            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
            String p4Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4Text.contains(".4.pid"));
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid"));
            saveProgram(this.sMinscha, pidId, "pid", -1, "p4", ".4.minscha.update", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));

            restProgram(this.sMinscha, "{'cmd':'shareDelete';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.ACCESS_RIGHT_DELETED);
            saveProgram(this.sMinscha, pidId, "pid", -1, "p4", ".5.minscha.fail", null, null, "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
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

        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
        saveProgram(this.sPid, pidId, "pid", -1, "p4", ".4.pId", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
        String shareWrite = "{'type':'User';'label':'minscha';'right':'WRITE'}";
        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'shareData':" + shareWrite + "}", "ok", Key.ACCESS_RIGHT_CHANGED);

        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
        assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");

        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p1'"));
        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p4'"));
        String programP4OfPid = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
        Assert.assertTrue(programP4OfPid.contains(".4.pId"));

        // scenario 1: minscha reads pid's p4, then he writes; pid doesn't use her program; the timestamp increases
        {
            Thread.sleep(500); // REST-call should be executed sequentially. The sleep is NO guaranty ... Otherwise see below!
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pId"));
            long lastChanged1 = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            saveProgram(this.sMinscha, pidId, "pid", lastChanged1, "p4", ".4.minscha.update", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
            Thread.sleep(500); // REST-call should be executed sequentially. The sleep is NO guaranty ... Otherwise see below!
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
            long lastChanged2 = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            Assert.assertTrue("causality violated: changed2 must be later than changed1", lastChanged2 > lastChanged1); // here sometimes a time race occurs. This may generate a test error.
        }
        final Key LOCK_ERROR = Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING;
        // scenario 2: minscha reads pid's p4, then pid reads her p4; pid stores her program, but minscha can't (his timestamp is outdated)
        {
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long minschaReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long pidReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
            saveProgram(this.sPid, pidId, "pid", pidReadTimestamp, "p4", ".4.pid.concurrentOk", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
            saveProgram(this.sMinscha, pidId, "pid", minschaReadTimestamp, "p4", ".4.minscha.concurrentFail", null, null, "error", LOCK_ERROR);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(program.contains(".4.pid.concurrentOk"));
        }
        // scenario 3: minscha reads pid's p4, then pid reads her p4; minscha stores the shared program, but pid can't (her timestamp is outdated)
        {
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long minschaReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            long pidReadTimestamp = new JSONObject((String) this.response.getEntity()).getLong("lastChanged");
            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
            saveProgram(this.sMinscha, pidId, "pid", minschaReadTimestamp, "p4", ".4.minscha.concurrentOk", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
            saveProgram(this.sPid, pidId, "pid", pidReadTimestamp, "p4", ".4.pid.concurrentFail", null, null, "error", LOCK_ERROR);
            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
            Assert.assertTrue(program.contains(".4.minscha.concurrentOk"));
        }
    }

    /**
     * check, that the relationship between programs and configurations works<br>
     * <br>
     * 1. save a new config. Check, that a name and data is required. Check, that the name doesn't exist. Check, that the data is ok.<br>
     * 2. save a new program with default configuration, anonymous configuration and named configuration. Check, that the data is ok.<br>
     */
    private void saveProgramsAndConfigurations() throws Exception {
        int minschaId = this.sMinscha.getUserId();

        {
            saveConfigAs(this.sMinscha, minschaId, "mc1", "mc1.1.conf.minscha", "ok", Key.CONFIGURATION_SAVE_SUCCESS);
            saveConfigAs(this.sMinscha, minschaId, null, "mc1.1.conf.minscha", "error", Key.SERVER_ERROR);
            saveConfigAs(this.sMinscha, minschaId, "mc2", null, "error", Key.SERVER_ERROR);
            saveConfigAs(this.sMinscha, minschaId, "mc1", "mc1.2.conf.minscha", "error", Key.CONFIGURATION_SAVE_ERROR);
            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where NAME like 'mc%'"));
            String confHash = this.memoryDbSetup.getOne("select CONFIGURATION_HASH from CONFIGURATION where NAME = 'mc1'");
            String confText = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + confHash + "'");
            Assert.assertTrue(confText.contains("mc1.1.conf.minscha"));
        }

        {
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp1", "mp1.minscha", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp2", "mp2.minscha", null, "mp2.conf.minscha", "ok", Key.PROGRAM_SAVE_SUCCESS);
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp3", "mp3.minscha", "mc1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
            saveProgramAs(this.sMinscha, "minscha", "minscha", "mp4", "mp3.minscha", "mc1", "mp2.conf.minscha", "error", Key.SERVER_ERROR);
            Assert.assertEquals(3, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where NAME like 'mp%'"));
            String cnMp1 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp1'");
            String chMp1 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp1'");
            Assert.assertTrue(cnMp1 == null && chMp1 == null);
            String cnMp2 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp2'");
            String chMp2 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp2'");
            Assert.assertTrue(cnMp2 == null && chMp2 != null);
            String ctMp2 = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + chMp2 + "'");
            Assert.assertTrue(ctMp2 != null);
            String cnMp3 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp3'");
            String chMp3 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp3'");
            Assert.assertTrue(cnMp3 != null && chMp3 == null);
            String chMc1 = this.memoryDbSetup.getOne("select CONFIGURATION_HASH from CONFIGURATION where NAME = 'mc1'");
            String ctMc1 = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + chMc1 + "'");
            Assert.assertTrue(ctMc1.contains("mc1.1.conf.minscha"));
        }
    }

    // small helpers
    @SuppressWarnings("unused")
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
        JSONObject jsonCmd = JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString);
        FullRestRequest cmd = FullRestRequest.make(jsonCmd);
        switch ( jsonCmd.getJSONObject("data").getString("cmd") ) {
            case "activateUser":
                this.response = this.restUser.activateUser(newDbSession(), cmd);
                break;
            case "changePassword":
                this.response = this.restUser.changePassword(newDbSession(), cmd);
                break;
            case "login":
                this.response = this.restUser.login(newDbSession(), cmd);
                break;
            case "logout":
                this.response = this.restUser.logout(cmd);
                break;
            case "createUser":
                this.response = this.restUser.createUser(newDbSession(), cmd);
                break;
            case "updateUser":
                this.response = this.restUser.updateUser(newDbSession(), cmd);
                break;
            case "getUser":
                this.response = this.restUser.getUser(newDbSession(), cmd);
                break;
            default:
                throw new DbcException("Unexpected JSON command");
        }
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
        if ( jsonAsString.contains("loadP") ) {
            this.response = this.restProject.getProgram(newDbSession(), this.xsltTransformer, mkFRR(httpSession.getInitToken(), jsonAsString));
        } else if ( jsonAsString.contains("shareP") ) {
            this.response = this.restProject.shareProgram(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        } else if ( jsonAsString.contains("deleteP") ) {
            this.response = this.restProject.deleteProgram(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        } else if ( jsonAsString.contains("shareDelete") ) {
            this.response = this.restProject.deleteSharedProgram(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        } else {
            throw new DbcException("Unexpected JSON command");
        }
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    private FullRestRequest mkFRR(String initToken, String jsonAsString) {
        JSONObject cmdAsJson = JSONUtilForServer.mkD(initToken, jsonAsString);
        return FullRestRequest.make(cmdAsJson);
    }

    /**
     * call the REST service responsible for storing NEW programs into the data base ("saveAsP").
     *
     * @param httpSession the session on which behalf the call is executed
     * @param owner the id of the owner of the program
     * @param progName the name of the program
     * @param progXml the program text
     * @param confName the name of the configuration; null, if an anonymous configuration is used
     * @param confText the configuration text (XML); null, if the default configuration is used
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     */
    private void saveProgramAs(
        HttpSessionState httpSession,
        String ownerAccount,
        String authorAccount,
        String progName,
        String progXml,
        String confName,
        String confText,
        String result,
        Key msgOpt)
        throws Exception //
    {
        String jsonAsString = "{'cmd':'saveAsP';'programName':'" + progName + "';'progXML':'" + PROG_PRE + progXml + PROG_POST + "'";
        if ( confName != null ) {
            jsonAsString += ";'configName':'" + confName + "'";
        }
        if ( confText != null ) {
            jsonAsString += ";'confXML':'" + confText + "'";
        }
        jsonAsString += ";'ownerAccount':'" + ownerAccount + "'}";
        this.response = this.restProject.saveProgram(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call the REST service responsible for UPDATING programs in the data base (save). The program may be shared ... .
     *
     * @param httpSession the session on which behalf the call is executed
     * @param ownerId the id of the user logged in
     * @param ownerAccount the account of the owner of the program, maybe the logged in user, maybe a sharer
     * @param timestamp the last changed timestamp. If the timestamp is -1, for convenience, it is read from the database.
     * @param progName the name of the program
     * @param progXml the program text (XML)
     * @param confName the name of the configuration; null, if an anonymous configuration is used
     * @param confXml the configuration text (XML); null, if the default configuration is used
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void saveProgram(
        HttpSessionState httpSession,
        int ownerId,
        String ownerAccount,
        long timestamp,
        String progName,
        String progXml,
        String confName,
        String confXml,
        String result,
        Key msgOpt)
        throws Exception //
    {
        if ( timestamp == -1 ) {
            // for update, first the timestamp is retrieved as it has to be shown to the persister (optimistic locking :-)
            Timestamp changed = this.memoryDbSetup.getOne("select LAST_CHANGED from PROGRAM where OWNER_ID = " + ownerId + " and NAME = '" + progName + "'");
            timestamp = changed.getTime();
        }
        String jsonAsString = "{'cmd':'save';'ownerAccount':'" + ownerAccount + "';";
        jsonAsString += "'programName':'" + progName + "';'timestamp':" + timestamp + ";'progXML':'" + PROG_PRE + progXml + PROG_POST + "'";
        if ( confName != null ) {
            jsonAsString += ";'configName':'" + confName + "'";
        }
        if ( confXml != null ) {
            jsonAsString += ";'confXML':'" + confXml + "'";
        }
        jsonAsString += ";}";
        this.response = this.restProject.saveProgram(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call the REST service responsible for storing NEW configutations into the data base ("saveAsC").
     *
     * @param httpSession the session on which behalf the call is executed
     * @param owner the id of the owner of the program
     * @param configName the name of the configuration
     * @param configText the configuration text (XML)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void saveConfigAs(HttpSessionState httpSession, int owner, String configName, String configText, String result, Key msgOpt) throws Exception //
    {
        String jsonAsString = "{'cmd':'saveAsC'";
        if ( configName != null ) {
            jsonAsString += ";'name':'" + configName + "'";
        }
        if ( configText != null ) {
            jsonAsString += ";'configuration':'" + CONF_PRE + configText + CONF_POST + "'";
        }
        jsonAsString += ";}";
        this.response = this.restConfiguration.saveAsConfig(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call the REST service responsible for UPDATING existing configurations in the data base ("saveC").
     *
     * @param httpSession the session on which behalf the call is executed
     * @param owner the id of the owner of the program
     * @param name the name of the configuration
     * @param conf the configuration text (XML)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void saveConfig(HttpSessionState httpSession, int owner, String name, String configuration, String result, Key msgOpt) throws Exception //
    {
        String jsonAsString = "{'cmd':'saveC';'name':'" + name + "';'configuration':'" + configuration + "';}";
        this.response = this.restConfiguration.saveConfig(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    private JSONArray assertProgramListingAsExpected(HttpSessionState httpSession, String expectedProgramNamesAsJson) throws Exception, JSONException {
        this.response = this.restProject.getInfosOfProgramsOfLoggedInUser(newDbSession(), mkFRR(httpSession.getInitToken(), "{'cmd':'loadPN'}"));
        JSONUtilForServer.assertEntityRc(this.response, "ok", Key.PROGRAM_GET_ALL_SUCCESS);
        JSONArray programListing = new JSONObject((String) this.response.getEntity()).getJSONArray("programNames");
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        JSONUtilForServer.assertJsonEquals(expectedProgramNamesAsJson, programNames, false);
        return programListing;
    }

    private DbSession newDbSession() {
        return this.sessionFactoryWrapper.getSession();
    }
}