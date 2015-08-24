package de.fhg.iais.roberta.javaServer.basics;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.javaServer.restServices.all.ClientProgram;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientUser;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3Communicator;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3CompilerWorkflow;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Util;

@Ignore
public class BasicSharingInteractionTest {

    private static final int MAX_TOTAL_FRIENDS = 30;

    private Ev3Communicator brickCommunicator;
    private SessionFactoryWrapper sessionFactoryWrapper;
    private String connectionUrl;
    private DbSetup memoryDbSetup;

    private Ev3CompilerWorkflow compilerWorkflow;
    private String crosscompilerBasedir;
    private String robotResourcesDir;

    private ClientUser restUser;
    private ClientProgram restProgram;
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

        this.brickCommunicator = new Ev3Communicator();
        this.compilerWorkflow = new Ev3CompilerWorkflow(this.brickCommunicator, this.crosscompilerBasedir, this.robotResourcesDir, this.buildXml);

        this.restUser = new ClientUser(this.brickCommunicator);
        this.restProgram = new ClientProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);

        this.s1 = HttpSessionState.init();
        this.s2 = HttpSessionState.init();
    }

    @Test
    public void test() throws Exception {
        Assert.assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
        // USER,PROGRAM AND USER_PROGRM table empty; create user "pid" with success; USER table has 1 row; create same user with error; create second user "minscha"
        Assert.assertEquals(0, getOneBigInteger("select count(*) from USER"));
        Assert.assertEquals(0, getOneBigInteger("select count(*) from PROGRAM"));
        Assert.assertEquals(0, getOneBigInteger("select count(*) from USER_PROGRAM"));

        //CREATE MASTER AND PROGRAM  TO BE SHARED
        this.response = this.restUser.command(//create
            this.s1,
            this.sessionFactoryWrapper.getSession(),
            JSONUtilForServer.mkD("{'cmd':'createUser';'accountName':'master';'password':'master-p';'userEmail':'master@home.com';'role':'STUDENT'}"));
        JSONUtilForServer.assertEntityRc(this.response, "ok");
        Assert.assertEquals(1, getOneBigInteger("select count(*) from USER"));
        this.response = this.restUser.command(//login
            this.s1,
            this.sessionFactoryWrapper.getSession(),
            JSONUtilForServer.mkD("{'cmd':'login';'accountName':'master';'password':'master-p'}"));
        JSONUtilForServer.assertEntityRc(this.response, "ok");
        this.response = this.restProgram.command(this.s1, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'toShare';'program':'<program>...</program>'}"));
        JSONUtilForServer.assertEntityRc(this.response, "ok");
        Assert.assertEquals(1, getOneBigInteger("select count(*) from PROGRAM"));

        //CREATE EACH FRIEND AND ONE PROGRAM PER FRIEND
        for ( int userNumber = 0; userNumber < BasicSharingInteractionTest.MAX_TOTAL_FRIENDS; userNumber += 1 ) {
            HttpSessionState s = HttpSessionState.init();
            Assert.assertTrue(!s.isUserLoggedIn());
            this.response =
                this.restUser.command(
                    s,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD(
                        "{'cmd':'createUser';'accountName':'pid-"
                            + userNumber
                            + "';'password':'dip-"
                            + userNumber
                            + "';'userEmail':'cavy@home';'role':'STUDENT'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertEquals(2 + userNumber, getOneBigInteger("select count(*) from USER"));
            this.response =
                this.restUser.command(
                    s,
                    this.sessionFactoryWrapper.getSession(),
                    JSONUtilForServer.mkD("{'cmd':'login';'accountName':'pid-" + userNumber + "';'password':'dip-" + userNumber + "'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
            Assert.assertTrue(s.isUserLoggedIn());
            this.response =
                this.restProgram.command(s, JSONUtilForServer.mkD("{'cmd':'saveP';'name':'test" + userNumber + "';'program':'<program>...</program>'}"));
            Assert.assertEquals(2 + userNumber, getOneBigInteger("select count(*) from PROGRAM"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");

        }
        Assert.assertEquals(BasicSharingInteractionTest.MAX_TOTAL_FRIENDS + 1, getOneBigInteger("select count(*) from USER"));
        //Login with master
        this.response =
            this.restUser.command(
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                JSONUtilForServer.mkD("{'cmd':'login';'accountName':'master';'password':'master-p'}"));
        JSONUtilForServer.assertEntityRc(this.response, "ok");
        Assert.assertTrue(this.s1.isUserLoggedIn());

        //Share with write rights pair friends
        for ( int userNumber = 0; userNumber < BasicSharingInteractionTest.MAX_TOTAL_FRIENDS; userNumber += 2 ) {
            this.response =
                this.restProgram
                    .command(this.s1, JSONUtilForServer.mkD("{'cmd':'shareP';'userToShare':'pid-" + userNumber + "';'programName':'toShare';'right':'WRITE'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
        }
        //Share with read rights odd friends
        for ( int userNumber = 1; userNumber < BasicSharingInteractionTest.MAX_TOTAL_FRIENDS; userNumber += 2 ) {
            this.response =
                this.restProgram
                    .command(this.s1, JSONUtilForServer.mkD("{'cmd':'shareP';'userToShare':'pid-" + userNumber + "';'programName':'toShare';'right':'READ'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
        }
        Assert.assertEquals(BasicSharingInteractionTest.MAX_TOTAL_FRIENDS, getOneBigInteger("select count(*) from USER_PROGRAM"));
        //Eliminate write rights for pair users
        for ( int userNumber = 1; userNumber < BasicSharingInteractionTest.MAX_TOTAL_FRIENDS; userNumber += 2 ) {
            this.response =
                this.restProgram
                    .command(this.s1, JSONUtilForServer.mkD("{'cmd':'shareP';'userToShare':'pid-" + userNumber + "';'programName':'toShare';'right':'NONE'}"));
            JSONUtilForServer.assertEntityRc(this.response, "ok");
        }
        Assert.assertEquals(BasicSharingInteractionTest.MAX_TOTAL_FRIENDS / 2, getOneBigInteger("select count(*) from USER_PROGRAM"));
    }

    private long getOneBigInteger(String sqlStmt) {
        return this.memoryDbSetup.getOneBigInteger(sqlStmt);
    }
}
