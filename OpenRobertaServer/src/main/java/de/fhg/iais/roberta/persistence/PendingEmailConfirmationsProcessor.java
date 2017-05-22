package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.PendingEmailConfirmations;
import de.fhg.iais.roberta.persistence.dao.PendingEmailConfirmationsDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class PendingEmailConfirmationsProcessor extends AbstractProcessor {
    public PendingEmailConfirmationsProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public PendingEmailConfirmations createEmailConfirmation(int userId) throws Exception {
        if ( userId <= 0 ) {
            setError(Key.USER_CREATE_ERROR_GENERATE_CONFIRMATION, String.valueOf(userId));
            return null;
        } else {
            PendingEmailConfirmationsDao confirmationsDao = new PendingEmailConfirmationsDao(this.dbSession);
            PendingEmailConfirmations confirmation = confirmationsDao.persistConfirmation(userId);
            if ( confirmation != null ) {
                setSuccess(Key.USER_CREATE_GENERATE_CONFIRMATION_SUCCESS);
                return confirmation;
            } else {
                setError(Key.USER_CREATE_GENERATE_CONFIRMATION_URL_USERID_NOT_SAVED_IN_DATABASE, String.valueOf(userId));
                return null;
            }
        }
    }

    public PendingEmailConfirmations loadConfirmation(String urlPostfix) throws Exception {
        PendingEmailConfirmationsDao confirmationsDao = new PendingEmailConfirmationsDao(this.dbSession);
        PendingEmailConfirmations confirmation = confirmationsDao.loadConfirmationByUrl(urlPostfix);
        if ( confirmation != null ) {
            return confirmation;
        } else {
            setError(Key.USER_ACTIVATION_INVALID_URL, urlPostfix);
            return null;
        }
    }

    public void deleteEmailConfirmation(String urlPostfix) throws Exception {
        PendingEmailConfirmationsDao confirmationsDao = new PendingEmailConfirmationsDao(this.dbSession);
        PendingEmailConfirmations confirmation = confirmationsDao.loadConfirmationByUrl(urlPostfix);
        if ( confirmation != null ) {
            confirmationsDao.deleteConfirmation(confirmation);
        }
    }
}