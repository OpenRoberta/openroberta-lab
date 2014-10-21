package de.fhg.iais.roberta.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.javaServer.resources.OpenRobertaSessionState;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.util.Util;

public abstract class AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractProcessor.class);

    protected final SessionWrapper dbSession;
    protected final OpenRobertaSessionState httpSessionState;

    private boolean successful;
    private String message;

    protected AbstractProcessor(SessionWrapper dbSession, OpenRobertaSessionState httpSessionState) {
        this.dbSession = dbSession;
        this.httpSessionState = httpSessionState;
    }

    /**
     * remember whether the command was successful or not. The message is LOG-ged anyway. The text " Success: true/false" is appended.
     */
    public final void setResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message + " Result: " + successful;
        LOG.info(this.message);
    }

    /**
     * the command was successful. Remember that.
     */
    public final void setSuccess() {
        this.successful = true;
        this.message = null;
    }

    /**
     * the command failed.Remember that.
     */
    public final void setSuccess(String message) {
        this.successful = true;
        this.message = message;
        LOG.error("Success: " + message);
    }

    /**
     * the command failed.Remember that.
     */
    public final void setError(String message) {
        this.successful = false;
        this.message = message;
        LOG.error("Error: " + message);
    }

    /**
     * gets the return code of a processor command
     *
     * @return true if command was successful, false otherwise
     */
    public final boolean wasSuccessful() {
        return this.successful;
    }

    /**
     * get the message, describing why the command was not successful<br>
     * TODO: replace concrete messages by message keys
     *
     * @return the error message
     */
    public final String getErrorMessage() {
        if ( this.message == null ) {
            Util.logServerError("error message missing");
            return Util.SERVER_ERROR;
        } else {
            return this.message;
        }
    }
}