package de.fhg.iais.roberta.util;

import eu.bitwalker.useragentutils.UserAgent;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.DbcException;

public final class Statistics {
    private static final Logger STAT = LoggerFactory.getLogger("statistics");
    private static final Logger LOG = LoggerFactory.getLogger(Statistics.class);

    private Statistics() {
    }

    /**
     * Logs the action and its arguments to the statistics logger with the trace level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void trace(String action, String... args) {
        try {
            STAT.trace(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the debug level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void debug(String action, String... args) {
        try {
            STAT.debug(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the info level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void info(String action, String... args) {
        try {
            STAT.info(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the info level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param request
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void infoUserAgent(String action, UserAgent userAgent, JSONObject request) {
        try {
            STAT.info(
                toJsonString(
                    action,
                    "Browser",
                    userAgent.getBrowser() + "/" + userAgent.getBrowserVersion(),
                    "OS",
                    userAgent.getOperatingSystem().getName(),
                    "DeviceType",
                    userAgent.getOperatingSystem().getDeviceType().getName(),
                    "ScreenSize",
                    request.getString("screenSize")));
        } catch ( Exception e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the warn level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void warn(String action, String... args) {
        try {
            STAT.warn(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the error level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void error(String action, String... args) {
        try {
            STAT.error(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    private static String toJsonString(String action, String... args) throws JSONException {
        if ( args.length % 2 != 0 ) {
            throw new DbcException("Statistics logging arguments must come in pairs of two!");
        }

        JSONObject jsonObjAction = new JSONObject();
        JSONObject jsonObjArgs = new JSONObject();
        JSONArray jsonArrArgs = new JSONArray();

        for ( int i = 0; i < args.length; i += 2 ) {
            jsonObjArgs.put(args[i], args[i + 1]);
        }

        jsonArrArgs.put(jsonObjArgs);
        jsonObjAction.put("args", jsonArrArgs);
        jsonObjAction.put("action", action);

        return jsonObjAction.toString();
    }
}
