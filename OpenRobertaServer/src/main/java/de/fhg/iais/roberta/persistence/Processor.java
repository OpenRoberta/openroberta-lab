package de.fhg.iais.roberta.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Util;

public abstract class Processor {
    private static final Logger LOG = LoggerFactory.getLogger(Processor.class);

    private boolean successful;
    private String message;

    /**
     * remember whether the command was successful or not. The message is LOG-ged anyway. The text " Success: true/false" is appended.
     */
    public final void setResult(boolean successful, String message) {
        this.successful = successful;
        this.message = successful ? null : message;
        LOG.info(message + " Success: " + successful);
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