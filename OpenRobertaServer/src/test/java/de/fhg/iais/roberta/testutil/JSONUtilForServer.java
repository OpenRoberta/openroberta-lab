package de.fhg.iais.roberta.testutil;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.javaServer.restServices.all.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientProgram;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3Command;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3DownloadJar;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

/**
 * utilities for creating, checking and transforming between Strings and JSON objects. Note, that two json libraries are used:<br>
 * - org.codehaus.jettison, used in almost all cases, also used by jersey), replacement of that by jackson or GSON possible<br>
 * - org.json, mainly used in the context of the org.skyscreamer.jsonassert library in the helper methods here<br>
 * You have to use a transformer before stating properties of JSON objects expressed with jsonassert<br>
 * <br>
 * to ease writing JSON objects, usually a String is supplied. In this String you may use single quotes ' as delimiter in keys ans String values. The helper
 * methods will convert single quotes ' to double quotes " before the Strings are converted to JSON objects.
 *
 * @author rbudde
 */
public class JSONUtilForServer {
    private static final Logger LOG = LoggerFactory.getLogger(JSONUtilForServer.class);

    private JSONUtilForServer() {
        // no objects
    }

    /**
     * transform a String to a JSON object. Before transforming replace all ' by "
     *
     * @param s the String to be transformed to a JSON object
     * @return the corresponding JSON object, never null
     * @throws JSONException
     */
    public static JSONObject mk(String s) throws JSONException {
        String sR = s.replaceAll("'", "\"");
        return new JSONObject(sR);
    }

    /**
     * transform a String to a JSON array. Before transforming replace all ' by "
     *
     * @param s the String to be transformed to a JSON array
     * @return the corresponding JSON array, never null
     * @throws JSONException
     */
    public static JSONArray mkA(String s) throws JSONException {
        String sR = s.replaceAll("'", "\"");
        return new JSONArray(sR);
    }

    /**
     * transform a String to a JSON object. Put this JSON object into a wrapper JSON object as used for the client-server communication. Before transforming
     * replace all ' by "
     *
     * @param s the String to be transformed to a JSON object and then set as the value of the data property of a wrapper JSON object
     * @return the JSON object for the client-server communication has the properties "data" and "log", never null
     * @throws JSONException
     */
    public static JSONObject mkD(String s) throws JSONException {
        return JSONUtilForServer.mk("{'data':" + s + ",'log':[]}");
    }

    /**
     * create an JSON object as needed for registering a robot
     *
     * @param token the token to be used to identify the robot
     * @return JSON object, never null
     * @throws JSONException
     */
    public static JSONObject mkRegisterToken(String token) throws JSONException {
        String s = "{'token':'" + token + "','cmd':'register','macaddr':'00-9A-90-00-2B-5B','brickname':'Garzi','battery':'7.2','version':'1.0.1'}";
        return JSONUtilForServer.mk(s);
    }

    /**
     * assert that an actual JSON object matches the expected one
     *
     * @param expected the expected JSON object as String. " may be encoded as '
     * @param actual the JSON object to compare with
     * @param strict true if properties must match 1:1 (no extension) and sequence matters; false otherwise
     * @throws Exception
     */
    public static void assertJsonEquals(String expected, JSONObject actual, boolean strict) throws Exception {
        org.json.JSONObject expectedJson = JSONUtilForServer.j2j(JSONUtilForServer.mk(expected));
        org.json.JSONObject actualJson = JSONUtilForServer.j2j(actual);
        JSONAssert.assertEquals(expectedJson, actualJson, strict);
    }

    /**
     * assert that an actual JSON array matches the expected one
     *
     * @param expected the expected JSON array as String. " may be encoded as '
     * @param actual the JSON array to compare with
     * @param strict true if properties must match 1:1 (no extension) and sequence matters; false otherwise
     * @throws Exception
     */
    public static void assertJsonEquals(String expected, JSONArray actual, boolean strict) throws Exception {
        org.json.JSONArray expectedJson = JSONUtilForServer.j2j(JSONUtilForServer.mkA(expected));
        org.json.JSONArray actualJson = JSONUtilForServer.j2j(actual);
        JSONAssert.assertEquals(expectedJson, actualJson, strict);
    }

    /**
     * given a response object, that contains a JSON entity with the property "rc" (return code"), assert that the value is as expected
     *
     * @param response the JSON object to check
     * @param rc the return code expected
     * @param message the success or error message key, that is transformed by the client to a localized message
     * @throws JSONException
     */
    public static void assertEntityRc(Response response, String rc, Key message) throws JSONException {
        JSONObject entity = (JSONObject) response.getEntity();
        Assert.assertEquals(rc, entity.getString("rc"));
        String responseKey = entity.optString("message");
        if ( message != null ) {
            Assert.assertEquals(message.getKey(), responseKey);
        } else if ( responseKey != null ) {
            Exception e = new RuntimeException("Please visit the stacktrace");
            JSONUtilForServer.LOG.error("you didn't supply a message key. You should do that. In this case I got: \"" + responseKey + "\"", e);
        }
    }

    /**
     * transform a org.json.JSONObject into a org.codehaus.jettison.json.JSONObject :-)<br>
     * Use this method for calling methods of class JSONAssert! JSONAssert expects org.json objects and not org.codehaus.jettison.json objects
     *
     * @param json of type org.codehaus.jettison.json.JSONObject
     * @return json of type org.json.JSONObject
     * @throws Exception
     */
    private static org.json.JSONObject j2j(org.codehaus.jettison.json.JSONObject json) throws Exception {
        return new org.json.JSONObject(json.toString());
    }

    /**
     * transform a org.json.JSONArray into a org.codehaus.jettison.json.JSONArray :-)<br>
     * use only in JSONAssert methods! be careful! JSONAssert expects org.json objects and not org.codehaus.jettison.json objects
     *
     * @param json of type org.codehaus.jettison.json.JSONArray
     * @return json of type org.json.JSONArray
     * @throws Exception
     */
    private static org.json.JSONArray j2j(org.codehaus.jettison.json.JSONArray json) throws Exception {
        return new org.json.JSONArray(json.toString());
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
                Response response = brickCommand.handle(JSONUtilForServer.mkRegisterToken(token));
                return ((JSONObject) response.getEntity()).getString("response").equals("ok");
            }
        };
        ThreadedFunction theUser = new ThreadedFunction() {
            @Override
            public boolean apply() throws Exception {
                Response response = restBlocks.command(sessionState, dbSession, JSONUtilForServer.mkD("{'cmd':'setToken';'token':'" + token + "'}"));
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
                Response response = downloadJar.handle(JSONUtilForServer.mk("{'token':'" + token + "'}")); // potentially an infinite wait
                return response.getStatus() == 200;
            }
        };
        ThreadedFunction theUser = new ThreadedFunction() {
            @Override
            public boolean apply() throws Exception {
                Response response =
                    restProgram
                        .command(sessionState, JSONUtilForServer.mkD("{'cmd':'runP';'name':'" + programName + "', 'configuration': 'Standardkonfiguration'}"));
                JSONObject entity = (JSONObject) response.getEntity();
                return entity.getString("rc").equals("ok") && entity.getString("data").equals("Brick is waiting");
            }
        };
        new SenderReceiverJUnit().run(theBrick, theUser);
    }
}
