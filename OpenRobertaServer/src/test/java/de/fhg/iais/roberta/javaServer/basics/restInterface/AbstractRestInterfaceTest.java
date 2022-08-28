package de.fhg.iais.roberta.javaServer.basics.restInterface;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.javaServer.basics.TestConfiguration;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientConfiguration;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientProgramController;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientUser;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.UserGroupController;
import de.fhg.iais.roberta.main.MailManagement;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
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
 * The tests in this classes subclasses are essentially <b>integration tests</b> (in spite of the fact, that the front end is <i>not</i> tested. But as the
 * server is called
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
 * - at the start of a test use the setup() class to setup the database
 * - to avoid code repetition, use simple wrappers for REST calls,<br>
 * - use the <code>this.sessionFactoryWrapper</code> object to create data base sessions and<br>
 * - save the response into this.response - use the two http sessions this.sPid and this.sMinscha for stories executed by the users "pid" and "minscha"
 *
 * @author rbudde
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractRestInterfaceTest {
    protected static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    protected static final String PROG_PRE_ROBOT = "<block_set xmlns=\\\"http://de.fhg.iais.roberta.blockly\\\" robottype=\\\"";

    protected static final String PROG_PRE_TEXT =
        "\\\" xmlversion=\\\"2.0\\\" description=\\\"\\\" tags=\\\"\\\">"
            + "<instance x=\\\"512\\\" y=\\\"50\\\">"
            + "<block type=\\\"robControls_start\\\" id=\\\"RDF[XZ?y7bn;Z{?V}Q)(\\\" intask=\\\"true\\\" deletable=\\\"false\\\">"
            + "<mutation declare=\\\"false\\\"></mutation>"
            + "<field name=\\\"DEBUG\\\">FALSE</field>"
            + "<comment w=\\\"0\\\" h=\\\"0\\\" pinned=\\\"false\\\">";
    protected static final String PROG_POST = "</comment>" + "</block></instance></block_set>";
    protected static final String CONF_PRE =
        "<block_set xmlns=\\\"http://de.fhg.iais.roberta.blockly\\\" robottype=\\\"ev3\\\" xmlversion=\\\"2.0\\\" description=\\\"\\\" tags=\\\"\\\">"
            + "<instance x=\\\"213\\\" y=\\\"213\\\">"
            + "<block type=\\\"robBrick_EV3-Brick\\\" id=\\\"1\\\" intask=\\\"true\\\" deletable=\\\"false\\\">"
            + "<comment w=\\\"0\\\" h=\\\"0\\\" pinned=\\\"false\\\">";
    protected static final String CONF_POST =
        "</comment>"
            + "<value name=\\\"S1\\\"><block type=\\\"robBrick_touch\\\" id=\\\"2\\\" intask=\\\"true\\\"></block></value>"
            + "</block></instance></block_set>";

    protected XsltTransformer xsltTransformer;

    protected TestConfiguration tc;
    protected DbSetup memoryDbSetup; // use to query the test data base, etc. Shortcut: Retrieved from TestConfiguration tc

    protected Response response; // store a REST response here
    protected HttpSessionState sPid; // reference user 1 is "pid"
    protected HttpSessionState sMinscha; // reference user 2 is "minscha"

    protected HttpSessionState groupMember1;
    protected HttpSessionState groupMember2;

    protected RobotCommunicator robotCommunicator;

    protected ServerProperties serverProperties;
    protected ClientUser restUser;
    protected ClientAdmin restClient;
    protected ClientProgramController restProject;
    protected UserGroupController restGroup;
    protected ClientConfiguration restConfiguration;

    protected Map<String, RobotFactory> robotPlugins;

    //this is the setup class and should be called before testing
    protected void setup() throws Exception {
        ServerStarter.initLoggingBeforeFirstUse(new String[0]);

        this.serverProperties = new ServerProperties(Util.loadProperties(null));
        this.serverProperties.getserverProperties().put("server.public", "true"); // not dangerous! For this.restUser the mail management is set to null

        this.robotCommunicator = new RobotCommunicator();

        MailManagement mailManagement = Mockito.mock(MailManagement.class);
        this.restUser = new ClientUser(this.robotCommunicator, this.serverProperties, mailManagement);

        tc = TestConfiguration.setup();
        tc.deleteAllFromUserAndProgramTmpPasswords();
        this.memoryDbSetup = tc.getMemoryDbSetup();

        this.xsltTransformer = new XsltTransformer();

        this.restProject = new ClientProgramController(this.serverProperties);
        this.restGroup = new UserGroupController(this.robotCommunicator, this.serverProperties);
        this.restClient = new ClientAdmin(robotCommunicator, serverProperties);
        this.restConfiguration = new ClientConfiguration(this.robotCommunicator);
        this.robotPlugins = ServerStarter.configureRobotPlugins(this.robotCommunicator, this.serverProperties, EMPTY_STRING_LIST);
        DbSession dbSession = this.tc.getSessionFactoryWrapper().getSession(); // session is closed in the method called below
        ServerStarter.checkRobotPluginsDB(dbSession, robotPlugins.values());
        this.sPid = HttpSessionState.initOnlyLegalForDebugging("pid", robotPlugins, this.serverProperties, 1);
        this.sMinscha = HttpSessionState.initOnlyLegalForDebugging("minscha", robotPlugins, this.serverProperties, 2);
        this.groupMember1 = HttpSessionState.initOnlyLegalForDebugging("member1", robotPlugins, this.serverProperties, 3);
        this.groupMember2 = HttpSessionState.initOnlyLegalForDebugging("member2", robotPlugins, this.serverProperties, 4);
    }

    /**
     * this method should contain the setup() method and be the Before or first test of every test Class
     * should contain: setup();
     *
     * @throws Exception
     */
    public abstract void init() throws Exception;

    //helper classes for testing mainly methods to execute REST calls

    /**
     * creates two users with following data:<br>
     * accountName: 'pid' , userName: 'cavy' , password: 'dip', userEmail: 'cavy1@home.de'<br>
     * accountName: 'minscha' , userName: 'cavy' , password: '12', userEmail: null
     *
     * @throws Exception
     */
    protected void createUsers() throws Exception {
        //craete and activate "pid"
        restUser(
            this.sPid,
            "{'cmd':'createUser';'accountName':'pid';'userName':'cavy';'password':'dip';'userEmail':'cavy1@home.de';'role':'STUDENT', 'isYoungerThen14': false, 'language': 'de'}",
            "ok",
            Key.USER_ACTIVATION_SENT_MAIL_SUCCESS);
        String url = this.memoryDbSetup.getOne("select URL_POSTFIX from PENDING_EMAIL_CONFIRMATIONS").toString();
        restUser(this.sPid, "{'cmd':'activateUser'; 'userActivationLink': '" + url + "';}", "ok", Key.USER_ACTIVATION_SUCCESS);
        //create "Minscha"
        restUser(
            this.sPid,
            "{'cmd':'createUser';'accountName':'minscha';'userName':'cavy';'password':'12';'userEmail':'';'role':'STUDENT', 'isYoungerThen14': true, 'language': 'de'}",
            "ok",
            Key.USER_CREATE_SUCCESS);
    }

    protected DbSession newDbSession() {
        return this.tc.getSessionFactoryWrapper().getSession();
    }

    //creates FullRestRequest from String
    protected FullRestRequest mkFRR(String initToken, String jsonAsString) {
        JSONObject cmdAsJson = JSONUtilForServer.mkD(initToken, jsonAsString);
        return FullRestRequest.make(cmdAsJson);
    }

    /**
     * call the REST service responsible for group related actions
     *
     * @param httpSession the session on which behalf the call is executed
     * @param jsonAsString the command (will be parsed to a JSON object)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    protected void restGroups(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws Exception {
        JSONObject jsonCmd = JSONUtilForServer.mkD(httpSession.getInitToken(), jsonAsString);
        FullRestRequest cmd = FullRestRequest.make(jsonCmd);
        switch ( jsonCmd.getJSONObject("data").getString("cmd") ) {
            case "createUserGroup":
                //needs groupName and groupMemberNames
                this.response = this.restGroup.createUserGroup(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            case "addGroupMembers":
                //needs groupName groupMemberNames
                this.response = this.restGroup.addGroupMembers(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            case "getUserGroup":
                //needs groupName
                this.response = this.restGroup.getUserGroup(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            case "getUserGroupList":
                this.response = this.restGroup.getUserGroupListForUser(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            case "deleteUserGroups":
                //needs groupNames
                this.response = this.restGroup.deleteUserGroups(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            case "updateMemberAccount":
                //needs groupName, currentGroupMemberAccount, newGroupMemberAccount
                this.response = this.restGroup.updateMemberAccount(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            case "deleteGroupMembers":
                //needs groupName, groupMemberAccounts
                this.response = this.restGroup.deleteGroupMembers(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            case "setUserGroupMemberDefaultPasswords":
                //needs groupName, groupMemberAccounts
                this.response = this.restGroup.setUserGroupMemberDefaultPasswords(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
                break;
            default:
                throw new DbcException("Unexpected JSON command");

        }
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    protected Response restExportAll(HttpSessionState httpSession) throws Exception {
        return this.restProject.exportAllProgrammsOfUser(newDbSession(), httpSession.getInitToken());
    }

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
    protected void restUser(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws Exception {
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
    protected void restProgram(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws JSONException, Exception {
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

    /**
     * call the REST service responsible for storing NEW programs into the data base ("saveAsP").
     *
     * @param httpSession the session on which behalf the call is executed
     * @param ownerAccount the account of the owner of the program, maybe the logged in user, maybe a sharer
     * @param progName the name of the program
     * @param progXml the program text
     * @param confName the name of the configuration; null, if an anonymous configuration is used
     * @param confText the configuration text (XML); null, if the default configuration is used
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     */
    protected void saveProgramAs(
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

        String jsonAsString =
            "{'cmd':'saveAsP';'programName':'"
                + progName
                + "';'progXML':'"
                + PROG_PRE_ROBOT
                + httpSession.getRobotFactory().getGroup()
                + PROG_PRE_TEXT
                + progXml
                + PROG_POST
                + "'";
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
    protected void saveProgram(
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

        jsonAsString +=
            "'programName':'"
                + progName
                + "';'timestamp':"
                + timestamp
                + ";'progXML':'"
                + PROG_PRE_ROBOT
                + httpSession.getRobotFactory().getGroup()
                + PROG_PRE_TEXT
                + progXml
                + PROG_POST
                + "'";
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
    protected void saveConfigAs(HttpSessionState httpSession, int owner, String configName, String configText, String result, Key msgOpt) throws Exception //
    {
        String jsonAsString = "{'cmd':'saveAsC'";
        if ( configName != null ) {
            jsonAsString += ";'name':'" + configName + "'";
        }
        if ( configText != null ) {
            jsonAsString += ";'configuration':'" + CONF_PRE + configText + CONF_POST + "'";
        }
        jsonAsString += ";}";
        this.response = this.restConfiguration.saveConfig(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call the REST service responsible for UPDATING existing configurations in the data base ("saveC").
     *
     * @param httpSession the session on which behalf the call is executed
     * @param owner the id of the owner of the program
     * @param name the name of the configuration
     * @param configuration the configuration text (XML)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    protected void saveConfig(HttpSessionState httpSession, int owner, String name, String configuration, String result, Key msgOpt) throws Exception //
    {
        String jsonAsString = "{'cmd':'saveC';'name':'" + name + "';'configuration':'" + configuration + "';}";
        this.response = this.restConfiguration.saveConfig(newDbSession(), mkFRR(httpSession.getInitToken(), jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    protected JSONArray assertProgramListingAsExpected(HttpSessionState httpSession, String expectedProgramNamesAsJson) throws Exception, JSONException {
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

}
