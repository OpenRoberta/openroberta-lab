package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.javaServer.resources.OpenRobertaSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.persistence.connector.SessionFactoryWrapper;

public class DBTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DB db;
    private BrickCommunicator brickCommunicator;

    private Response response;
    private OpenRobertaSessionState s1;
    private OpenRobertaSessionState s2;

    @Before
    public void setup() throws Exception {
        this.sessionFactoryWrapper = new SessionFactoryWrapper("db/hibernate-test-cfg.xml");
        Session session = this.sessionFactoryWrapper.getNativeSession();
        this.db = DB.make(session);
        this.db.executeSqlFile("./db/create-tables.sql");
        this.brickCommunicator = new BrickCommunicator();
    }

    @Test
    public void testCompleteSession() throws Exception {
        RestUser restUser = new RestUser(this.sessionFactoryWrapper);
        RestProgram restProgram = new RestProgram(this.sessionFactoryWrapper, this.brickCommunicator);
        this.s1 = OpenRobertaSessionState.init();
        this.s2 = OpenRobertaSessionState.init();
        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());

        // USER table empty; create user with success; USER table has 1 row; create same user with error; create second user
        assertEquals(0, getOneInt("select count(*) from USER"));
        this.response = restUser.command(this.s1, mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertResponseRc("ok");
        assertEquals(1, getOneInt("select count(*) from USER"));
        this.response = restUser.command(this.s1, mkD("{'cmd':'createUser';'accountName':'pid';'password':'dip';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertResponseRc("ERROR");
        assertEquals(1, getOneInt("select count(*) from USER"));
        this.response = restUser.command(this.s1, mkD("{'cmd':'createUser';'accountName':'minscha';'password':'12';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertResponseRc("ok");
        assertEquals(2, getOneInt("select count(*) from USER"));

        // login with user "pid", create 3 programs
        this.response = restUser.command(this.s1, mkD("{'cmd':'login';'accountName':'pid';'password':'wrong'}"));
        assertResponseRc("ERROR");
        assertTrue(!this.s1.isUserLoggedIn() && !this.s2.isUserLoggedIn());
        this.response = restUser.command(this.s1, mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
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
        this.response = restUser.command(this.s2, mkD("{'cmd':'login';'accountName':'minscha';'password':'12'}"));
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

        // "pid" logout and login, add 1 program has now 4 programs, updates p2, has 4 programs, get list of programs
        this.response = restUser.command(this.s1, mkD("{'cmd':'logout'}"));
        assertResponseRc("ok");
        assertTrue(!this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        this.response = restUser.command(this.s1, mkD("{'cmd':'login';'accountName':'pid';'password':'dip'}"));
        assertResponseRc("ok");
        assertTrue(this.s1.isUserLoggedIn() && this.s2.isUserLoggedIn());
        this.response = restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p4';'program':'<program>.4.</program>'}"));
        assertResponseRc("ok");
        assertEquals(4, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        this.response = restProgram.command(this.s1, mkD("{'cmd':'saveP';'name':'p2';'program':'<program>.2 updated 2.</program>'}"));
        assertResponseRc("ok");
        assertEquals(4, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
    }

    private static JSONObject mkD(String s) throws JSONException {
        return mk("{'data':" + s + "}");
    }

    private static JSONObject mk(String s) throws JSONException {
        String sDouble = s.replaceAll("'", "\"");
        return new JSONObject(sDouble);
    }

    private void assertResponseRc(String rc) throws JSONException {
        assertEquals(rc, ((JSONObject) this.response.getEntity()).getString("rc"));
    }

    private int getOneInt(String sqlStmt) {
        return ((BigInteger) this.db.executeOneValueSelect(sqlStmt)).intValue();
    }
}
