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
            if ( forRequest.isTraceEnabled() ) {
                if ( SHORT_LOG ) {
                    String requestString = request.toString();
                    forRequest.trace("first 120 char of request: " + requestString.substring(0, Math.min(120, requestString.length())));
                } else {
                    forRequest.trace("request: " + request);
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
                    String logLine = logs.getString(i);
                    LOG.info("log entry: " + logLine);
                    // currently no way to catch false case
                    if ( logLine.contains("simImport clicked") ) {
                        Statistics.info("SimulationBackgroundUploaded", "success", true);
                    } else if ( logLine.contains("language switched to") ) {
                        Statistics.info("LanguageChanged", "success", true, "newLanguage", logLine.substring(logLine.length() - 2));
                    } else if ( logLine.contains("help clicked") ) {
                        // this is the help on the top menu, not the one on the right side
                        Statistics.info("HelpClicked", "success", true);
                    } else if ( logLine.contains("ProgramExport") ) {
                        Statistics.info("ProgramExport", "success", true);
                    } else if ( logLine.contains("ProgramNew") ) {
                        Statistics.info("ProgramNew", "success", true);
                    } else if ( logLine.contains("ProgramLinkShare") ) {
                        Statistics.info("ProgramLinkShare", "success", true);
                    }
                }
            }
        } catch ( Exception e ) {
            LOG.error("Exception caught when the client payload of the request JSONObject was logged", e);
        }
    }
}