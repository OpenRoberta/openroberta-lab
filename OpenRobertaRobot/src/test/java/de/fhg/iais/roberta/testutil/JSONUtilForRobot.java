package de.fhg.iais.roberta.testutil;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

public class JSONUtilForRobot {
    private JSONUtilForRobot() {
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
}
