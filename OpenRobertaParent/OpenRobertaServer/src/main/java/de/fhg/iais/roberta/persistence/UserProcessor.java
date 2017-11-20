package de.fhg.iais.roberta.persistence;

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
            setSuccess(Key.USER_GET_ONE_SUCCESS);
            return user;
        } else {
            setError(Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
            return null;
        }
    }

    public User getUser(String account, String password) throws Exception {
        Pattern p = Pattern.compile("[^a-zA-Z0-9=+!?.,%#+&^@_\\- ]", Pattern.CASE_INSENSITIVE);
        Matcher acc_symbols = p.matcher(account);
        boolean account_check = acc_symbols.find();
        if ( account_check ) {
            setError(Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, account);
            return null;
        } else {
            UserDao userDao = new UserDao(this.dbSession);
            User user = userDao.loadUser(account);
            if ( user != null && user.isPasswordCorrect(password) && !account_check ) {
                setSuccess(Key.USER_GET_ONE_SUCCESS);
                return user;
            } else {
                setError(Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
                return null;
            }
        }
    }

    public User getUserByEmail(String email) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUserByEmail(email);
        if ( user != null ) {
            setSuccess(Key.USER_EMAIL_ONE_SUCCESS);
            return user;
        } else {
            setError(Key.USER_EMAIL_ONE_ERROR_USER_NOT_EXISTS_WITH_THIS_EMAIL);
            return null;
        }
    }

    public User getUser(int id) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(id);
        if ( user != null ) {
            setSuccess(Key.USER_GET_ONE_SUCCESS);
            return user;
        } else {
            setError(Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
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
        if ( account == null || account.equals("") || password == null || password.equals("") ) {
            setError(Key.USER_CREATE_ERROR_MISSING_REQ_FIELDS, account);
        } else if ( account_check || userName_check ) {
            setError(Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, account, userName);
        } else if ( account.length() > 25 || userName.length() > 25 ) {
            setError(Key.USER_CREATE_ERROR_ACCOUNT_LENGTH, account, userName);
        } else {
            if ( !isMailUsed(account, email) ) {
                UserDao userDao = new UserDao(this.dbSession);
                User user = userDao.persistUser(account, password, roleAsString);
                if ( user != null ) {
                    setSuccess(Key.USER_CREATE_SUCCESS);
                    user.setUserName(userName);
                    user.setEmail(email);
                    user.setTags(tags);
                    user.setYoungerThen14(youngerThen14);
                } else {
                    setError(Key.USER_CREATE_ERROR_NOT_SAVED_TO_DB, account);
                }
            }
        }
    }

    private boolean isMailUsed(String account, String email) {
        UserDao userDao = new UserDao(this.dbSession);
        if ( !email.equals("") ) {
            User user = userDao.loadUserByEmail(email);
            if ( user != null && !user.getAccount().equals(account) ) {
                setError(Key.USER_ERROR_EMAIL_USED, account);
                return true;
            }
        }
        return false;
    }

    public void updatePassword(String account, String oldPassword, String newPassword) throws Exception {
        if ( account == null || account.equals("") ) {
            setError(Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, account);
        } else {
            User user = getUser(account, oldPassword);
            if ( user != null && this.httpSessionState.getUserId() == user.getId() ) {
                user.setPassword(newPassword);
                setSuccess(Key.USER_UPDATE_SUCCESS);
            } else {
                setError(Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, account);
            }
        }
    }

    public void resetPassword(int userID, String newPassword) throws Exception {
        if ( userID <= 0 ) {
            setError(Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, String.valueOf(userID));
        } else {
            User user = getUser(userID);
            if ( user != null ) {
                user.setPassword(newPassword);
                setSuccess(Key.USER_UPDATE_SUCCESS);
            } else {
                setError(Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, String.valueOf(userID));
            }
        }
    }

    public void activateAccount(int userID) throws Exception {
        if ( userID <= 0 ) {
            setError(Key.USER_ACTIVATION_WRONG_ACCOUNT, String.valueOf(userID));
        } else {
            User user = getUser(userID);
            if ( user != null ) {
                user.setActivated(true);
                setSuccess(Key.USER_ACTIVATION_SUCCESS);
            } else {
                setError(Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, String.valueOf(userID));
            }
        }
    }

    public void deactivateAccount(int userID) throws Exception {
        if ( userID <= 0 ) {
            setError(Key.USER_ACTIVATION_WRONG_ACCOUNT, String.valueOf(userID));
        } else {
            User user = getUser(userID);
            if ( user != null ) {
                user.setActivated(false);
                setSuccess(Key.USER_DEACTIVATION_SUCCESS);
            } else {
                setError(Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, String.valueOf(userID));
            }
        }
    }

    public void updateUser(String account, String userName, String roleAsString, String email, String tags, boolean youngerThen14) throws Exception {
        if ( account == null || account.equals("") ) {
            setError(Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, account);
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
                    setSuccess(Key.USER_UPDATE_SUCCESS);
                }
            } else {
                setError(Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, account);
            }
        }
    }

    public void deleteUser(String account, String password) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            int rowCount = userDao.deleteUser(user);
            if ( rowCount > 0 ) {
                setSuccess(Key.USER_DELETE_SUCCESS);
            } else {
                setError(Key.USER_DELETE_ERROR_NOT_DELETED_IN_DB, account);
            }
        } else {
            setError(Key.USER_DELETE_ERROR_ID_NOT_FOUND, account);
        }
    }
}
