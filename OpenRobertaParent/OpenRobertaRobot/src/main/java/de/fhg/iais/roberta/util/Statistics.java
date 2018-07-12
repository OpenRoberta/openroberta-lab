package de.fhg.iais.roberta.util;

import de.fhg.iais.roberta.util.dbc.DbcException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Statistics {
    private static final Logger LOG = LoggerFactory.getLogger("statistics");

    private Statistics() {}

    /**
     * Logs the action and its arguments to the statistics logger with the trace level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param  action  the general action or category of the log
     * @param  args variable number of key value pairs, these are put in a json array
     */
    public static void trace(String action, String... args) {
        LOG.trace(toJsonString(action, args));
    }

    /**
     * Logs the action and its arguments to the statistics logger with the debug level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param  action  the general action or category of the log
     * @param  args variable number of key value pairs, these are put in a json array
     */
    public static void debug(String action, String... args) {
        LOG.debug(toJsonString(action, args));
    }

    /**
     * Logs the action and its arguments to the statistics logger with the info level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param  action  the general action or category of the log
     * @param  args variable number of key value pairs, these are put in a json array
     */
    public static void info(String action, String... args) {
        LOG.info(toJsonString(action, args));
    }

    /**
     * Logs the action and its arguments to the statistics logger with the warn level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param  action  the general action or category of the log
     * @param  args variable number of key value pairs, these are put in a json array
     */
    public static void warn(String action, String... args) {
        LOG.warn(toJsonString(action, args));
    }

    /**
     * Logs the action and its arguments to the statistics logger with the error level.
     * The arguments must come in key value pairs, otherwise the internal toJsonString method
     * throws a DbcException.
     *
     * @param  action  the general action or category of the log
     * @param  args variable number of key value pairs, these are put in a json array
     */
    public static void error(String action, String... args) {
        LOG.error(toJsonString(action, args));
    }

    private static String toJsonString(String action, String... args) {
        if (args.length % 2 != 0) {
            throw new DbcException("Statistics logging arguments must come in pairs of two!");
        }

        JSONObject jsonObjAction = new JSONObject();
        JSONObject jsonObjArgs = new JSONObject();
        JSONArray jsonArrArgs = new JSONArray();

        for (int i = 0; i < args.length; i+=2) {
            jsonObjArgs.put(args[i], args[i+1]);
        }

        jsonArrArgs.put(jsonObjArgs);
        jsonObjAction.put("args", jsonArrArgs);
        jsonObjAction.put("action", action);

        return jsonObjAction.toString();
    }
}
