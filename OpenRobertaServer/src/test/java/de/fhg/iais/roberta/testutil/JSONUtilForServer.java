package de.fhg.iais.roberta.testutil;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import de.fhg.iais.roberta.javaServer.restServices.all.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientProgram;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3Command;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3DownloadJar;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;

public class JSONUtilForServer {
    private JSONUtilForServer() {
        // no objects
    }

    public static void assertJsonEquals(String expected, JSONArray actual, boolean strict) throws Exception {
        org.json.JSONArray expectedJson = j2j(mkA(expected));
        org.json.JSONArray actualJson = j2j(actual);
        JSONAssert.assertEquals(expectedJson, actualJson, strict);
    }

    public static void assertJsonEquals(String expected, JSONObject actual, boolean strict) throws Exception {
        org.json.JSONObject expectedJson = j2j(mk(expected));
        org.json.JSONObject actualJson = j2j(actual);
        JSONAssert.assertEquals(expectedJson, actualJson, strict);
    }

    public static JSONObject mkD(String s) throws JSONException {
        return mk("{'data':" + s + ",'log':[]}");
    }

    public static JSONObject mk(String s) throws JSONException {
        String sR = s.replaceAll("'", "\"");
        return new JSONObject(sR);
    }

    public static JSONObject mkRegisterToken(String token) throws JSONException {
        String s = "{'token':'" + token + "','cmd':'register','macaddr':'00-9A-90-00-2B-5B','brickname':'Garzi','battery':'7.2','version':'1.0.1'}";
        return mk(s);
    }

    /**
     * transform a org.json.JSONObject into a org.codehaus.jettison.json.JSONObject :-)<br>
     * Use this method only for calling methods of class JSONAssert! JSONAssert expects org.json objects and not org.codehaus.jettison.json objects
     *
     * @param json of type org.codehaus.jettison.json.JSONObject
     * @return json of type org.json.JSONObject
     * @throws Exception
     */
    public static org.json.JSONObject j2j(org.codehaus.jettison.json.JSONObject json) throws Exception {
        return new org.json.JSONObject(json.toString());
    }

    /**
     * use only in JSONAssert methods! be careful! JSONAssert expects org.json objects and not org.codehaus.jettison.json objects
     *
     * @param json of type org.codehaus.jettison.json.JSONArray
     * @return json of type org.json.JSONArray
     * @throws Exception
     */
    public static org.json.JSONArray j2j(org.codehaus.jettison.json.JSONArray json) throws Exception {
        return new org.json.JSONArray(json.toString());
    }

    public static JSONArray mkA(String s) throws JSONException {
        String sR = s.replaceAll("'", "\"");
        return new JSONArray(sR);
    }

    public static void assertEntityRc(Response response, String rc) throws JSONException {
        assertEquals(rc, ((JSONObject) response.getEntity()).getString("rc"));
    }

    public static void registerToken(
        final Ev3Command brickCommand,
        final ClientAdmin restBlocks,
        final HttpSessionState sessionState,
        final DbSession dbSession,
        final String token) throws Exception //
    {
        ThreadedFunction theBrick = new ThreadedFunction() {
            @Override
            public boolean apply() throws Exception {
                Response response = brickCommand.handle(mkRegisterToken(token));
                return ((JSONObject) response.getEntity()).getString("response").equals("ok");
            }
        };
        ThreadedFunction theUser = new ThreadedFunction() {
            @Override
            public boolean apply() throws Exception {
                Response response = restBlocks.command(sessionState, dbSession, mkD("{'cmd':'setToken';'token':'" + token + "'}"));
                return ((JSONObject) response.getEntity()).getString("rc").equals("ok");
            }
        };
        new SenderReceiverJUnit().run(theBrick, theUser);
    }

    public static void downloadJar(
        final Ev3DownloadJar downloadJar,
        final ClientProgram restProgram,
        final HttpSessionState sessionState,
        final String token,
        final String programName) throws Exception //
    {
        ThreadedFunction theBrick = new ThreadedFunction() {
            @Override
            public boolean apply() throws Exception {
                Response response = downloadJar.handle(mk("{'token':'" + token + "'}")); // potentially an infinite wait
                return response.getStatus() == 200;
            }
        };
        ThreadedFunction theUser = new ThreadedFunction() {
            @Override
            public boolean apply() throws Exception {
                Response response =
                    restProgram.command(sessionState, mkD("{'cmd':'runP';'name':'" + programName + "', 'configuration': 'Standardkonfiguration'}"));
                JSONObject entity = (JSONObject) response.getEntity();
                return entity.getString("rc").equals("ok") && entity.getString("data").equals("Brick is waiting");
            }
        };
        new SenderReceiverJUnit().run(theBrick, theUser);
    }
}
