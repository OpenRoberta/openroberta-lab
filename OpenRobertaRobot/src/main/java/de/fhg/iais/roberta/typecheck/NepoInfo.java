package de.fhg.iais.roberta.typecheck;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * describes either an error or a problem w.r.t. a phrase of the AST. Collections of objects of this class are stored in the mutable part of the phrase (within
 * a phrase the tree structure is immutable, but the attachment of problems is (more) dynamic.
 *
 * @author rbudde
 */
public class NepoInfo {
    private static final Logger LOG = LoggerFactory.getLogger(NepoInfo.class);

    private final Severity severity;
    private final String message;

    private NepoInfo(Severity severity, String message) {
        this.severity = severity;
        this.message = message;
    }

    public static NepoInfo error(String message) {
        return new NepoInfo(Severity.ERROR, message);
    }

    public static NepoInfo warning(String message) {
        return new NepoInfo(Severity.WARNING, message);
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public String getMessage() {
        return this.message;
    }

    public JSONObject getAsJson() {
        JSONObject nepoInfoJSON = new JSONObject();
        try {
            nepoInfoJSON.put(this.getSeverity().toString(), this.message);
        } catch ( JSONException e ) {
            LOG.error("NepoInfo to JSON failed", e);
        }
        return nepoInfoJSON;
    }

    @Override
    public String toString() {
        return "NepoProblem [" + this.severity + ": " + this.message + "]";
    }

    public static enum Severity {
        WARNING, ERROR;
    }
}
