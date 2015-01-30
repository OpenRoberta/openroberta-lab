package de.fhg.iais.roberta.persistence;

import org.codehaus.jettison.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public abstract class AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractProcessor.class);

    protected final DbSession dbSession;
    protected final HttpSessionState httpSessionState;

    private boolean success;
    private Key message;
    private String[] parameter;

    protected AbstractProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        this.dbSession = dbSession;
        this.httpSessionState = httpSessionState;
    }

    /**
     * the command failed. Remember that.
     */
    public final void setSuccess(Key message, String... parameter) {
        this.success = true;
        this.message = message;
        this.parameter = parameter;
        LOG.info("Success: " + message);
    }

    /**
     * the command failed. Remember that.
     */
    public final void setError(Key message, String... parameters) {
        this.success = false;
        this.message = message;
        this.parameter = parameters;
        LOG.error("Error. The error key is: " + message);
    }

    /**
     * gets the return code of a processor command
     *
     * @return the string describing success or error
     */
    public final String getRC() {
        return this.success ? "ok" : "error";
    }

    /**
     * get the message key, describing why the command was not successful
     *
     * @return the error message key
     */
    public final Key getMessage() {
        if ( this.message == null ) {
            LOG.error("error message missing. Returning server error.");
            return Key.SERVER_ERROR;
        } else {
            return this.message;
        }
    }

    /**
     * get the parameters for a message key, if they exist
     *
     * @return the parameters for a message key, null if they don't exist
     */
    public final JSONArray getParameter() {
        if ( this.parameter == null || this.parameter.length == 0 ) {
            return null;
        } else {
            JSONArray parametersJSONArray = new JSONArray();
            for ( String p : this.parameter ) {
                parametersJSONArray.put(p);
            }
            return parametersJSONArray;
        }
    }
}