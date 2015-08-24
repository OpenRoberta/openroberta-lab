package de.fhg.iais.roberta.javaServer.restInterfaceTest;

import java.sql.Timestamp;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.javaServer.restServices.all.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientProgram;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientUser;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3Command;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3DownloadJar;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3Communicator;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3CompilerWorkflow;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

@Category(IntegrationTest.class)
public class UserRelatedRestInterfaceTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private Ev3Communicator brickCommunicator;

    private String buildXml;
    private String connectionUrl;
    private String crosscompilerBasedir;
    private String robotResourcesDir;

    private Ev3CompilerWorkflow compilerWorkflow;

    private ClientUser restUser;
    private ClientProgram restProgram;
    private ClientAdmin restBlocks;
    private Ev3DownloadJar downloadJar;

    private Response response;
    private HttpSessionState sPid;
    private HttpSessionState sMinscha;
    private String blocklyProgram;
    private Ev3Command brickCommand;

    @Before
    public void setup() throws Exception {
        Properties properties = Util.loadProperties("classpath:openRoberta-basicUserInteraction.properties");
        this.buildXml = properties.getProperty("crosscompiler.build.xml");
        this.connectionUrl = properties.getProperty("hibernate.connection.url");
        this.crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");
        this.robotResourcesDir = properties.getProperty("robot.resources.dir");

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();
        this.brickCommunicator = new Ev3Communicator();
        this.compilerWorkflow = new Ev3CompilerWorkflow(this.brickCommunicator, this.crosscompilerBasedir, this.robotResourcesDir, this.buildXml);
        this.restUser = new ClientUser(this.brickCommunicator);
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);
        this.restBlocks = new ClientAdmin(this.brickCommunicator);
        this.downloadJar = new Ev3DownloadJar(this.brickCommunicator, this.crosscompilerBasedir);
        this.brickCommand = new Ev3Command(this.brickCommunicator);
        this.sPid = HttpSessionState.init();
        this.sMinscha = HttpSessionState.init();
        this.blocklyProgram = Resources.toString(UserRelatedRestInterfaceTest.class.getResource("/restInterfaceTest/action_BrickLight.xml"), Charsets.UTF_8);
    }

    @Test
    public void test() throws Exception {
        Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());

        // USER table empty; create user "pid" with success; USER table has 1 row; create same user with error; create second user "minscha"
        {
            Assert.assertEquals(0, getOneBigInteger("select count(*) from USER"));
            this.response =
                this.restUser.command(
                    this.sPid,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertEquals(1, getOneBigInteger("select count(*) from USER"));
            this.response =
                this.restUser.command(
                    this.sPid,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
            JSONUtilForServer.assertEntityRc(this.response, "error");
            Assert.assertEquals(1, getOneBigInteger("select count(*) from USER"));
            this.response =
                this.restUser.command(
                    this.sPid,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'createUser';'accountName':'minscha';'password':'12';'userEmail':'cavy@home';'role':'STUDENT'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertEquals(2, getOneBigInteger("select count(*) from USER"));
        }

        // login with user "pid", create 3 programs; p1 is a REAL program that may be compiled
        Integer s1Id;
        {
            this.response = //
                this.restUser.command( //
                    this.sPid,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'login';'accountName':'pid';'password':'wrong'}"));
            JSONUtilForServer.assertEntityRc(this.response, "error");
            Assert.assertTrue(!this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
            this.response = //
                this.restUser.command( //
                    this.sPid,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertTrue(this.sPid.isUserLoggedIn() && !this.sMinscha.isUserLoggedIn());
            s1Id = this.sPid.getUserId();
            Assert.assertEquals(0, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
            JSONObject fullRequest = JSONUtilForServer.mk("{'log':[];'data':{'cmd':'saveP';'name':'p1';'timestamp':0}}");
            fullRequest.getJSONObject("data").put("program", this.blocklyProgram);
            this.response = this.restProgram.command(this.sPid, fullRequest);
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            this.response =
                this.restProgram.command(this.sPid, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'p2';'timestamp':0;'program':'<program>.2.</program>'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            this.response =
                this.restProgram.command(this.sPid, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'p3';'timestamp':0;'program':'<program>.3.</program>'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertEquals(3, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        }

        // login with user "minscha", no program visible, create 2 programs; 5 programs in total
        Integer s2Id;
        {
            this.response = //
                this.restUser.command( //
                    this.sMinscha,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'login';'accountName':'minscha';'password':'12'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
            s2Id = this.sMinscha.getUserId();
            Assert.assertEquals(0, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s2Id));
            Assert.assertEquals(3, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
            this.response =
                this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'p1';'timestamp':0;'program':'<program>.0.</program>'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            this.response =
                this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'p2';'timestamp':0;'program':'<program>.1.</program>'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertEquals(2, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s2Id));
            Assert.assertEquals(5, getOneBigInteger("select count(*) from PROGRAM"));
        }

        // "pid" logs out and in, adds 1 program, has now 4 programs, updates p2, has 4 programs, get list of programs and assert that the names match
        {
            this.response = //
                this.restUser.command( //
                    this.sPid,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'logout'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertTrue(!this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
            this.response = //
                this.restUser.command( //
                    this.sPid,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertTrue(this.sPid.isUserLoggedIn() && this.sMinscha.isUserLoggedIn());
            this.response =
                this.restProgram.command(this.sPid, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'p4';'timestamp':0;'program':'<program>.4.</program>'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertEquals(4, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
            Assert.assertEquals(6, getOneBigInteger("select count(*) from PROGRAM"));
            // for update, first the timestamp is retrieved as it has to be shown to the persister (optimistic locking :-)
            Timestamp lastChanged = this.memoryDbSetup.getOne("select LAST_CHANGED from PROGRAM where OWNER_ID = " + s1Id + " and NAME = 'p2'");
            this.response =
                this.restProgram.command(
                    this.sPid,
                    JSONUtilForServer
                        .mkD("{'cmd':'saveP';'name':'p2';'timestamp':" + lastChanged.getTime() + ";'program':'<program>.2 updated 2.</program>'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertEquals(4, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
            Assert.assertEquals(6, getOneBigInteger("select count(*) from PROGRAM"));
            this.response = this.restProgram.command(this.sPid, JSONUtilForServer.mkD("{'cmd':'loadPN'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            JSONArray programListing = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
            assertProgramNames("['p1','p2','p3','p4']", programListing);
        }

        // user "pid" shares program "p2" with user "minscha", it appears in his listing of 3 programs (the name "p2" appears twice: own + shared)
        // user "minscha" deletes his program "p2" successfully, but cannot delete the shared program "p2". Now he has access to 2 programs. Then he delete the share.
        // Now he views 1 program.
        {
            this.response =
                this.restProgram.command(this.sPid, JSONUtilForServer.mkD("{'cmd':'shareP';'programName':'p2';'userToShare':'minscha';'right':'WRITE'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            this.response = this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'loadPN'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            JSONArray programListing1 = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
            assertProgramNames("['p1','p2', 'p2']", programListing1);
            this.response = this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'deleteP';'name':'p2'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            this.response = this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'deleteP';'name':'p2'}"));
            JSONUtilForServer.assertEntityRc(this.response, "error");
            this.response = this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'loadPN'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            JSONArray programListing2 = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
            assertProgramNames("['p1','p2']", programListing2);
            boolean ownershipOk = false;
            for ( int i = 0; i < programListing2.length(); i++ ) {
                JSONArray programInfo = programListing2.getJSONArray(i);
                if ( programInfo.getString(0).equals("p2") ) {
                    Assert.assertEquals("p2 is owned by pid", "pid", programInfo.getString(1));
                    ownershipOk = true;
                    break;
                }
            }
            Assert.assertTrue(ownershipOk);
            this.response = this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'shareDelete';'programName':'p2';'owner':'pid'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            this.response = this.restProgram.command(this.sMinscha, JSONUtilForServer.mkD("{'cmd':'loadPN'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            JSONArray programListing3 = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
            assertProgramNames("['p1']", programListing3);
            //            this.response =
            //                this.restProgram.command(this.sPid, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'p2';'timestamp':0;'program':'<program>.2.</program>'}"));
            //            JSONUtilForServer.assertEntityRc(this.response, "ok");
        }
        // User "pid" takes her program "p2" and shares it again. User "minscha" has access to "p2" again.
        // User "pid" deletes her program "p2". User "minscha" has no access to "p2" anymore. Trying to access the deleted program should NOT generate a server
        // error (500 ...), because this situation is possible when user actions are interleaved ... .

        // user "minscha" deletes his program "p"
        // user "pid" registers the robot with token "garzi" (and optionally many more ...); runs "p1"
        // registerToken(this.brickCommand, this.restBlocks, this.s1, this.sessionFactoryWrapper.getSession(), "garzi");
        // TODO: refactor downloadJar(this.downloadJar, this.restProgram, this.s1, "garzi", "p1");
    }

    private void assertProgramNames(String programNamesAsJsonArray, JSONArray programListing) throws JSONException, Exception {
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        JSONUtilForServer.assertJsonEquals(programNamesAsJsonArray, programNames, false);
    }

    private long getOneBigInteger(String sqlStmt) {
        return this.memoryDbSetup.getOneBigInteger(sqlStmt);
    }
}
