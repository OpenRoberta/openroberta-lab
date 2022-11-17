package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;

public class UserProcessor extends AbstractProcessor {

    private static final String ILLEGAL_USER_NAME_CHARACTER_PATTERN_DEFINITION = "[^a-zA-Z0-9=+!?.,%#+&^@_\\- ]";
    public static Pattern ILLEGAL_USER_NAME_CHARACTER_PATTERN = Pattern.compile(ILLEGAL_USER_NAME_CHARACTER_PATTERN_DEFINITION, Pattern.CASE_INSENSITIVE);

    public UserProcessor(DbSession dbSession, int userId) {
        super(dbSession, userId);
    }

    public User getStandardUser(String account) {
        return getUser(null, account);
    }

    public User getUser(UserGroup userGroup, String account) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(userGroup, account);
        if ( user != null ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.USER_GET_ONE_SUCCESS, new HashMap<>());
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG, new HashMap<>());
        }
        return user;
    }

    /**
     * Returns a user with the given account name and password. Not for group members.
     *
     * @param account
     * @param password
     * @return the user matching the parameters; null, if errors occurred or user not found
     */
    public User getStandardUserForLogin(String account, String password) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(null, account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.USER_GET_ONE_SUCCESS, new HashMap<>());
            return user;
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG, new HashMap<>());
            return null;
        }
    }

    public User getGroupUserForLogin(UserGroup userGroup, String account, String password) throws Exception {
        if ( userGroup == null ) {
            setStatus(ProcessorStatus.FAILED, Key.GROUP_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
            return null;
        }
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(userGroup, account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.USER_GET_ONE_SUCCESS, new HashMap<>());
            return user;
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG, new HashMap<>());
            return null;
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

    /**
     * Create a new user. If data is invalid or the email is already used, set error status.<br><br>
     * Not used to create user group members. The UserGroupProcessor is used for that.
     *
     * @param account not null, not empty
     * @param password not null, not empty
     * @param userName may be null
     * @param role not null, not empty
     * @param email not null, may be empty
     * @param tags may be null
     * @param youngerThen14 boolean flag
     * @return the user; maybe null, if the creation failed
     */
    public User createUser(String account, String password, String userName, String role, String email, String tags, boolean youngerThen14) throws Exception {
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("ACCOUNT", account);
        processorParameters.put("USER_NAME", userName);

        if ( account == null || account.equals("") || password == null || password.equals("") ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_MISSING_REQ_FIELDS, processorParameters);
            return null;
        }
        Matcher acc_symbols = ILLEGAL_USER_NAME_CHARACTER_PATTERN.matcher(account);
        boolean account_check = acc_symbols.find();
        Matcher userName_symbols = ILLEGAL_USER_NAME_CHARACTER_PATTERN.matcher(userName);
        boolean userName_check = userName_symbols.find();
        if ( account_check || userName_check ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS, processorParameters);
            return null;
        } else if ( account.length() > 25 || userName.length() > 25 ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_ACCOUNT_LENGTH, processorParameters);
            return null;
        } else if ( email != null && !email.equals("") && !Util.isValidEmailAddress(email) ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_EMAIL_INVALID, processorParameters);
            return null;
        }
        UserDao userDao = new UserDao(this.dbSession);
        if ( email != null && !email.equals("") ) {
            User user = userDao.loadUserByEmail(email);
            if ( user != null ) {
                setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_EMAIL_USED, processorParameters);
                return null;
            }
        }

        User user = userDao.persistNewUser(null, account, password, role);
        if ( user == null ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_CREATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
            return null;
        }
        setStatus(ProcessorStatus.SUCCEEDED, Key.USER_CREATE_SUCCESS, new HashMap<>());
        user.setUserName(userName);
        user.setEmail(email);
        user.setTags(tags);
        user.setYoungerThen14(youngerThen14);
        return user;
    }

    public void updatePassword(String account, String oldPassword, String newPassword) throws Exception {
        if ( account == null || account.equals("") ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, new HashMap<>());
            return;
        }

        User user = isUserLoggedIn() ? getUser(getIdOfLoggedInUser()) : null;
        if ( user == null || !user.getAccount().equals(account) || !user.isPasswordCorrect(oldPassword) ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("ACCOUNT", account);
            this.setStatus(ProcessorStatus.FAILED, Key.USER_UPDATE_ERROR_NOT_SAVED_TO_DB, processorParameters);
            return;
        }

        user.setPassword(newPassword);
        this.setStatus(ProcessorStatus.SUCCEEDED, Key.USER_UPDATE_SUCCESS, new HashMap<>());
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

    /**
     * Updates the information of a user. Is not used to update user group members, since they are not able to update their accounts.
     *
     * @param user user to be uodated, not null
     * @param userName to be set in the data base
     * @param roleAsString to be set in the data base
     * @param email to be set in the data base
     * @param tags to be set in the data base
     * @param youngerThen14 to be set in the data base
     * @param deactivateAccount true, if the account has to be deactivated (until confirmation email will arrives in the future)
     * @throws Exception
     */
    public void updateUser(
        User user,
        String userName,
        String roleAsString,
        String email,
        String tags,
        boolean youngerThen14,
        boolean deactivateAccount) throws Exception //
    {
        Assert.isTrue(user != null && getIdOfLoggedInUser() == user.getId(), "user or userid invalid");
        user.setUserName(userName);
        user.setRole(Role.valueOf(roleAsString));
        user.setEmail(email);
        user.setTags(tags);
        user.setYoungerThen14(youngerThen14);
        if ( deactivateAccount ) {
            user.setActivated(false);
        }
        setStatus(ProcessorStatus.SUCCEEDED, Key.USER_UPDATE_SUCCESS, new HashMap<>());
    }

    /**
     * Deletes a user. Do not use for user group members
     *
     * @param account to be deleted
     * @param password of the account to be deleted
     * @throws Exception
     */
    public void deleteUser(String account, String password) throws Exception {
        UserDao userDao = new UserDao(this.dbSession);
        UserGroupDao userGroupDao = new UserGroupDao(this.dbSession);
        User user = userDao.loadUser(null, account);
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("ACCOUNT", account);

        if ( user == null || !user.isPasswordCorrect(password) || user.getUserGroup() != null ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_DELETE_ERROR_ID_NOT_FOUND, processorParameters);
            return;
        }

        if ( userGroupDao.getNumberOfGroupsOfOwner(user) > 0 ) {
            setStatus(ProcessorStatus.FAILED, Key.USER_DELETE_ERROR_HAS_GROUPS, new HashMap<>());
            return;
        }

        int rowCount = userDao.deleteUser(user);
        if ( rowCount > 0 ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.USER_DELETE_SUCCESS, new HashMap<>());
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_DELETE_ERROR_NOT_DELETED_IN_DB, processorParameters);
        }
    }

    /**
     * Deletes a set of members of a user group.
     *
     * @param userGroup The user group the members are belonging to.
     * @param memberAccounts A list of account names of the members of the group that shall be deleted.
     * @throws Exception
     */
    public void deleteUserGroupMembers(UserGroup userGroup, List<String> memberAccounts) {
        UserDao userDao = new UserDao(this.dbSession);
        if ( userGroup == null ) {
            //Necessary to not use this end point to delete global users
            setStatus(ProcessorStatus.FAILED, Key.GROUP_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
            return;
        }
        List<User> usersToDelete = new ArrayList<>();
        for ( String memberAccount : memberAccounts ) {
            User user = userDao.loadUser(userGroup, memberAccount);
            if ( user == null ) {
                continue;
            }
            usersToDelete.add(user);
        }
        for ( User user : usersToDelete ) {
            int rowCount = userDao.deleteUser(user);
            if ( rowCount == 0 ) {
                //Show an error, because there is most likely a database problem.
                Map<String, String> processorParameters = new HashMap<>();
                processorParameters.put("OWNER", userGroup.getOwner().getAccount());
                processorParameters.put("USERGROUP", userGroup.getName());
                processorParameters.put("MEMBER", user.getAccount());
                setStatus(ProcessorStatus.FAILED, Key.USER_DELETE_ERROR_NOT_DELETED_IN_DB, processorParameters);
                return;
            }
        }
        setStatus(ProcessorStatus.SUCCEEDED, Key.USER_DELETE_SUCCESS, new HashMap<>());
    }
}