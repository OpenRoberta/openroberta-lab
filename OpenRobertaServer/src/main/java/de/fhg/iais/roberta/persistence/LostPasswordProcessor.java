package de.fhg.iais.roberta.persistence;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.persistence.bo.LostPassword;
import de.fhg.iais.roberta.persistence.dao.LostPasswordDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class LostPasswordProcessor extends AbstractProcessor {
    public LostPasswordProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState.getUserId());
    }

    public LostPassword createLostPassword(int userId) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USER_ID", String.valueOf(userId));
        if ( userId <= 0 ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_PASSWORD_RECOVERY_GENERATE_URL_USERID_ERROR, processorParameters);
            return null;
        } else {
            LostPasswordDao lostPasswordDao = new LostPasswordDao(this.dbSession);
            LostPassword lostPassword = lostPasswordDao.persistLostPassword(userId);
            if ( lostPassword != null ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.USER_PASSWORD_RECOVERY_GENERATE_URL_SUCCESS, new HashMap<>());
                return lostPassword;
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_PASSWORD_RECOVERY_GENERATE_URL_USERID_NOT_SAVED_IN_DATABASE, processorParameters);
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
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("POSTFIX_URL", urlPostfix);
            setStatus(ProcessorStatus.FAILED, Key.USER_PASSWORD_RECOVERY_INVALID_URL, processorParameters);
            return null;
        }
    }

    public void deleteLostPassword(String urlPostfix) throws Exception {
        LostPasswordDao lostPasswordDao = new LostPasswordDao(this.dbSession);
        LostPassword lostPassword = lostPasswordDao.loadLostPassword(urlPostfix);
        if ( lostPassword != null ) {
            lostPasswordDao.deleteLostPassword(lostPassword);
        }
    }
}