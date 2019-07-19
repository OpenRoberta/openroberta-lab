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

    public void log(Logger forRequest, JSONObject request) {
        try {
            if ( forRequest.isDebugEnabled() ) {
                if ( SHORT_LOG ) {
                    String requestString = request.toString();
                    forRequest.debug("first 120 char of request: " + requestString.substring(0, Math.min(120, requestString.length())));
                } else {
                    forRequest.debug("request: " + request);
                }
            }
        } catch ( Exception e ) {
            LOG.info("Exception caught when the request JSONObject was logged", e);
        }
        try {
            JSONArray logs = request.getJSONArray("log");
            int logLength = logs.length();
            if ( logLength > 0 ) {
                for ( int i = 0; i < logLength; i++ ) {
                    LOG.info("log entry: " + logs.getString(i));
                    if ( logs.getString(i).contains("simImport clicked") ) {
                        Statistics.info("SimulationBackgroundUploaded");
                    }
                }
            }
        } catch ( Exception e ) {
            LOG.error("Exception caught when the client payload of the request JSONObject was logged", e);
        }
    }
}