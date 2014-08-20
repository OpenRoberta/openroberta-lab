package de.fhg.iais.roberta.persistence;


import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.UserDao;


public class UserProcessor{

    public User getUser(SessionWrapper session, String accountName, String userPassword) {
    	
        UserDao userDao = new UserDao(session);
        User user = userDao.loadUser(accountName,userPassword);
        return user;
        
    }

    public User saveUser(SessionWrapper session, String accountName, String userName, String email, String userPassword, String role) {

        UserDao userDao = new UserDao(session);
        User user0 = userDao.checkAccountName(accountName);
        
        if(user0 == null){
        	User user1 = userDao.persistUser(accountName, userName, email,  userPassword, role);
        	return user1;
        }else{
        	return null;
        }
        
    }
    
    public User checkUser(SessionWrapper session, String accountName){
   
    	UserDao userDao = new UserDao(session);
    	User user = userDao.checkAccountName(accountName);
    	return user;
    }
    
    public int deleteUserProgramByName(SessionWrapper session, String accountName) {
        UserDao userDao = new UserDao(session);
        User user0 = userDao.checkAccountName(accountName);
        
        if(user0 == null){
        	return 0;
        }else{
        	return userDao.deleteUserByAccountName(accountName);
        }

    }
    
}
