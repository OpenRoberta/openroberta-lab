//package de.fhg.iais.roberta.javaServer.basics;
//
//import static de.fhg.iais.roberta.testutil.JSONUtil.assertEntityRc;
//import static de.fhg.iais.roberta.testutil.JSONUtil.mkD;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Properties;
//
//import javax.ws.rs.core.Response;
//
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONObject;
//import org.hibernate.Session;
//import org.junit.Before;
//import org.junit.Test;
//
//import de.fhg.iais.roberta.brick.BrickCommunicator;
//import de.fhg.iais.roberta.brick.CompilerWorkflow;
//import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
//import de.fhg.iais.roberta.javaServer.resources.RestProgram;
//import de.fhg.iais.roberta.javaServer.resources.RestUser;
//import de.fhg.iais.roberta.persistence.bo.Program;
//import de.fhg.iais.roberta.persistence.bo.User;
//import de.fhg.iais.roberta.persistence.dao.ProgramDao;
//import de.fhg.iais.roberta.persistence.dao.UserDao;
//import de.fhg.iais.roberta.persistence.util.DbSession;
//import de.fhg.iais.roberta.persistence.util.DbSetup;
//import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
//import de.fhg.iais.roberta.util.Util;
//
//public class BasicSharingTest {
//
//    private static final int MAX_TOTAL_FRIENDS = 30;
//
//    private BrickCommunicator brickCommunicator;
//    private SessionFactoryWrapper sessionFactoryWrapper;
//    private String connectionUrl;
//    private DbSetup memoryDbSetup;
//
//    private CompilerWorkflow compilerWorkflow;
//    private String crosscompilerBasedir;
//    private String robotResourcesDir;
//
//    private RestUser restUser;
//    private RestProgram restProgram;
//    private String buildXml;
//
//    private Response response;
//    private HttpSessionState s1;
//    private HttpSessionState s2;
//
//    @Before
//    public void setup() throws Exception {
//
//        Properties properties = Util.loadProperties("classpath:openRoberta-basicSharing.properties");
//        this.connectionUrl = properties.getProperty("hibernate.connection.url");
//        this.buildXml = properties.getProperty("crosscompiler.build.xml");
//        this.crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");
//        this.robotResourcesDir = properties.getProperty("robot.resources.dir");
//
//        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
//        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
//        this.memoryDbSetup = new DbSetup(nativeSession);
//        this.memoryDbSetup.runDefaultRobertaSetup();
//
//        this.brickCommunicator = new BrickCommunicator();
//        this.compilerWorkflow = new CompilerWorkflow(this.crosscompilerBasedir, this.robotResourcesDir, this.buildXml);
//
//        this.restUser = new RestUser(this.brickCommunicator);
//        this.restProgram = new RestProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);
//
//        this.s1 = HttpSessionState.init();
//        this.s2 = HttpSessionState.init();
//    }
//
//    @Test
//    public void test() throws Exception {
//        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
//
//        DbSession hSession = this.sessionFactoryWrapper.getSession();
//        UserDao userDao = new UserDao(hSession);
//        ProgramDao programDao = new ProgramDao(hSession);
//
//        // USER,PROGRAM AND USER_PROGRM table empty; create user "pid" with success; USER table has 1 row; create same user with error; create second user "minscha"
//        assertEquals(0, getOneInt("select count(*) from USER"));
//        assertEquals(0, getOneInt("select count(*) from PROGRAM"));
//        assertEquals(0, getOneInt("select count(*) from USER_PROGRAM"));
//
//        //CREATE MASTER AND PROGRAM  TO BE SHARED
//        this.response = this.restUser.command(//create
//            this.s1,
//            this.sessionFactoryWrapper.getSession(),
//            mkD("{'cmd':'createUser';'accountName':'master';'password':'master-p';'userEmail':'master@home.com';'role':'STUDENT'}"));
//        assertEntityRc(this.response, "ok");
//        assertEquals(1, getOneInt("select count(*) from USER"));
//        this.response = this.restUser.command(//login
//            this.s1,
//            this.sessionFactoryWrapper.getSession(),
//            mkD("{'cmd':'login';'accountName':'master';'password':'master-p'}"));
//        assertEntityRc(this.response, "ok");
//        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'toShare';'program':'<program>...</program>'}"));
//        assertEntityRc(this.response, "ok");
//        assertEquals(1, getOneInt("select count(*) from PROGRAM"));
//
//        //CREATE FRIEND AND ONE PROGRAM PER FRIEND
//        HttpSessionState s = HttpSessionState.init();
//        assertTrue(!s.isUserLoggedIn());
//        this.response =
//            this.restUser.command(
//                s,
//                this.sessionFactoryWrapper.getSession(),
//                mkD("{'cmd':'createUser';'accountName':'pid-0';'password':'dip-0';'userEmail':'cavy@home';'role':'STUDENT'}"));
//        assertEntityRc(this.response, "ok");
//        assertEquals(2, getOneInt("select count(*) from USER"));
//        this.response = this.restUser.command(s, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'login';'accountName':'pid-0';'password':'dip-0'}"));
//        assertEntityRc(response, "ok");
//        assertTrue(s.isUserLoggedIn());
//        this.response = this.restProgram.command(s, mkD("{'cmd':'saveP';'name':'test0';'program':'<program>...</program>'}"));
//        assertEquals(2, getOneInt("select count(*) from PROGRAM"));
//        assertEntityRc(this.response, "ok");
//        assertEquals(2, getOneInt("select count(*) from USER"));
//
//        //Login with master
//        this.response =
//            this.restUser.command(this.s1, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'login';'accountName':'master';'password':'master-p'}"));
//        assertEntityRc(response, "ok");
//        assertTrue(this.s1.isUserLoggedIn());
//
//        //Login with user pid-0
//        this.response =
//            this.restUser.command(this.s2, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'login';'accountName':'pid-0';'password':'dip-0'}"));
//        assertEntityRc(response, "ok");
//        assertTrue(this.s2.isUserLoggedIn());
//
//        //Share
//        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'shareP';'userToShare':'pid-0';'programName':'toShare';'right':'WRITE'}"));
//        assertEntityRc(response, "ok");
//
//        //Here we make sure that the share has the right user and the right shared program
//        User owner = userDao.load(s1.getUserId());
//        Program program = programDao.load("toShare", owner);
//        Object[] result1 = getOneResult("select * from USER_PROGRAM");
//        assertEquals(1, getOneInt("select count(*) from USER_PROGRAM"));
//        assertEquals(s2.getUserId(), (int) result1[1]);
//        assertEquals(program.getId(), (int) result1[2]);
//        assertEquals("WRITE", result1[3].toString());
//
//        //Change to read rights
//        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'shareP';'userToShare':'pid-0';'programName':'toShare';'right':'READ'}"));
//        assertEntityRc(response, "ok");
//        assertEquals(1, getOneInt("select count(*) from USER_PROGRAM"));
//        Object[] result2 = getOneResult("select * from USER_PROGRAM");
//        assertEquals("READ", result2[3].toString());
//
//        //Access List of Programs for user pid-0
//        this.response = this.restProgram.command(this.s2, mkD("{'cmd':'loadPN'}"));
//        JSONObject responseObject = (JSONObject) response.getEntity();
//        JSONArray programNames = responseObject.getJSONArray("programNames");
//        JSONArray programInfo = programNames.getJSONArray(programNames.length() - 1);
//        String ownerName = programInfo.getString(1);
//        assertEquals("master", ownerName);
//        assertEntityRc(response, "ok");
//
//        //Access List of Programs for master
//        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'loadPN'}"));
//        JSONObject responseObject2 = (JSONObject) response.getEntity();
//        JSONArray programNames2 = responseObject2.getJSONArray("programNames");
//        JSONArray programInfo2 = programNames2.getJSONArray(0);
//        JSONArray sharedUsersList2 = programInfo2.getJSONArray(programInfo2.length() - 1);
//        JSONArray sharedUser = sharedUsersList2.getJSONArray(0);
//        String userShared = sharedUser.getString(0);
//        String rigth = sharedUser.getString(1);
//        assertEquals("pid-0", userShared);
//        assertEquals("READ", rigth);
//        assertEntityRc(response, "ok"); 
//
//        //Eliminate write rights for pair users
//        this.response = this.restProgram.command(this.s1, mkD("{'cmd':'shareP';'userToShare':'pid-0';'programName':'toShare';'right':'NONE'}"));
//        assertEquals(0, getOneInt("select count(*) from USER_PROGRAM"));
//        assertEntityRc(response, "ok");
//    }
//
//    private int getOneInt(String sqlStmt) {
//        return this.memoryDbSetup.getOneInt(sqlStmt);
//    }
//
//    private Object[] getOneResult(String sqlStmt) {
//        return this.memoryDbSetup.getOneResult(sqlStmt);
//    }
//}
