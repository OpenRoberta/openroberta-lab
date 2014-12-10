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
        setResult(user != null && user.isPasswordCorrect(password), "user loaded and password checked.");
        return wasSuccessful() ? user : null;
    }

    public void saveUser(String account, String password, String roleAsString, String email, String tags) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.persistUser(account, password, roleAsString);
        setResult(user != null, "user created with account " + account + ".");
        if ( wasSuccessful() ) {
            user.setEmail(email);
            user.setTags(tags);
        }
    }

    public User getUser(String account) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        setResult(user != null, "user loaded.");
        return user;
    }
    
    public JSONArray  getUsersJSONArray(String sortBy,int offset,String tagFilter) {
    	
        UserDao userDao = new UserDao(this.dbSession);
        List<User> userList = userDao.loadUserList(sortBy, offset, tagFilter);
        JSONArray usersJSONArray = new JSONArray();

        for(User user : userList){
        	
        	JSONObject userJSON = new JSONObject();
        	
            try {
                if(user != null){
            
                	userJSON.put("id",user.getId());
                	userJSON.put("name",user.getAccount());
                	userJSON.put("role",user.getRole()); // This will be changed to user rights
                             	usersJSONArray.put(userJSON);
                }
            } 
            catch (JSONException e) {
            	//Exception to be trough
            }
        }
        
        setResult(userList != null, "user list loaded.");
        return usersJSONArray;
    }
    
    public void deleteUserByAccount(String account, String password) {
        UserDao userDao = new UserDao(this.dbSession);
        User user = userDao.loadUser(account);
        if ( user != null && user.isPasswordCorrect(password) ) {
            int rowCount = userDao.deleteUser(user);
            setResult(rowCount > 0, "user deleted after password check.");
        } else {
            setError("user account not found");
        }
    }
}
