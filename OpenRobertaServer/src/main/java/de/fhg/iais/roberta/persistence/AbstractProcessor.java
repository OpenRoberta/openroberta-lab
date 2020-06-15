package de.fhg.iais.roberta.persistence;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * super class of the various processor implementations (program, user, ...). The processor objects are light-weight.<br>
 * A processor object should be used for one action only. Otherwise the success/error logic is hard to manage ...
 *
 * @author rbudde
 */
public abstract class AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractProcessor.class);

    protected final DbSession dbSession;
    protected final int idOfLoggedInUser;

    private Key message;
    private ProcessorStatus status;
    private Map<String, String> parameters;

    protected AbstractProcessor(DbSession dbSession, int idOfLoggedInUser) {
        this.dbSession = dbSession;
        this.idOfLoggedInUser = idOfLoggedInUser;
    }

    protected boolean isUserLoggedIn() {
        return this.idOfLoggedInUser >= 1;
    }

    protected int getIdOfLoggedInUser() {
        return this.idOfLoggedInUser;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public JSONObject getParameters() {
        return new JSONObject(this.parameters);
    }

    public final void setStatus(ProcessorStatus status, Key message, Map<String, String> parameters) {
        this.status = status;
        this.message = message;
        this.parameters = parameters;
        switch ( status ) {
            case SUCCEEDED:
                AbstractProcessor.LOG.info("Processor succeeded: " + message);
                break;
            case FAILED:
                AbstractProcessor.LOG.info("Processor failed: " + message);
                break;
            default:
                AbstractProcessor.LOG.info("Illegal processor state passed: " + message);
                throw new DbcException("Illegal processor state passed: " + message);
        }
    }

    /**
     * Returns the state of the processor, either SUCCEEDED or FAILED
     *
     * @return {@link ProcessorStatus}
     */
    public final ProcessorStatus getStatus() {
        return this.status;
    }

    /**
     * Check if processor state is SUCCEEDED or FAILED
     *
     * @return return true if SUCCEEDED, false if FAILED and throw an {@link DbcException} otherwise
     */

    public final boolean succeeded() {
        switch ( this.status ) {
            case SUCCEEDED:
                return true;
            case FAILED:
                return false;
            default:
                AbstractProcessor.LOG.info("Illegal processor state passed: " + this.message);
                throw new DbcException("Illegal processor state passed: " + this.message);
        }
    }

    /**
     * get the message key, describing why the command was not successful
     *
     * @return the error message key
     */
    public final Key getMessage() {
        if ( this.message == null ) {
            AbstractProcessor.LOG.error("error message missing. Returning server error.");
            return Key.SERVER_ERROR;
        } else {
            return this.message;
        }
    }
}