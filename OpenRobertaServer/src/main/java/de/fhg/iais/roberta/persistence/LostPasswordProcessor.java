package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.LostPassword;
import de.fhg.iais.roberta.persistence.dao.LostPasswordDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class LostPasswordProcessor extends AbstractProcessor {
    public LostPasswordProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public LostPassword createLostPassword(int userId) throws Exception {
        if ( userId <= 0 ) {
            setError(Key.USER_PASSWORD_RECOVERY_GENERATE_URL_USERID_ERROR, String.valueOf(userId));
            return null;
        } else {
            LostPasswordDao lostPasswordDao = new LostPasswordDao(this.dbSession);
            LostPassword lostPassword = lostPasswordDao.persistLostPassword(userId);
            if ( lostPassword != null ) {
                setSuccess(Key.USER_PASSWORD_RECOVERY_GENERATE_URL_SUCCESS);
                return lostPassword;
            } else {
                setError(Key.USER_PASSWORD_RECOVERY_GENERATE_URL_USERID_NOT_SAVED_IN_DATABASE, String.valueOf(userId));
                return null;
            }
        }
    }

    public LostPassword loadLostPassword(String urlPostfix) throws Exception {
        LostPasswordDao lostPasswordDao = new LostPasswordDao(this.dbSession);
        LostPassword lostPassword = lostPasswordDao.loadLostPassword(urlPostfix);
        if ( lostPassword != null ) {
            return lostPassword;
        } else {
            setError(Key.USER_PASSWORD_RECOVERY_INVALID_URL, urlPostfix);
            return null;
        }

    }

}