package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Util;

public class UserProcessor extends AbstractProcessor {

    public UserProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public User getUser(String account, String password) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            setSuccess(Util.USER_GET_ONE_SUCCESS);
            return user;
        } else {
            setError(Util.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG);
            return null;
        }
    }

    public void saveUser(String account, String password, String roleAsString, String email, String tags) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.persistUser(account, password, roleAsString);
        if ( user != null ) {
            setSuccess(Util.USER_CREATE_SUCCESS);
            user.setEmail(email);
            user.setTags(tags);
        } else {
            setError(Util.USER_CREATE_ERROR_NOT_SAVED_TO_DB, account);
        }
    }

    public void deleteUser(String account, String password) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            int rowCount = userDao.deleteUser(user);
            if ( rowCount > 0 ) {
                setSuccess(Util.USER_DELETE_SUCCESS);
            } else {
                setError(Util.USER_DELETE_ERROR_NOT_DELETED_IN_DB, account);
            }
        } else {
            setError(Util.USER_DELETE_ERROR_ID_NOT_FOUND, account);
        }
    }

    @Deprecated
    public JSONArray getUsers(String sortBy, int offset, String tagFilter) throws JSONException {
        UserDao userDao = new UserDao(this.dbSession);
        List<User> userList = userDao.loadUserList(sortBy, offset, tagFilter);
        JSONArray usersJSONArray = new JSONArray();

        for ( User user : userList ) {
            JSONObject userJSON = new JSONObject();
            if ( user != null ) {
                userJSON.put("id", user.getId());
                userJSON.put("name", user.getAccount());
                userJSON.put("role", user.getRole()); // This will be changed to user rights
                usersJSONArray.put(userJSON);
            }
        }
        setSuccess(Util.USER_GET_ALL_SUCCESS);
        return usersJSONArray;
    }
}
