package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;

public class UserProcessor extends AbstractProcessor {

    public UserProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public User getUser(String account, String password) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            setSuccess("user.get_one.success");
            return user;
        } else {
            setError("user.get_one.error.id_or_password_wrong");
            return null;
        }
    }

    public void saveUser(String account, String password, String roleAsString, String email, String tags) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.persistUser(account, password, roleAsString);
        if ( user != null ) {
            setSuccess("user.create.success");
            user.setEmail(email);
            user.setTags(tags);
        } else {
            setError("user.create.error.not_saved_to_db", account);
        }
    }

    public void deleteUser(String account, String password) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            int rowCount = userDao.deleteUser(user);
            if ( rowCount > 0 ) {
                setSuccess("user.delete.success");
            } else {
                setError("user.delete.error.not_deleted_in_db", account);
            }
        } else {
            setError("user.delete.error.id_not_found", account);
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
        setSuccess("user.get_all.success");
        return usersJSONArray;
    }
}
