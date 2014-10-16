package de.fhg.iais.roberta.util;

import java.sql.Timestamp;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.AbstractProcessor;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    public static final String SERVER_ERROR = "Server error. Operation aborted.";

    private Util() {
        // no objects
    }

    /**
     * Check whether a String is a valid Java identifier
     *
     * @param s String to check
     * @return <code>true</code> if the given String is a valid Java
     *         identifier; <code>false</code> otherwise.
     */
    public final static boolean isValidJavaIdentifier(String s) {
        if ( s == null || s.length() == 0 || !Character.isJavaIdentifierStart(s.charAt(0)) ) {
            return false;
        }
        char[] a = s.toCharArray();
        for ( int i = 1; i < a.length; i++ ) {
            if ( !Character.isJavaIdentifierPart(a[i]) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * get the actual date as timestamp
     *
     * @return the actual date as timestamp
     */
    public static Timestamp getNow() {
        return new Timestamp(new Date().getTime());
    }

    public static void logServerError(String detailMessage) {
        LOG.error(SERVER_ERROR + " Detail message: " + detailMessage, new Throwable());
    }

    public static void addResultInfo(JSONObject response, AbstractProcessor processor) throws JSONException {
        if ( processor.wasSuccessful() ) {
            response.put("rc", "ok");
        } else {
            response.put("rc", "ERROR");
            response.put("cause", processor.getErrorMessage());
        }
    }
}
