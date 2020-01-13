package de.fhg.iais.roberta.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class UserProcessor extends AbstractProcessor {

    public UserProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public User getUser(String account) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        if ( user != null ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.USER_GET_ONE_SUCCESS, new HashMap<>());
            return user;
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG, new HashMap<>());
            return null;
        }
    }

    public User getUser(String account, String password) throws Exception {
        Pattern p = Pattern.compile("[^a-zA-Z0-9=+!?.,%#+&^@_\\- ]", Pattern.CASE_INSENSITIVE);
        Matcher acc_symbols = p.matcher(account);
        boolean account_check = acc_symbols.find();
        if ( account_check ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("ACCOUNT", account);
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, processorParameters);
            return null;
        } else {
            UserDao userDao = new UserDao(this.dbSession);
            User user = userDao.loadUser(account);
            if ( user != null && user.isPasswordCorrect(password) ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.USER_GET_ONE_SUCCESS, new HashMap<>());
                return user;
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG, new HashMap<>());
                return null;
            }
        }
    }

    public User getUserByEmail(String email) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUserByEmail(email);
        if ( user != null ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.USER_EMAIL_ONE_SUCCESS, new HashMap<>());
            return user;
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_EMAIL_ONE_ERROR_USER_NOT_EXISTS_WITH_THIS_EMAIL, new HashMap<>());
            return null;
        }
    }

    public User getUser(int id) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(id);
        if ( user != null ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.USER_GET_ONE_SUCCESS, new HashMap<>());
            return user;
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG, new HashMap<>());
            return null;
        }
    }

    public void createUser(String account, String password, String userName, String roleAsString, String email, String tags, boolean youngerThen14)
        throws Exception {
        Pattern p = Pattern.compile("[^a-zA-Z0-9=+!?.,%#+&^@_\\- ]", Pattern.CASE_INSENSITIVE);
        Matcher acc_symbols = p.matcher(account);
        boolean account_check = acc_symbols.find();
        Matcher userName_symbols = p.matcher(userName);
        boolean userName_check = userName_symbols.find();
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("ACCOUNT", account);
        processorParameters.put("USER_NAME", userName);
        if ( account == null || account.equals("") || password == null || password.equals("") ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_MISSING_REQ_FIELDS, processorParameters);
        } else if ( account_check || userName_check ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, processorParameters);
        } else if ( account.length() > 25 || userName.length() > 25 ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_ACCOUNT_LENGTH, processorParameters);
        } else {
            if ( !isMailUsed(account, email) ) {
                UserDao userDao = new UserDao(this.dbSession);
                User user = userDao.persistUser(account, password, roleAsString);
                if ( user != null ) {
                    setStatus(ProcessorStatus.SUCCEEDED, Key.USER_CREATE_SUCCESS, new HashMap<>());
                    user.setUserName(userName);
                    user.setEmail(email);
                    user.setTags(tags);
                    user.setYoungerThen14(youngerThen14);
                } else {
                    setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
                }
            }
        }
    }

    public void updatePassword(String account, String oldPassword, String newPassword) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("ACCOUNT", account);
        if ( account == null || account.equals("") ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, processorParameters);
        } else {
            User user = getUser(account, oldPassword);
            if ( user != null && this.httpSessionState.getUserId() == user.getId() ) {
                user.setPassword(newPassword);
                setStatus(ProcessorStatus.SUCCEEDED, Key.USER_UPDATE_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
            }
        }
    }

    public void resetPassword(int userID, String newPassword) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USER_ID", String.valueOf(userID));
        if ( userID <= 0 ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, processorParameters);
        } else {
            User user = getUser(userID);
            if ( user != null ) {
                user.setPassword(newPassword);
                setStatus(ProcessorStatus.SUCCEEDED, Key.USER_UPDATE_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
            }
        }
    }

    public void activateAccount(int userID) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USER_ID", String.valueOf(userID));
        if ( userID <= 0 ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_ACTIVATION_WRONG_ACCOUNT, processorParameters);
        } else {
            User user = getUser(userID);
            if ( user != null ) {
                user.setActivated(true);
                setStatus(ProcessorStatus.SUCCEEDED, Key.USER_ACTIVATION_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
            }
        }
    }

    public void deactivateAccount(int userID) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("USER_ID", String.valueOf(userID));
        if ( userID <= 0 ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_ACTIVATION_WRONG_ACCOUNT, processorParameters);
        } else {
            User user = getUser(userID);
            if ( user != null ) {
                user.setActivated(false);
                setStatus(ProcessorStatus.SUCCEEDED, Key.USER_DEACTIVATION_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
            }
        }
    }

    public void updateUser(String account, String userName, String roleAsString, String email, String tags, boolean youngerThen14) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("ACCOUNT", account);
        if ( account == null || account.equals("") ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, processorParameters);
        } else {
            UserDao userDao = new UserDao(this.dbSession);
            User user = userDao.loadUser(account);
            if ( user != null && this.httpSessionState.getUserId() == user.getId() ) {
                if ( !isMailUsed(account, email) ) {
                    user.setUserName(userName);
                    user.setRole(Role.valueOf(roleAsString));
                    user.setEmail(email);
                    user.setTags(tags);
                    user.setYoungerThen14(youngerThen14);
                    setStatus(ProcessorStatus.SUCCEEDED, Key.USER_UPDATE_SUCCESS, new HashMap<>());
                }
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
            }
        }
    }

    public void deleteUser(String account, String password) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("ACCOUNT", account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            int rowCount = userDao.deleteUser(user);
            if ( rowCount > 0 ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.USER_DELETE_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, Key.USER_DELETE_ERROR_NOT_DELETED_IN_DB, processorParameters);
            }
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_DELETE_ERROR_ID_NOT_FOUND, processorParameters);
        }
    }

    private boolean isMailUsed(String account, String email) {
        UserDao userDao = new UserDao(this.dbSession);
        if ( !email.equals("") ) {
            User user = userDao.loadUserByEmail(email);
            if ( user != null && !user.getAccount().equals(account) ) {
                Map<String, String> processorParameters = new HashMap<>();
                processorParameters.put("ACCOUNT", account);
                setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_EMAIL_USED, processorParameters);
                return true;
            }
        }
        return false;
    }
}
