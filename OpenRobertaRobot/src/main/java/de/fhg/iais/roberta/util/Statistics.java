package de.fhg.iais.roberta.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.DbcException;
import eu.bitwalker.useragentutils.UserAgent;

public final class Statistics {
    private static final Logger STAT = LoggerFactory.getLogger("statistics");
    private static final Logger LOG = LoggerFactory.getLogger(Statistics.class);

    private static final Set<Class<?>> SUPPORTED_OBJECT_CLASSES =
        new HashSet<>(Arrays.asList(Boolean.class, Integer.class, Float.class, Double.class, String.class));

    private Statistics() {
    }

    /**
     * Logs the action and its arguments to the statistics logger with the trace level. The arguments must come in key value pairs, otherwise the internal
     * toJsonString method throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void trace(String action, Object... args) {
        try {
            STAT.trace(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the debug level. The arguments must come in key value pairs, otherwise the internal
     * toJsonString method throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void debug(String action, Object... args) {
        try {
            STAT.debug(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the info level. The arguments must come in key value pairs, otherwise the internal
     * toJsonString method throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void info(String action, Object... args) {
        try {
            STAT.info(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the info level. The arguments must come in key value pairs, otherwise the internal
     * toJsonString method throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param string
     * @param request
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void infoUserAgent(String action, UserAgent userAgent, String countryCode, JSONObject request) {
        try {
            STAT
                .info(
                    toJsonString(
                        action,
                        "Browser",
                        userAgent.getBrowser() + "/" + userAgent.getBrowserVersion(),
                        "OS",
                        userAgent.getOperatingSystem().getName(),
                        "CountryCode",
                        countryCode,
                        "DeviceType",
                        userAgent.getOperatingSystem().getDeviceType().getName(),
                        "ScreenSize",
                        request.getJSONArray("screenSize").toString()));
        } catch ( Exception e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the warn level. The arguments must come in key value pairs, otherwise the internal
     * toJsonString method throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void warn(String action, Object... args) {
        try {
            STAT.warn(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    /**
     * Logs the action and its arguments to the statistics logger with the error level. The arguments must come in key value pairs, otherwise the internal
     * toJsonString method throws a DbcException.
     *
     * @param action the general action or category of the log
     * @param args variable number of key value pairs, these are put in a json array
     */
    public static void error(String action, Object... args) {
        try {
            STAT.error(toJsonString(action, args));
        } catch ( JSONException e ) {
            LOG.error("Logging statistics failed for: " + action);
        }
    }

    private static String toJsonString(String action, Object... args) throws JSONException {
        if ( args.length % 2 != 0 ) {
            throw new DbcException("Statistics logging arguments must come in pairs of two!");
        }

        JSONObject jsonObjAction = new JSONObject();
        JSONObject jsonObjArgs = new JSONObject();

        for ( int i = 0; i < args.length; i += 2 ) {
            if ( SUPPORTED_OBJECT_CLASSES.contains(args[i + 1].getClass()) ) {
                jsonObjArgs.put((String) args[i], args[i + 1]);
            } else {
                throw new DbcException("Statistics logging only supports specific types");
            }
        }

        jsonObjAction.put("action", action);
        jsonObjAction.put("args", jsonObjArgs);

        return jsonObjAction.toString();
    }
}
