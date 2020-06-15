package de.fhg.iais.roberta.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientLogger {
    private static final Logger LOG = LoggerFactory.getLogger(ClientLogger.class);

    public ClientLogger() {
    }

    public void log(Logger forRequest, List<String> logData) {
        try {
            for ( String logLine : logData ) {
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
                } else if ( logLine.contains("tutorial executed") ) {
                    Statistics.info("TutorialExecuted", "success", true, "tutorial", logLine.substring(logLine.lastIndexOf(" ") + 1));
                }
            }
        } catch ( Exception e ) {
            LOG.error("Exception caught when the log data from the client was logged", e);
        }
    }
}