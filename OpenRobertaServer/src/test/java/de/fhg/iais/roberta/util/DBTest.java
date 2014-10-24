package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.persistence.connector.SessionFactoryWrapper;

public class DBTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DB db;
    private BrickCommunicator brickCommunicator;
    private CompilerWorkflow compilerWorkflow;

    private Response response;
    private HttpSessionState s1;
    private HttpSessionState s2;

    @Before
    public void setup() throws Exception {
        this.sessionFactoryWrapper = new SessionFactoryWrapper("db/hibernate-test-cfg.xml");
        Session session = this.sessionFactoryWrapper.getNativeSession();
        this.db = DB.make(session);
        this.db.executeSqlFile("./db/create-tables.sql");
        this.brickCommunicator = new BrickCommunicator();
        Path tempDirectory = Files.createTempDirectory("userProjects");
        String buildXml = "../OpenRobertaRuntime/.build.xml"; // TODO: brittle relative path
        this.compilerWorkflow = new CompilerWorkflow(tempDirectory.toString(), buildXml);
    }

    @Test
    public void testCompleteSession() throws Exception {
        RestUser restUser = new RestUser();
        RestProgram restProgram = new RestProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);
        this.s1 = HttpSessionState.init();
        this.s2 = HttpSessionState.init();
        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());

        // USER table empty; create user "pid" with success; USER table has 1 row; create same user with error; create second user "minscha"
        assertEquals(0, getOneInt("select count(*) from USER"));
        this.response =
            restUser.command(
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertResponseRc("ok");
        assertEquals(1, getOneInt("select count(*) from USER"));
        this.response =
            restUser.command(
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertResponseRc("ERROR");
        assertEquals(1, getOneInt("select count(*) from USER"));
        this.response =
            restUser.command(
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'minscha';'password':'12';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertResponseRc("ok");
        assertEquals(2, getOneInt("select count(*) from USER"));

        // login with user "pid", create 3 programs
        this.response = //
            restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid';'password':'wrong'}"));
        assertResponseRc("ERROR");
        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
        this.response = //
            restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
        assertResponseRc("ok");
        assertTrue(this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
        int s1Id = this.s1.getUserId();
        assertEquals(0, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p1';'program':'<program>.1.</program>'}"));
        assertResponseRc("ok");
        this.response = restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p2';'program':'<program>.2.</program>'}"));
        assertResponseRc("ok");
        this.response = restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p3';'program':'<program>.3.</program>'}"));
        assertResponseRc("ok");
        assertEquals(3, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));

        // login with user "minscha", no program visible, create 2 programs; 5 programs in total
        this.response = //
            restUser.command( //
                this.s2,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'minscha';'password':'12'}"));
        assertResponseRc("ok");
        assertTrue(this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        int s2Id = this.s2.getUserId();
        assertEquals(0, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s2Id));
        assertEquals(3, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = restProgram.command(this.s2, mkD("{'cmd':'saveP';'name':'p0';'program':'<program>.0.</program>'}"));
        assertResponseRc("ok");
        this.response = restProgram.command(this.s2, mkD("{'cmd':'saveP';'name':'p1';'program':'<program>.1.</program>'}"));
        assertResponseRc("ok");
        assertEquals(2, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s2Id));
        assertEquals(5, getOneInt("select count(*) from PROGRAM"));

        // "pid" logout and login, add 1 program, has now 4 programs, updates p2, has 4 programs, get list of programs, assert that the names match
        this.response = //
            restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'logout'}"));
        assertResponseRc("ok");
        assertTrue(!this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        this.response = //
            restUser.command( //
                this.s1,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
        assertResponseRc("ok");
        assertTrue(this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        this.response = restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p4';'program':'<program>.4.</program>'}"));
        assertResponseRc("ok");
        assertEquals(4, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p2';'program':'<program>.2 updated 2.</program>'}"));
        assertResponseRc("ok");
        assertEquals(4, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = restProgram.command(this.s1, mkD("{'cmd':'loadPN'}"));
        assertResponseRc("ok");
        JSONArray programListing = ((JSONObject) this.response.getEntity()).getJSONArray("programNames");
        JSONArray programNames = new JSONArray();
        for ( int i = 0; i < programListing.length(); i++ ) {
            programNames.put(programListing.getJSONArray(i).get(0));
        }
        assertJsonEquals("['p1','p2','p3','p4']", programNames, false);
    }

    private void assertJsonEquals(String expected, JSONArray actual, boolean strict) throws Exception {
        org.json.JSONArray expectedJson = j2j(mkA(expected));
        org.json.JSONArray actualJson = j2j(actual);
        JSONAssert.assertEquals(expectedJson, actualJson, strict);
    }

    private void assertJsonEquals(String expected, JSONObject actual, boolean strict) throws Exception {
        org.json.JSONObject expectedJson = j2j(mk(expected));
        org.json.JSONObject actualJson = j2j(actual);
        JSONAssert.assertEquals(expectedJson, actualJson, strict);
    }

    private static JSONObject mkD(String s) throws JSONException {
        return mk("{'data':" + s + "}");
    }

    private static JSONObject mk(String s) throws JSONException {
        String sR = s.replaceAll("'", "\"");
        return new JSONObject(sR);
    }

    /**
     * use only in JSONAssert methods! be careful! JSONAssert expects org.json objects and not org.codehaus.jettison.json objects
     *
     * @param json of type org.codehaus.jettison.json.JSONObject
     * @return json of type org.json.JSONObject
     * @throws Exception
     */
    private static org.json.JSONObject j2j(org.codehaus.jettison.json.JSONObject json) throws Exception {
        return new org.json.JSONObject(json.toString());
    }

    /**
     * use only in JSONAssert methods! be careful! JSONAssert expects org.json objects and not org.codehaus.jettison.json objects
     *
     * @param json of type org.codehaus.jettison.json.JSONArray
     * @return json of type org.json.JSONArray
     * @throws Exception
     */
    private static org.json.JSONArray j2j(org.codehaus.jettison.json.JSONArray json) throws Exception {
        return new org.json.JSONArray(json.toString());
    }

    private static JSONArray mkA(String s) throws JSONException {
        String sR = s.replaceAll("'", "\"");
        return new JSONArray(sR);
    }

    private void assertResponseRc(String rc) throws JSONException {
        assertEquals(rc, ((JSONObject) this.response.getEntity()).getString("rc"));
    }

    private int getOneInt(String sqlStmt) {
        return ((BigInteger) this.db.executeOneValueSelect(sqlStmt)).intValue();
    }
}
