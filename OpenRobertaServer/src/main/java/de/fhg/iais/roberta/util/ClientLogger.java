package de.fhg.iais.roberta.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientLogger {
    private static final Logger LOG = LoggerFactory.getLogger(ClientLogger.class);
    private static final boolean SHORT_LOG = true;

    public ClientLogger() {
    }

    public int log(Logger forRequest, JSONObject request) {
        try {
            if ( forRequest.isDebugEnabled() ) {
                if ( SHORT_LOG ) {
                    forRequest.debug("first 120 char of request: " + request.toString().substring(0, 120));
                } else {
                    forRequest.debug("request: " + request);
                }
            }
        } catch ( Exception e ) {
            LOG.info("Exception caught when the request JSONObject was logged", e);
        }
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
                LOG.info("Exception caught when the client payload of the request JSONObject was logged", e);
            }
        }
        return logLength;
    }
}