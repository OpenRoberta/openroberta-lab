package de.fhg.iais.roberta.persistence;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.persistence.bo.PendingEmailConfirmations;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.PendingEmailConfirmationsDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;

public class PendingEmailConfirmationsProcessor extends AbstractProcessor {
    public PendingEmailConfirmationsProcessor(DbSession dbSession, int userId) {
        super(dbSession, userId);
    }

    public PendingEmailConfirmations createEmailConfirmation(User user) throws Exception {
        Assert.notNull(user);
        String account = user.getAccount();
        int userId = user.getId();
        if ( account == null || account.equals("") ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("ACCOUNT", account);
            setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, processorParameters);
            return null;
        } else {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("USER_ID", String.valueOf(userId));
            if ( userId <= 0 ) {
                setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_GENERATE_CONFIRMATION, processorParameters);
                return null;
            } else {
                PendingEmailConfirmationsDao confirmationsDao = new PendingEmailConfirmationsDao(this.dbSession);
                PendingEmailConfirmations confirmation = confirmationsDao.persistConfirmation(userId);
                if ( confirmation != null ) {
                    setStatus(ProcessorStatus.SUCCEEDED, Key.USER_CREATE_GENERATE_CONFIRMATION_SUCCESS, new HashMap<>());
                    return confirmation;
                } else {
                    setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_GENERATE_CONFIRMATION_URL_USERID_NOT_SAVED_IN_DATABASE, processorParameters);
                    return null;
                }
            }
        }
    }

    public PendingEmailConfirmations loadConfirmation(String urlPostfix) throws Exception {
        PendingEmailConfirmationsDao confirmationsDao = new PendingEmailConfirmationsDao(this.dbSession);
        PendingEmailConfirmations confirmation = confirmationsDao.loadConfirmationByUrl(urlPostfix);
        if ( confirmation != null ) {
            return confirmation;
        } else {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("POSTFIX_URL", urlPostfix);
            setStatus(ProcessorStatus.FAILED, Key.USER_ACTIVATION_INVALID_URL, processorParameters);
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