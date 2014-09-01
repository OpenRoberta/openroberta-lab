package de.fhg.iais.roberta.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientLogger
{
    private static final Logger LOG = LoggerFactory.getLogger(ClientLogger.class);

    public ClientLogger() {
    }

    public int log(JSONObject request) {
        int logLength = 0;
        try {
            JSONArray logs = request.getJSONArray("log");
            logLength = logs.length();
            for ( int i = 0; i < logLength; i++ ) {
                LOG.info("log entry: " + logs.getString(i));
            }
            LOG.info(logLength + (logLength == 1 ? " log entry" : " log entries") + " written");
        } catch ( Exception e ) {
            String msg = e.getMessage();
            if ( msg.indexOf("JSONObject[\"log\"]") != -1 ) {
                LOG.error("the request JSONObject has either no 'log' property or its value is no JSONArray");
            } else {
                LOG.info("Exception caught when the request JSONObject was processed", e);
            }
        }
        return logLength;
    }

}
