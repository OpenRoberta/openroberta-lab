package de.fhg.iais.roberta.javaServer.basics;

import static de.fhg.iais.roberta.testutil.JSONUtil.assertEntityRc;
import static de.fhg.iais.roberta.testutil.JSONUtil.assertJsonEquals;
import static de.fhg.iais.roberta.testutil.JSONUtil.downloadJar;
import static de.fhg.iais.roberta.testutil.JSONUtil.mkD;
import static de.fhg.iais.roberta.testutil.JSONUtil.registerToken;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.brick.Templates;
import de.fhg.iais.roberta.javaServer.resources.DownloadJar;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestBlocks;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.javaServer.resources.TokenReceiver;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;

public class BasicUserInteractionTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private BrickCommunicator brickCommunicator;

    private String buildXml;
    private String connectionUrl;
    private String crosscompilerBasedir;

    private CompilerWorkflow compilerWorkflow;

    private RestUser restUser;
    private RestProgram restProgram;
    private RestBlocks restBlocks;
    private DownloadJar downloadJar;
    private TokenReceiver tokenReceiver;

    private Response response;
    private HttpSessionState s1;
    private HttpSessionState s2;
    private String blocklyProgram;

    @Before
    public void setup() throws Exception {
        Properties properties = Util.loadProperties("classpath:openRoberta-basicUserInteraction.properties");
        this.buildXml = properties.getProperty("crosscompiler.build.xml");
        this.connectionUrl = properties.getProperty("hibernate.connection.url");
        this.crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();
        this.brickCommunicator = new BrickCommunicator();
        this.compilerWorkflow = new CompilerWorkflow(this.crosscompilerBasedir, this.buildXml);
        this.restUser = new RestUser();
        this.restProgram = new RestProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);
        this.restBlocks = new RestBlocks(new Templates(), this.brickCommunicator);
        this.downloadJar = new DownloadJar(this.brickCommunicator, this.crosscompilerBasedir);
        this.tokenReceiver = new TokenReceiver(this.brickCommunicator);
        this.s1 = HttpSessionState.init();
        this.s2 = HttpSessionState.init();
        this.blocklyProgram = Resources.toString(BasicPerformanceUserInteractionTest.class.getResource("/ast/actions/action_BrickLight.xml"), Charsets.UTF_8);
    }

    @Ignore
    public void test() throws Exception {
        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());

        // USER table empty; create user "pid" with success; USER table has 1 row; create same user with error; create second user "minscha"
        assertEquals(0, getOneInt("select count(*) from USER"));
        this.response =
            this.restUser.command(
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(1, getOneInt("select count(*) from USER"));
        this.response =
            this.restUser.command(
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertEntityRc(this.response, "ERROR");
        assertEquals(1, getOneInt("select count(*) from USER"));
        this.response =
            this.restUser.command(
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'minscha';'password':'12';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(2, getOneInt("select count(*) from USER"));

        // login with user "pid", create 3 programs; p1 is a REAL program that may be compiled
        this.response = //
            this.restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid';'password':'wrong'}"));
        assertEntityRc(this.response, "ERROR");
        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
        this.response = //
            this.restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
        assertEntityRc(this.response, "ok");
        assertTrue(this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
        int s1Id = this.s1.getUserId();
        assertEquals(0, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        JSONObject fullRequest = new JSONObject("{\"log\":[];\"data\":{\"cmd\":\"saveP\";\"name\":\"p1\"}}");
        fullRequest.getJSONObject("data").put("program", this.blocklyProgram);
        this.response = this.restProgram.command(this.s1, fullRequest);
        assertEntityRc(this.response, "ok");
        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p2';'program':'<program>.2.</program>'}"));
        assertEntityRc(this.response, "ok");
        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p3';'program':'<program>.3.</program>'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(3, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));

        // login with user "minscha", no program visible, create 2 programs; 5 programs in total
        this.response = //
            this.restUser.command( //
                this.s2,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'minscha';'password':'12'}"));
        assertEntityRc(this.response, "ok");
        assertTrue(this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        int s2Id = this.s2.getUserId();
        assertEquals(0, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s2Id));
        assertEquals(3, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = this.restProgram.command(this.s2, mkD("{'cmd':'saveP';'name':'p0';'program':'<program>.0.</program>'}"));
        assertEntityRc(this.response, "ok");
        this.response = this.restProgram.command(this.s2, mkD("{'cmd':'saveP';'name':'p1';'program':'<program>.1.</program>'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(2, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s2Id));
        assertEquals(5, getOneInt("select count(*) from PROGRAM"));

        // "pid" logout and login, add 1 program, has now 4 programs, updates p2, has 4 programs, get list of programs, assert that the names match
        this.response = //
            this.restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'logout'}"));
        assertEntityRc(this.response, "ok");
        assertTrue(!this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        this.response = //
            this.restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
        assertEntityRc(this.response, "ok");
        assertTrue(this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p4';'program':'<program>.4.</program>'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(4, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p2';'program':'<program>.2 updated 2.</program>'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(4, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'loadPN'}"));
        assertEntityRc(this.response, "ok");
        JSONArray programListing = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        assertJsonEquals("['p1','p2','p3','p4']", programNames, false);

        // user "pid" registers the robot with token "garzi" (and optionally many more ...); runs "p1"
        registerToken(this.tokenReceiver, this.restBlocks, this.s1, this.sessionFactoryWrapper.getSession(), "garzi");
        downloadJar(this.downloadJar, this.restProgram, this.s1, "garzi", "p1");
    }

    private int getOneInt(String sqlStmt) {
        return this.memoryDbSetup.getOneInt(sqlStmt);
    }
}
