//package de.fhg.iais.roberta.javaServer.basics;
//
//import java.sql.Timestamp;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import javax.ws.rs.core.Response;
//
//import org.hibernate.Session;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import de.fhg.iais.roberta.factory.IRobotFactory;
//import de.fhg.iais.roberta.javaServer.restServices.all.ServerData;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.Alive;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientConfiguration;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientInit;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientPing;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientProgramController;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientToolbox;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientUser;
//import de.fhg.iais.roberta.javaServer.restServices.all.controller.ProjectWorkflowRestController;
//import de.fhg.iais.roberta.main.IpToCountryDefault;
//import de.fhg.iais.roberta.main.ServerStarter;
//import de.fhg.iais.roberta.persistence.util.DbSetup;
//import de.fhg.iais.roberta.persistence.util.HttpSessionState;
//import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
//import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
//import de.fhg.iais.roberta.testutil.JSONUtilForServer;
//import de.fhg.iais.roberta.util.Key;
//import de.fhg.iais.roberta.util.ServerProperties;
//import de.fhg.iais.roberta.util.Util;
//import de.fhg.iais.roberta.util.dbc.DbcException;
//
///**
// * Testing for db resource leaks when calling REST services<br>
// *
// * @author rbudde
// */
//public class RestInterfaceResourceUsageTest {
//    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
//
//    private SessionFactoryWrapper sessionFactoryWrapper; // used by REST services to retrieve data base sessions
//    private DbSetup memoryDbSetup; // use to query the test data base, etc.
//
//    private Response response; // store a REST response here
//    private HttpSessionState sPid; // reference user 1 is "pid"
//    private HttpSessionState sMinscha; // reference user 2 is "minscha"
//
//    // objects for specialized user stories
//    private String connectionUrl;
//
//    private RobotCommunicator robotCommunicator;
//    private ServerProperties serverProperties;
//
//    // REST services
//    private ServerData restServerData;
//    private Alive restAlive;
//    private ClientAdmin restAdmin;
//    private ClientConfiguration restConfiguration;
//    private ClientInit restInit;
//    private ClientPing restPing;
//    private ClientProgramController restProject;
//    private ClientToolbox restClientToolbox;
//    private ClientUser restUser;
//    private ProjectWorkflowRestController restWorkflow;
//
//    @BeforeClass
//    public static void setupClass() throws Exception {
//        ServerStarter.initLoggingBeforeFirstUse(new String[0]);
//    }
//
//    @Before
//    public void setup() throws Exception {
//        this.serverProperties = new ServerProperties(Util.loadProperties(null));
//        this.serverProperties.getserverProperties().put("server.public", "true"); // not dangerous! For this.restUser the mail management is set to null
//
//        this.connectionUrl = "jdbc:hsqldb:mem:restTestInMemoryDb";
//        this.robotCommunicator = new RobotCommunicator();
//
//        Map<String, IRobotFactory> robotPlugins = ServerStarter.configureRobotPlugins(robotCommunicator, serverProperties, EMPTY_STRING_LIST);
//        this.sPid = HttpSessionState.initOnlyLegalForDebugging("pid", robotPlugins, serverProperties, 1);
//        this.sMinscha = HttpSessionState.initOnlyLegalForDebugging("minscha", robotPlugins, serverProperties, 2);
//
//        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.connectionUrl);
//        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
//        this.memoryDbSetup = new DbSetup(nativeSession);
//        this.memoryDbSetup.createEmptyDatabase();
//
//        this.restServerData = new ServerData(this.robotCommunicator);
//        this.restAlive = new Alive();
//        this.restAdmin = new ClientAdmin(this.robotCommunicator, this.serverProperties);
//        this.restConfiguration = new ClientConfiguration(this.sessionFactoryWrapper, this.robotCommunicator);
//        this.restInit = new ClientInit(robotPlugins, this.robotCommunicator, this.serverProperties, new IpToCountryDefault());
//        this.restPing = new ClientPing("XXX", this.robotCommunicator);
//        this.restProject = new ClientProgramController(this.sessionFactoryWrapper, this.serverProperties);
//        this.restClientToolbox = new ClientToolbox(this.robotCommunicator);
//        this.restUser = new ClientUser(this.robotCommunicator, this.serverProperties, null);
//        this.restWorkflow = new ProjectWorkflowRestController(this.robotCommunicator);
//    }
//
//    @Test
//    public void test() throws Exception {
//        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
//        createTwoUsers();
//        activateUser();
//        updateUser();
//        changeUserPassword();
//        loginLogoutPid();
//        pidCreatesAndUpdates4Programs();
//        minschaCreates1ConfAnd2Programs();
//        pidSharesProgramsMinschaCanAccessRW();
//        pidDeletesProgramsMinschaCannotAccess();
//        pidSharesProgram1MinschaCanDeleteTheShare();
//        pidAndMinschaAccessConcurrently();
//        saveProgramsAndConfigurations();
//        Response response = serverData.tellUsageOfHttpAndDatabaseSessions();
//        System.out.println(response.getEntity().toString());
//    }
//
//    /**
//     * create two user:<br>
//     * <b>PRE:</b> no user exists<br>
//     * <b>POST:</b> two user exist, no user has logged in
//     * <ul>
//     * <li>USER table empty; create user "pid" -> success; USER table has 1 row
//     * <li>create "pid" a second time -> error; USER table has 1 row
//     * <li>create second user "minscha" -> success; USER table has 2 rows
//     * </ul>
//     */
//    private void createTwoUsers() throws Exception {
//        {
//            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//            Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
//            clientUser(
//                this.sPid,
//                "{'cmd':'createUser';'accountName':'pid';'userName':'cavy';'password':'dip';'userEmail':'cavy1@home';'role':'STUDENT', 'isYoungerThen14': 1, 'language': 'de'}",
//                "error",
//                Key.USER_ACTIVATION_SENT_MAIL_FAIL);
//            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
//            clientUser(
//                this.sPid,
//                "{'cmd':'createUser';'accountName':'pid';'userName':'secondTry';'password':'dip';'userEmail':'cavy1@home';'role':'STUDENT', 'isYoungerThen14': 'false', 'language': 'de'}",
//                "error",
//                Key.USER_CREATE_ERROR_NOT_SAVED_TO_DB);
//            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
//            clientUser(
//                this.sPid,
//                "{'cmd':'createUser';'accountName':'minscha';'userName':'cavy';'password':'12';'userEmail':'';'role':'STUDENT', 'isYoungerThen14': 'true', 'language': 'de'}",
//                "ok",
//                Key.USER_CREATE_SUCCESS);
//            Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
//            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        }
//    }
//
//    /**
//     * activate user account:<br>
//     * <b>PRE:</b> two user exists with no activated accounts<br>
//     * <b>POST:</b> one user with activated account, no user has logged in
//     * <ul>
//     * </ul>
//     */
//    private void activateUser() throws Exception {
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
//        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PENDING_EMAIL_CONFIRMATIONS"));
//
//        String url = this.memoryDbSetup.getOne("select URL_POSTFIX from PENDING_EMAIL_CONFIRMATIONS").toString();
//        clientUser(this.sPid, "{'cmd':'activateUser';'accountName':'pid'; 'userActivationLink': '" + url + "';}", "ok", Key.USER_ACTIVATION_SUCCESS);
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PENDING_EMAIL_CONFIRMATIONS"));
//    }
//
//    /**
//     * update user:<br>
//     * <b>PRE:</b> two user exist, no user has logged in<br>
//     * <b>POST:</b> two user exist, no user has logged in
//     * <ul>
//     * <li>USER table has 2 rows
//     * <li>update of user "minscha" fails, because the user is not logged in
//     * <li>login with "minscha" -> success
//     * <li>update user name of "minscha" -> fail
//     * <li>user "minscha" logs out -> success
//     * </ul>
//     */
//    private void updateUser() throws Exception {
//
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
//        clientUser(
//            this.sMinscha,
//            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
//            "error",
//            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);
//
//        clientUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12', 'isYoungerThen14': false}", "ok", Key.USER_GET_ONE_SUCCESS);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//
//        clientUser(
//            this.sMinscha,
//            "{'cmd':'updateUser';'accountName':'minscha';'userName':'cavy1231';'userEmail':'cavy@home';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
//            "ok",
//            Key.USER_DEACTIVATION_SUCCESS);
//
//        clientUser(this.sMinscha, "{'cmd':'getUser'}", "ok", Key.USER_GET_ONE_SUCCESS);
//        this.response.getEntity().toString().contains("cavy1231");
//        clientUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//
//    }
//
//    /**
//     * change user password:<br>
//     * <b>PRE:</b> two user exists, no user has logged in<br>
//     * <b>POST:</b> two user exist, no user has logged in
//     * <ul>
//     * <li>password change of user "minscha" fails, because the user is not logged in
//     * <li>user "minscha" logs in -> success
//     * <li>password change of user "minscha" fails, because the old password supplied is wrong
//     * <li>password change of user "minscha" with valid data -> success
//     * <li>user "minscha" logs out -> success
//     * <li>user "minscha" logs in with the new password -> success
//     * <li>password change of user "minscha" with valid data (change the password to the old value) -> success
//     * <li>user "minscha" logs out -> success
//     * </ul>
//     */
//    private void changeUserPassword() throws Exception {
//
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER"));
//        clientUser(
//            this.sMinscha,
//            "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12';'newPassword':'12345'}",
//            "error",
//            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);
//
//        clientUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        clientUser(
//            this.sMinscha,
//            "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'123';'newPassword':'12345'}",
//            "error",
//            Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB);
//        clientUser(this.sMinscha, "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12';'newPassword':'12345'}", "ok", Key.USER_UPDATE_SUCCESS);
//
//        clientUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//
//        clientUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12345'}", "ok", Key.USER_GET_ONE_SUCCESS);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        clientUser(this.sMinscha, "{'cmd':'changePassword';'accountName':'minscha';'oldPassword':'12345';'newPassword':'12'}", "ok", Key.USER_UPDATE_SUCCESS);
//
//        clientUser(this.sMinscha, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//    }
//
//    /**
//     * test login and logout<br>
//     * <b>PRE:</b> two user exist, no user has logged in<br>
//     * <b>POST:</b> two user exist, both user have logged in
//     * <ul>
//     * <li>login as "pid", use wrong password -> ERROR; pid-session tells that nobody is logged in
//     * <li>login as "pid", use right password -> success; pid-session remembers the login
//     * <li>login as "minscha" using the same session as for "pid" -> ERROR; pid-session has remembered the first login
//     * <li>logout -> success; pid-session tells that nobody is logged in
//     * <li>login as "pid", use right password -> success; pid-session remembers the login
//     * <li>login as "minscha", use right password -> success; minscha-session remembers the login
//     * </ul>
//     */
//    private void loginLogoutPid() throws Exception {
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        clientUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'wrong'}", "error", Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        clientUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        clientUser(this.sPid, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "error", Key.COMMAND_INVALID); // because pid already has logged in
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        clientUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'21'}", "error", Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        clientUser(this.sPid, "{'cmd':'logout'}", "ok", Key.USER_LOGOUT_SUCCESS);
//        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        clientUser(this.sPid, "{'cmd':'login';'accountName':'pid';'password':'dip'}", "ok", Key.USER_GET_ONE_SUCCESS);
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
//        clientUser(this.sMinscha, "{'cmd':'login';'accountName':'minscha';'password':'12'}", "ok", Key.USER_GET_ONE_SUCCESS);
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//    }
//
//    /**
//     * test store and update of programs for "pid"<br>
//     * <b>INVARIANT:</b> two user exist, both user have logged in<br>
//     * <b>PRE:</b> no program exists<br>
//     * <b>POST:</b> "pid" owns four programs
//     * <ul>
//     * <li>save 4 programs -> success; 4 programs found
//     * <li>saveAs to an existing program -> error; program must not exist
//     * <li>save to a not existing program -> error; program must exist
//     * </ul>
//     */
//    private void pidCreatesAndUpdates4Programs() throws Exception {
//        // PRE
//        int pidId = this.sPid.getUserId();
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//
//        // store (saveAs) 4 programs and check the count in the db
//        saveProgramAs(this.sPid, pidId, "p1", "<program>.1.pid</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        saveProgramAs(this.sPid, pidId, "p2", "<program>.2.pid</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        saveProgramAs(this.sPid, pidId, "p3", "<program>.3.pid</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        saveProgramAs(this.sPid, pidId, "p4", "<program>.4.pid</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
//
//        // update (save) program 2 and check the effect in the data base
//        saveProgram(this.sPid, pidId, -1, "p2", "<program>.2.pid.updated</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
//        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
//        Assert.assertTrue(program.contains(".2.pid.updated"));
//
//        // check that 4 programs are stored, check their names
//        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//
//        // check correct server behavior: (1) the program to save exists (2) the program in saveAs doesn't exist
//        saveProgramAs(this.sPid, pidId, "p4", "<program>.4.pid</program>", null, null, "error", Key.PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS);
//        saveProgram(this.sPid, pidId, 0, "p5", "<program>.5.pid</program>", null, null, "error", Key.PROGRAM_SAVE_ERROR_PROGRAM_TO_UPDATE_NOT_FOUND);
//
//        // POST
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
//    }
//
//    /**
//     * test store and update of programs for "minscha"<br>
//     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs<br>
//     * <b>PRE:</b> "minscha" owns no programs and no configurations<br>
//     * <b>POST:</b> "minscha" owns two programs and one configuration attached to both programs
//     * <ul>
//     * <li>store a named configuration "c1" and check the db
//     * <li>store two programs "p1" and "p2" with default configuration and check the db
//     * <li>change the configuration of the programs to "c1" and check the db
//     * <li>change the configuration "c1" and check whether the other program is updated implicitly
//     * <li>change the configuration of one program to anonymous and check, that this has no effect to the other
//     * </ul>
//     */
//    private void minschaCreates1ConfAnd2Programs() throws Exception {
//        int pidId = this.sPid.getUserId();
//        int minschaId = this.sMinscha.getUserId();
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//
//        saveConfigAs(this.sMinscha, minschaId, "c1", "<conf>c1.1.conf.minscha</conf>", "ok", Key.CONFIGURATION_SAVE_SUCCESS);
//        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
//        String config =
//            this.memoryDbSetup
//                .getOne(
//                    "select d.CONFIGURATION_TEXT from CONFIGURATION c, CONFIGURATION_DATA d where c.CONFIGURATION_HASH = d.CONFIGURATION_HASH and OWNER_ID = "
//                        + minschaId
//                        + " and NAME = 'c1'");
//        Assert.assertTrue(config.contains("c1.1.conf.minscha"));
//        saveConfig(this.sMinscha, minschaId, "c1", "<conf>c1.2.conf.minscha</conf>", "ok", Key.CONFIGURATION_SAVE_SUCCESS);
//        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where OWNER_ID = " + minschaId));
//        config =
//            this.memoryDbSetup
//                .getOne(
//                    "select d.CONFIGURATION_TEXT from CONFIGURATION c, CONFIGURATION_DATA d where c.CONFIGURATION_HASH = d.CONFIGURATION_HASH and OWNER_ID = "
//                        + minschaId
//                        + " and NAME = 'c1'");
//        Assert.assertTrue(config.contains("c1.2.conf.minscha"));
//
//        saveProgramAs(this.sMinscha, minschaId, "p1", "<program>.1.minscha</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        saveProgramAs(this.sMinscha, minschaId, "p2", "<program>.2.minscha</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
//
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        JSONObject responseJson = (JSONObject) this.response.getEntity();
//        Assert.assertFalse(responseJson.has("configName"));
//        Assert.assertFalse(responseJson.has("confXML"));
//        saveProgram(this.sMinscha, minschaId, -1, "p1", "<program>p1.1.1.minscha</program>", "c1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        responseJson = (JSONObject) this.response.getEntity();
//        Assert.assertEquals("c1", responseJson.getString("configName"));
//        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));
//        saveProgram(this.sMinscha, minschaId, -1, "p2", "<program>p2.2.1.minscha</program>", "c1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        responseJson = (JSONObject) this.response.getEntity();
//        Assert.assertEquals("c1", responseJson.getString("configName"));
//        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));
//        saveProgram(
//            this.sMinscha,
//            minschaId,
//            -1,
//            "p1",
//            "<program>.1.2.minscha</program>",
//            null,
//            "<conf>p1.3.conf.minscha</conf>",
//            "ok",
//            Key.PROGRAM_SAVE_SUCCESS);
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p1';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        responseJson = (JSONObject) this.response.getEntity();
//        Assert.assertFalse(responseJson.has("configName"));
//        Assert.assertTrue(responseJson.getString("confXML").contains("p1.3.conf.minscha"));
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        responseJson = (JSONObject) this.response.getEntity();
//        Assert.assertEquals("c1", responseJson.getString("configName"));
//        Assert.assertTrue(responseJson.getString("confXML").contains("c1.2.conf.minscha"));
//
//        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + minschaId + " and NAME = 'p2'");
//        Assert.assertTrue(program.contains("p2.2.1.minscha"));
//
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//    }
//
//    /**
//     * activate user account<br>
//     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs<br>
//     * <b>PRE:</b> "minscha" owns no programs<br>
//     * <b>POST:</b> "minscha" owns two programs
//     * <ul>
//     * <li>store 2 programs and check the count in the db
//     * <li>check the content of program 'p2' in the db
//     * </ul>
//     */
//    private void activateUserAccount() throws Exception {
//        int pidId = this.sPid.getUserId();
//        int minschaId = this.sMinscha.getUserId();
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//
//        saveProgramAs(this.sMinscha, minschaId, "p1", "<program>.1.minscha</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        saveProgramAs(this.sMinscha, minschaId, "p2", "<program>.2.minscha</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + minschaId));
//        Assert.assertEquals(4, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId));
//        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM"));
//
//        String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + minschaId + " and NAME = 'p2'");
//        Assert.assertTrue(program.contains(".2.minscha"));
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//    }
//
//    /**
//     * share a programm and access it<br>
//     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns four programs, "minscha" owns two programs<br>
//     * <b>PRE:</b> "minscha" can access his two programs, nothing is shared<br>
//     * <b>POST:</b> "minscha" can access his two programs and two shared programs
//     * <ul>
//     * <li>"pid" shares her programs "p2" R and "p3" W with "minscha"
//     * <li>"minscha" cannot modify "p2", but can modify "p3"
//     * </ul>
//     */
//    private void pidSharesProgramsMinschaCanAccessRW() throws Exception {
//        int pidId = this.sPid.getUserId();
//        int minschaId = this.sMinscha.getUserId();
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
//        assertProgramListingAsExpected(this.sMinscha, "['p1','p2']");
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));
//
//        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p2';'userToShare':'minscha';'right':'READ'}", "ok", Key.ACCESS_RIGHT_CHANGED);
//        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p3';'userToShare':'minscha';'right':'WRITE'}", "ok", Key.ACCESS_RIGHT_CHANGED);
//        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
//        JSONArray programListing = assertProgramListingAsExpected(this.sMinscha, "['p1','p2', 'p2', 'p3']");
//        boolean ownershipOk = false;
//        for ( int i = 0; i < programListing.length(); i++ ) {
//            JSONArray programInfo = programListing.getJSONArray(i);
//            if ( programInfo.getString(0).equals("p3") ) {
//                Assert.assertEquals("p3 is owned by pid", "pid", programInfo.getString(1));
//                ownershipOk = true;
//                break;
//            }
//        }
//        Assert.assertTrue(ownershipOk);
//
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
//        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        Assert.assertTrue(this.response.getEntity().toString().contains(".2.pid.updated"));
//        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        Assert.assertTrue(this.response.getEntity().toString().contains(".2.1.minscha"));
//        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p3';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        Assert.assertTrue(this.response.getEntity().toString().contains(".3.pid"));
//
//        saveProgram(this.sMinscha, pidId, -1, "p2", "<program>.2.minscha.update</program>", null, null, "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
//        String p2Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'");
//        Assert.assertTrue(p2Text.contains(".2.pid.updated"));
//        saveProgram(this.sMinscha, pidId, -1, "p3", "<program>.3.minscha.update</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        String p3Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p3'");
//        Assert.assertTrue(p3Text.contains(".3.minscha.update"));
//
//        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
//        assertProgramListingAsExpected(this.sMinscha, "['p1','p2', 'p2', 'p3']");
//    }
//
//    /**
//     * deleting a program removes the share<br>
//     * <b>INVARIANT:</b> two user exist, both user have logged in<br>
//     * <b>PRE:</b> "minscha" can access his two programs and two shared programs<br>
//     * <b>POST:</b> "minscha" can access his program "p1" and no shared programs
//     * <ul>
//     * <li>"minscha" deletes his program "p2" -> success; the programm cannot be loaded anymor, the programm cannot be deleted a second time, "minscha"
//     * continues to see a program "p2" (the program shared with "pid")
//     * <li>"pid" deletes her programs "p2" and "p3" -> success; nothing shared anymore, the visible programs match, neither owner nor sharer can load a deleted
//     * program
//     * </ul>
//     */
//    private void pidDeletesProgramsMinschaCannotAccess() throws Exception {
//        int pidId = this.sPid.getUserId();
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
//        assertProgramListingAsExpected(this.sMinscha, "['p1','p2','p2','p3']"); // p2 is from "pid"!
//
//        restProgram(this.sMinscha, "{'cmd':'deleteP';'programName':'p2';'author':'minscha'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
//        restProgram(
//            this.sMinscha,
//            "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'robot':'ev3';'author':'minscha'}",
//            "error",
//            Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
//        restProgram(this.sMinscha, "{'cmd':'deleteP';'programName':'p2';'author':'minscha'}", "error", Key.PROGRAM_DELETE_ERROR);
//        assertProgramListingAsExpected(this.sPid, "['p1','p2','p3','p4']");
//        assertProgramListingAsExpected(this.sMinscha, "['p1','p2','p3']"); // p2 is from "pid"!
//
//        Assert.assertEquals(2, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));
//        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p2';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
//        restProgram(this.sPid, "{'cmd':'deleteP';'programName':'p3';'author':'pid'}", "ok", Key.PROGRAM_DELETE_SUCCESS);
//        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
//        assertProgramListingAsExpected(this.sMinscha, "['p1']");
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM"));
//
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'pid'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
//        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'pid';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
//        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p2';'owner':'minscha';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
//        restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p3';'owner':'pid';'author':'minscha'}", "error", Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
//
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p2'"));
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p3'"));
//    }
//
//    /**
//     * it is possible to delete the share. This doesn't delete the program :-)<br>
//     * <b>INVARIANT:</b> two user exist, both user have logged in, "pid" owns program "p4"<br>
//     * <b>PRE:</b> "minscha" can access his program "p1" and no shared programs<br>
//     * <b>POST:</b> "minscha" can access his program "p1" and no shared programs
//     * <ul>
//     * <li>"pid" shares program "p4" W with "minscha"
//     * <li>"minscha" can write it
//     * <li>"pid" does see the change<br>
//     * <br>
//     * <li>"minscha" can delete the share
//     * <li>"minscha" cannot write it anymore
//     * <li>the program continues to exist (for "pid"), but it vanishes from the program list of "minscha"
//     * </ul>
//     */
//    private void pidSharesProgram1MinschaCanDeleteTheShare() throws Exception, JSONException {
//        {
//            int pidId = this.sPid.getUserId();
//            int minschaId = this.sMinscha.getUserId();
//            Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//            assertProgramListingAsExpected(this.sPid, "['p1','p4']");
//            assertProgramListingAsExpected(this.sMinscha, "['p1']");
//
//            restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'userToShare':'minscha';'right':'WRITE'}", "ok", Key.ACCESS_RIGHT_CHANGED);
//            assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");
//
//            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
//            String p4Text = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
//            Assert.assertTrue(p4Text.contains(".4.pid"));
//            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid"));
//            saveProgram(this.sMinscha, pidId, -1, "p4", "<program>.4.minscha.update</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
//            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
//            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
//
//            restProgram(this.sMinscha, "{'cmd':'shareDelete';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.ACCESS_RIGHT_DELETED);
//            saveProgram(this.sMinscha, pidId, -1, "p4", "<program>.5.minscha.fail</program>", null, null, "error", Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION);
//            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
//            assertProgramListingAsExpected(this.sMinscha, "['p1']");
//            assertProgramListingAsExpected(this.sPid, "['p1','p4']");
//            String p4TextUpd2 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
//            Assert.assertTrue(p4TextUpd2.contains(".4.minscha.update"));
//            Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
//        }
//    }
//
//    /**
//     * the lastChanged-timestamp is used to guarantee an optimistic locking: - "pid" shares program "p4" W with "minscha"
//     * <ul>
//     * <li>"pid" reads "p4" (with lastChanged == timestamp X1
//     * <li>"minscha" reads "p4" (with the same lastChanged == timestamp X1
//     * <li>"pid" can write (lastChanged becomes timestamp X2)
//     * <li>"minscha" cannot write (because lastChanged changed :-)
//     * <li>Vice versa: if "minscha" writes back before "pid" writes back, "pid"s write fails
//     * </ul>
//     */
//    private void pidAndMinschaAccessConcurrently() throws Exception, JSONException {
//        int pidId = this.sPid.getUserId();
//        int minschaId = this.sMinscha.getUserId();
//        Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
//        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
//        assertProgramListingAsExpected(this.sMinscha, "['p1']");
//
//        restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//        saveProgram(this.sPid, pidId, -1, "p4", "<program>.4.pId</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//        restProgram(this.sPid, "{'cmd':'shareP';'programName':'p4';'userToShare':'minscha';'right':'WRITE'}", "ok", Key.ACCESS_RIGHT_CHANGED);
//        assertProgramListingAsExpected(this.sPid, "['p1','p4']");
//        assertProgramListingAsExpected(this.sMinscha, "['p1','p4']");
//
//        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM where USER_ID = '" + minschaId + "'"));
//        Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p1'"));
//        Assert.assertEquals(0, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where OWNER_ID = '" + minschaId + "' and NAME = 'p4'"));
//        String programP4OfPid = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
//        Assert.assertTrue(programP4OfPid.contains(".4.pId"));
//
//        // scenario 1: minscha reads pid's p4, then he writes; pid doesn't use her program; the timestamp increases
//        {
//            Thread.sleep(500); // REST-call should be executed sequentially. The sleep is NO guaranty ... Otherwise see below!
//            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pId"));
//            long lastChanged1 = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
//            saveProgram(this.sMinscha, pidId, lastChanged1, "p4", "<program>.4.minscha.update</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//            String p4TextUpd1 = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
//            Assert.assertTrue(p4TextUpd1.contains(".4.minscha.update"));
//            Thread.sleep(500); // REST-call should be executed sequentially. The sleep is NO guaranty ... Otherwise see below!
//            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.update"));
//            long lastChanged2 = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
//            Assert.assertTrue("causality violated: changed2 must be later than changed1", lastChanged2 > lastChanged1); // here sometimes a time race occurs. This may generate a test error.
//        }
//        final Key LOCK_ERROR = Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING;
//        // scenario 2: minscha reads pid's p4, then pid reads her p4; pid stores her program, but minscha can't (his timestamp is outdated)
//        {
//            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            long minschaReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
//            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            long pidReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
//            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
//            saveProgram(this.sPid, pidId, pidReadTimestamp, "p4", "<program>.4.pid.concurrentOk</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
//            saveProgram(this.sMinscha, pidId, minschaReadTimestamp, "p4", "<program>.4.minscha.concurrentFail</program>", null, null, "error", LOCK_ERROR);
//            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.pid.concurrentOk"));
//            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
//            Assert.assertTrue(program.contains(".4.pid.concurrentOk"));
//        }
//        // scenario 3: minscha reads pid's p4, then pid reads her p4; minscha stores the shared program, but pid can't (her timestamp is outdated)
//        {
//            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            long minschaReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
//            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            long pidReadTimestamp = ((JSONObject) this.response.getEntity()).getLong("lastChanged");
//            Thread.sleep(2); // both timestamps are probably the same, sleeping to get a different 'last update timestamp'
//            saveProgram(
//                this.sMinscha,
//                pidId,
//                minschaReadTimestamp,
//                "p4",
//                "<program>.4.minscha.concurrentOk</program>",
//                null,
//                null,
//                "ok",
//                Key.PROGRAM_SAVE_SUCCESS);
//            restProgram(this.sMinscha, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
//            saveProgram(this.sPid, pidId, pidReadTimestamp, "p4", "<program>.4.pid.concurrentFail</program>", null, null, "error", LOCK_ERROR);
//            restProgram(this.sPid, "{'cmd':'loadP';'programName':'p4';'owner':'pid';'author':'pid'}", "ok", Key.PROGRAM_GET_ONE_SUCCESS);
//            Assert.assertTrue(this.response.getEntity().toString().contains(".4.minscha.concurrentOk"));
//            String program = this.memoryDbSetup.getOne("select PROGRAM_TEXT from PROGRAM where OWNER_ID = " + pidId + " and NAME = 'p4'");
//            Assert.assertTrue(program.contains(".4.minscha.concurrentOk"));
//        }
//    }
//
//    /**
//     * check, that the relationship between programs and configurations works<br>
//     * <br>
//     * 1. save a new config. Check, that a name and data is required. Check, that the name doesn't exist. Check, that the data is ok.<br>
//     * 2. save a new program with default configuration, anonymous configuration and named configuration. Check, that the data is ok.<br>
//     */
//    private void saveProgramsAndConfigurations() throws Exception {
//        int minschaId = this.sMinscha.getUserId();
//
//        {
//            saveConfigAs(this.sMinscha, minschaId, "mc1", "<conf>mc1.1.conf.minscha</conf>", "ok", Key.CONFIGURATION_SAVE_SUCCESS);
//            saveConfigAs(this.sMinscha, minschaId, null, "<conf>mc1.1.conf.minscha</conf>", "error", Key.SERVER_ERROR);
//            saveConfigAs(this.sMinscha, minschaId, "mc2", null, "error", Key.SERVER_ERROR);
//            saveConfigAs(this.sMinscha, minschaId, "mc1", "<conf>mc1.2.conf.minscha</conf>", "error", Key.CONFIGURATION_SAVE_ERROR);
//            Assert.assertEquals(1, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from CONFIGURATION where NAME like 'mc%'"));
//            String confHash = this.memoryDbSetup.getOne("select CONFIGURATION_HASH from CONFIGURATION where NAME = 'mc1'");
//            String confText = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + confHash + "'");
//            Assert.assertTrue(confText.contains("<conf>mc1.1.conf.minscha</conf>"));
//        }
//
//        {
//            saveProgramAs(this.sMinscha, minschaId, "mp1", "<program>mp1.minscha</program>", null, null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//            saveProgramAs(
//                this.sMinscha,
//                minschaId,
//                "mp2",
//                "<program>mp2.minscha</program>",
//                null,
//                "<conf>mp2.conf.minscha</conf>",
//                "ok",
//                Key.PROGRAM_SAVE_SUCCESS);
//            saveProgramAs(this.sMinscha, minschaId, "mp3", "<program>mp3.minscha</program>", "mc1", null, "ok", Key.PROGRAM_SAVE_SUCCESS);
//            saveProgramAs(this.sMinscha, minschaId, "mp4", "<program>mp3.minscha</program>", "mc1", "<conf>mp2.conf.minscha</conf>", "error", Key.SERVER_ERROR);
//            Assert.assertEquals(3, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from PROGRAM where NAME like 'mp%'"));
//            String cnMp1 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp1'");
//            String chMp1 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp1'");
//            Assert.assertTrue(cnMp1 == null && chMp1 == null);
//            String cnMp2 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp2'");
//            String chMp2 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp2'");
//            Assert.assertTrue(cnMp2 == null && chMp2 != null);
//            String ctMp2 = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + chMp2 + "'");
//            Assert.assertTrue(ctMp2 != null);
//            String cnMp3 = this.memoryDbSetup.getOne("select CONFIG_NAME from PROGRAM where NAME = 'mp3'");
//            String chMp3 = this.memoryDbSetup.getOne("select CONFIG_HASH from PROGRAM where NAME = 'mp3'");
//            Assert.assertTrue(cnMp3 != null && chMp3 == null);
//            String chMc1 = this.memoryDbSetup.getOne("select CONFIGURATION_HASH from CONFIGURATION where NAME = 'mc1'");
//            String ctMc1 = this.memoryDbSetup.getOne("select CONFIGURATION_TEXT from CONFIGURATION_DATA where CONFIGURATION_HASH = '" + chMc1 + "'");
//            Assert.assertTrue(ctMc1.contains("<conf>mc1.1.conf.minscha</conf>"));
//        }
//    }
//
//    // small helpers
//    @SuppressWarnings("unused")
//    private static final boolean _____helper_start_____ = true;
//
//    private void clientUser(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws Exception {
//        this.response = this.clientUser.command(this.sessionFactoryWrapper.getSession(), JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
//    }
//
//    /**
//     * call a REST service for program-related commands. Store the response into <code>this.response</code>. Check whether the expected result and the expected
//     * message key are found
//     *
//     * @param httpSession the session on which behalf the call is executed
//     * @param jsonAsString the command (will be parsed to a JSON object)
//     * @param result the expected result is either "ok" or "error"
//     * @param msgOpt optional key for the message; maybe null
//     * @throws Exception
//     */
//    private void restProgram(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws JSONException, Exception {
//        // TODO handle this in a better way
//        if ( jsonAsString.contains("loadP") ) {
//            this.response = this.restProject.getProgram(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        } else if ( jsonAsString.contains("shareP") ) {
//            this.response = this.restProject.shareProgram(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        } else if ( jsonAsString.contains("deleteP") ) {
//            this.response = this.restProject.deleteProject(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        } else if ( jsonAsString.contains("shareDelete") ) {
//            this.response = this.restProject.deleteProjectShare(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        } else {
//            throw new DbcException("Unexpected JSON command");
//        }
//        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
//    }
//
//    /**
//     * call the REST service responsible for storing NEW programs into the data base ("saveAsP").
//     *
//     * @param httpSession the session on which behalf the call is executed
//     * @param owner the id of the owner of the program
//     * @param name the name of the program
//     * @param program the program text
//     * @param confName the name of the configuration; null, if an anonymous configuration is used
//     * @param confText the configuration text (XML); null, if the default configuration is used
//     * @param result the expected result is either "ok" or "error"
//     * @param msgOpt optional key for the message; maybe null
//     */
//    private void saveProgramAs(
//        HttpSessionState httpSession,
//        int owner,
//        String name,
//        String program,
//        String confName,
//        String confText,
//        String result,
//        Key msgOpt)
//        throws Exception //
//    {
//        String jsonAsString = "{'cmd':'saveAsP';'programName':'" + name + "';'programText':'" + program + "'";
//        if ( confName != null ) {
//            jsonAsString += ";'configName':'" + confName + "'";
//        }
//        if ( confText != null ) {
//            jsonAsString += ";'configText':'" + confText + "'";
//        }
//        jsonAsString += ";}";
//        this.response = this.restProject.updateProject(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
//    }
//
//    /**
//     * call the REST service responsible for UPDATING programs in the data base (save). The program may be shared ... .
//     *
//     * @param httpSession the session on which behalf the call is executed
//     * @param owner the id of the owner of the program
//     * @param timestamp the last changed timestamp. If the timestamp is -1, for convenience, it is read from the database.
//     * @param name the name of the program
//     * @param program the program text (XML)
//     * @param confName the name of the configuration; null, if an anonymous configuration is used
//     * @param confText the configuration text (XML); null, if the default configuration is used
//     * @param result the expected result is either "ok" or "error"
//     * @param msgOpt optional key for the message; maybe null
//     * @throws Exception
//     */
//    private void saveProgram(
//        HttpSessionState httpSession,
//        int owner,
//        long timestamp,
//        String name,
//        String program,
//        String confName,
//        String confText,
//        String result,
//        Key msgOpt)
//        throws Exception //
//    {
//        boolean shared = httpSession.getUserId() != owner;
//        if ( timestamp == -1 ) {
//            // for update, first the timestamp is retrieved as it has to be shown to the persister (optimistic locking :-)
//            Timestamp changed = this.memoryDbSetup.getOne("select LAST_CHANGED from PROGRAM where OWNER_ID = " + owner + " and NAME = '" + name + "'");
//            timestamp = changed.getTime();
//        }
//        String jsonAsString =
//            "{'cmd':'save';'shared':" + shared + ";'programName':'" + name + "';'timestamp':" + timestamp + ";'programText':'" + program + "'";
//        if ( confName != null ) {
//            jsonAsString += ";'configName':'" + confName + "'";
//        }
//        if ( confText != null ) {
//            jsonAsString += ";'configText':'" + confText + "'";
//        }
//        jsonAsString += ";}";
//        this.response = this.restProject.updateProject(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
//    }
//
//    /**
//     * call the REST service responsible for storing NEW configutations into the data base ("saveAsC").
//     *
//     * @param httpSession the session on which behalf the call is executed
//     * @param owner the id of the owner of the program
//     * @param configName the name of the configuration
//     * @param configText the configuration text (XML)
//     * @param result the expected result is either "ok" or "error"
//     * @param msgOpt optional key for the message; maybe null
//     * @throws Exception
//     */
//    private void saveConfigAs(HttpSessionState httpSession, int owner, String configName, String configText, String result, Key msgOpt) throws Exception //
//    {
//        String jsonAsString = "{'cmd':'saveAsC'";
//        if ( configName != null ) {
//            jsonAsString += ";'name':'" + configName + "'";
//        }
//        if ( configText != null ) {
//            jsonAsString += ";'configuration':'" + configText + "'";
//        }
//        jsonAsString += ";}";
//        this.response = this.restConfiguration.command(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
//    }
//
//    /**
//     * call the REST service responsible for UPDATING existing configurations in the data base ("saveC").
//     *
//     * @param httpSession the session on which behalf the call is executed
//     * @param owner the id of the owner of the program
//     * @param name the name of the configuration
//     * @param conf the configuration text (XML)
//     * @param result the expected result is either "ok" or "error"
//     * @param msgOpt optional key for the message; maybe null
//     * @throws Exception
//     */
//    private void saveConfig(HttpSessionState httpSession, int owner, String name, String conf, String result, Key msgOpt) throws Exception //
//    {
//        String jsonAsString = "{'cmd':'saveC';'name':'" + name + "';'configuration':'" + conf + "';}";
//        this.response = this.clientConfiguration.command(JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString));
//        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
//    }
//
//    private JSONArray assertProgramListingAsExpected(HttpSessionState httpSession, String expectedProgramNamesAsJson) throws Exception, JSONException {
//        this.response = this.restProject.getProgramNames(JSONUtilForServer.mkD(httpSession.getInitToken(), "{'cmd':'loadPN'}"));
//        JSONUtilForServer.assertEntityRc(this.response, "ok", Key.PROGRAM_GET_ALL_SUCCESS);
//        JSONArray programListing = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
//        JSONArray programNames = new JSONArray();
//        for ( int i = 0; i < programListing.length(); i++ ) {
//            programNames.put(programListing.getJSONArray(i).get(0));
//        }
//        JSONUtilForServer.assertJsonEquals(expectedProgramNamesAsJson, programNames, false);
//        return programListing;
//    }
//}