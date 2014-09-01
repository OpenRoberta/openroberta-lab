package de.fhg.iais.roberta.persistence.dao;


import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;

import java.util.Date;

public class UserDao extends AbstractDao<User>{
    /**
     * create a new DAO for users. This creation is cheap.
     * 
     * @param session the session used to access the database.
     */
	
	public UserDao(SessionWrapper session) {
		super(User.class, session);
		// TODO Auto-generated constructor stub
	}
    
    public User persistUser(String accountName, String userName, String email, String userPassword, String role) {
        Assert.notNull(accountName);
        User user = loadUser(accountName,userPassword);
        
        if (user == null) {
       	 	Date date= new Date();
       	 	Timestamp createdTime = new Timestamp(date.getTime());
            user = new User(accountName, userName, email, userPassword, role, createdTime, createdTime);
            this.session.save(user);
            
            return user;
        }else{
        	return null;
        }
    }
    
    public User load(int userId){
    	
        Assert.notNull(userId);
        Query hql = this.session.createQuery("from User where id=:id");
        hql.setInteger("id", userId);
        
        @SuppressWarnings("unchecked")
		List<User> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
        
    }
    
    public User loadUser(String accountName, String userPassword){

        Assert.notNull(accountName);
        Query hql = this.session.createQuery("from User where accountName=:accountName");
        hql.setString("accountName", accountName);
        
        @SuppressWarnings("unchecked")
		List<User> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if(il.size() == 0){
        	return null;
        }else{

        	String dbUserPassword = il.get(0).getPassword();
        	
        	if(dbUserPassword.equals(userPassword)){
           	 	Date date= new Date();
           	 	Timestamp lastSignedOn = new Timestamp(date.getTime());
        		il.get(0).setLastSignedOn(lastSignedOn);
        		return il.get(0);
        	}else{
        		return null;
        	}
        }
        
    }
    
    public User checkAccountName(String accountName){
    	
        Assert.notNull(accountName);
        Query hql = this.session.createQuery("from User where accountName=:accountName");
        hql.setString("accountName", accountName);
        
        @SuppressWarnings("unchecked")
		List<User> il = hql.list();

        if(il.size() == 0){
        	return null;
        }else{
        	return il.get(0);
        }
        
    }
    
    public int deleteUserByAccountName(String accountName) {
    	
        Assert.notNull(accountName);
        Query hql = this.session.createQuery("from User where accountName=:accountName");
        hql.setString("accountName", accountName);
        
        @SuppressWarnings("unchecked")
        List<User> il = hql.list();
        if ( il.size() == 0 ) {
            return 0;
        } else {
            User toBeDeleted = il.get(0);
            this.session.delete(toBeDeleted);
            return 1;
        } 
    }
    
//    public int deleteUserByAccountName(String accountName) {
//    	
//        Assert.notNull(accountName);
//        Query hql = this.session.createQuery("from User where accountName=:accountName");
//        hql.setString("accountName", accountName);
//        
//        @SuppressWarnings("unchecked")
//        List<User> il = hql.list();
//        if ( il.size() == 0 ) {
//            return 0;
//        } else if ( il.size() == 1 ) {
//            User toBeDeleted = il.get(0);
//            this.session.delete(toBeDeleted);
//            return 1;
//        } else {
//            throw new DbcException("DB is inconsistent");
//        }
//    }
    
}