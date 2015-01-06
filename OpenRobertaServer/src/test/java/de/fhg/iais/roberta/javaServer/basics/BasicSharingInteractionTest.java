package de.fhg.iais.roberta.javaServer.basics;

import static de.fhg.iais.roberta.testutil.JSONUtil.assertEntityRc;
import static de.fhg.iais.roberta.testutil.JSONUtil.mkD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;

public class BasicSharingInteractionTest {

    private static final int MAX_TOTAL_FRIENDS = 30;

    private BrickCommunicator brickCommunicator;
    private SessionFactoryWrapper sessionFactoryWrapper;
    private String connectionUrl;
    private DbSetup memoryDbSetup;

    private CompilerWorkflow compilerWorkflow;
    private String crosscompilerBasedir;
    private String robotResourcesDir;

    private RestUser restUser;
    private RestProgram restProgram;
    private String buildXml;

    private Response response;
    private HttpSessionState s1;
    private HttpSessionState s2;

    @Before
    public void setup() throws Exception {

        Properties properties = Util.loadProperties("classpath:openRoberta-basicSharingInteraction.properties");
        this.connectionUrl = properties.getProperty("hibernate.connection.url");
        this.buildXml = properties.getProperty("crosscompiler.build.xml");
        this.crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");
        this.robotResourcesDir = properties.getProperty("robot.resources.dir");

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();

        this.brickCommunicator = new BrickCommunicator();
        this.compilerWorkflow = new CompilerWorkflow(this.crosscompilerBasedir, this.robotResourcesDir, this.buildXml);

        this.restUser = new RestUser(this.brickCommunicator);
        this.restProgram = new RestProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);

        this.s1 = HttpSessionState.init();
        this.s2 = HttpSessionState.init();
    }

    @Test
    public void test() throws Exception {
        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
        // USER,PROGRAM AND USER_PROGRM table empty; create user "pid" with success; USER table has 1 row; create same user with error; create second user "minscha"
        assertEquals(0, getOneInt("select count(*) from USER"));
        assertEquals(0, getOneInt("select count(*) from PROGRAM"));
        assertEquals(0, getOneInt("select count(*) from USER_PROGRAM"));

        //CREATE MASTER AND PROGRAM  TO BE SHARED
        this.response = this.restUser.command(//create
            this.s1,
            this.sessionFactoryWrapper.getSession(),
            mkD("{'cmd':'createUser';'accountName':'master';'password':'master-p';'userEmail':'master@home.com';'role':'STUDENT'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(1, getOneInt("select count(*) from USER"));
        this.response = this.restUser.command(//login
            this.s1,
            this.sessionFactoryWrapper.getSession(),
            mkD("{'cmd':'login';'accountName':'master';'password':'master-p'}"));
        assertEntityRc(this.response, "ok");
        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'toShare';'program':'<program>...</program>'}"));
        assertEntityRc(this.response, "ok");
        assertEquals(1, getOneInt("select count(*) from PROGRAM"));

        //CREATE EACH FRIEND AND ONE PROGRAM PER FRIEND
        for ( int userNumber = 0; userNumber < MAX_TOTAL_FRIENDS; userNumber += 1 ) {
            HttpSessionState s = HttpSessionState.init();
            assertTrue(!s.isUserLoggedIn());
            this.response =
                this.restUser.command(s, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'createUser';'accountName':'pid-"
                    + userNumber
                    + "';'password':'dip-"
                    + userNumber
                    + "';'userEmail':'cavy@home';'role':'STUDENT'}"));
            assertEntityRc(this.response, "ok");
            assertEquals(2 + userNumber, getOneInt("select count(*) from USER"));
            this.response =
                this.restUser.command(s, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'login';'accountName':'pid-"
                    + userNumber
                    + "';'password':'dip-"
                    + userNumber
                    + "'}"));
            assertEntityRc(response, "ok");
            assertTrue(s.isUserLoggedIn());
            this.response = this.restProgram.command(s, mkD("{'cmd':'saveP';'name':'test" + userNumber + "';'program':'<program>...</program>'}"));
            assertEquals(2 + userNumber, getOneInt("select count(*) from PROGRAM"));
            assertEntityRc(this.response, "ok");

        }
        assertEquals(MAX_TOTAL_FRIENDS + 1, getOneInt("select count(*) from USER"));
        //Login with master
        this.response =
            this.restUser.command(this.s1, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'login';'accountName':'master';'password':'master-p'}"));
        assertEntityRc(response, "ok");
        assertTrue(this.s1.isUserLoggedIn());

        //Share with write rights pair friends
        for ( int userNumber = 0; userNumber < MAX_TOTAL_FRIENDS; userNumber += 2 ) {
            this.response =
                this.restProgram.command(this.s1, mkD("{'cmd':'shareP';'userToShare':'pid-" + userNumber + "';'programName':'toShare';'right':'WRITE'}"));
            assertEntityRc(response, "ok");
        }
        //Share with read rights odd friends
        for ( int userNumber = 1; userNumber < MAX_TOTAL_FRIENDS; userNumber += 2 ) {
            this.response =
                this.restProgram.command(this.s1, mkD("{'cmd':'shareP';'userToShare':'pid-" + userNumber + "';'programName':'toShare';'right':'READ'}"));
            assertEntityRc(response, "ok");
        }
        assertEquals(MAX_TOTAL_FRIENDS, getOneInt("select count(*) from USER_PROGRAM"));
        //Eliminate write rights for pair users 
        for ( int userNumber = 1; userNumber < MAX_TOTAL_FRIENDS; userNumber += 2 ) {
            this.response =
                this.restProgram.command(this.s1, mkD("{'cmd':'shareP';'userToShare':'pid-" + userNumber + "';'programName':'toShare';'right':'NONE'}"));
            assertEntityRc(response, "ok");
        }
        assertEquals(MAX_TOTAL_FRIENDS / 2, getOneInt("select count(*) from USER_PROGRAM"));
    }

    private int getOneInt(String sqlStmt) {
        return this.memoryDbSetup.getOneInt(sqlStmt);
    }
}
