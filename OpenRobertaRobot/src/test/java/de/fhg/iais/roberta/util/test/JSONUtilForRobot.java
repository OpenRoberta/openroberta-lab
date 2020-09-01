package de.fhg.iais.roberta.util.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

public class JSONUtilForRobot {
    private JSONUtilForRobot() {
        // no objects
    }

    public static void assertJsonEquals(String expected, JSONArray actual, boolean strict) throws Exception {
        JSONArray expectedJson = mkA(expected);
        JSONAssert.assertEquals(expectedJson, actual, strict);
    }

    public static void assertJsonEquals(String expected, JSONObject actual, boolean strict) throws Exception {
        JSONObject expectedJson = mk(expected);
        JSONAssert.assertEquals(expectedJson, actual, strict);
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

    public static JSONArray mkA(String s) throws JSONException {
        String sR = s.replaceAll("'", "\"");
        return new JSONArray(sR);
    }

    public static void assertEntityRc(Response response, String rc) throws JSONException {
        assertEquals(rc, ((JSONObject) response.getEntity()).getString("rc"));
    }
}
