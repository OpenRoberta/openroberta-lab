package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.TmpPassword;
import de.fhg.iais.roberta.persistence.dao.TmpPasswordDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class TmpPasswordProcessor extends AbstractProcessor {

    public TmpPasswordProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public TmpPassword createTmpPassword(int userId) throws Exception {
        if ( userId <= 0 ) {
            setError(Key.USER_PASSWORD_RECOVERY_GENERATE_TMP_PASSWORD_USERID_ERROR, String.valueOf(userId));
            return null;
        } else {
            TmpPasswordDao tmpPasswordDao = new TmpPasswordDao(this.dbSession);
            TmpPassword tmpPassword = tmpPasswordDao.persistTmpPassword(userId);
            if ( tmpPassword != null ) {
                setSuccess(Key.USER_PASSWORD_RECOVERY_GENERATE_TMP_PASSWORD_SUCCESS);
                return tmpPassword;
            } else {
                setError(Key.USER_PASSWORD_RECOVERY_GENERATE_TMP_PASSWORD_USERID_NOT_SAVED_IN_DATABASE, String.valueOf(userId));
                return null;
            }
        }
    }

    public boolean validateTmpPassword(int userId, String password) throws Exception {
        TmpPasswordDao tmpPasswordDao = new TmpPasswordDao(this.dbSession);
        TmpPassword tmpPassword = tmpPasswordDao.load(userId);
        if ( tmpPassword != null && tmpPassword.isPasswordCorrect(password) ) {
            //setSuccess(Key.);
            return true;
        } else {

            return false;
        }

    }

}