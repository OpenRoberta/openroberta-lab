package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserProgram;
import de.fhg.iais.roberta.persistence.util.DbSession;

import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserProgramDao;
import de.fhg.iais.roberta.util.Util;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

public class UserProgramProcessor extends AbstractProcessor {
	
    public UserProgramProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }
    
    public void setRights(int ownerId, String programName, JSONArray usersJSONArray){
    	
    	UserProgramDao userProgramDao = new UserProgramDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        
        //I am assuming that the user exists and that it has the correct values
        UserDao userDao = new UserDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Program program = programDao.load(programName, owner);
        
        System.out.println("owner data "+owner.getId());
        
        for (int i = 0 ; i < usersJSONArray.length(); i++) {

                try {
                    JSONObject userToInvite = usersJSONArray.getJSONObject(i);
                    int userId = userToInvite.getInt("id");
                    String right = userToInvite.getString("right");
                    
                    UserDao userDaoN = new UserDao(this.dbSession);
                    User user = userDaoN.get(userId);
                    
                    System.out.println("user to set rigths to "+user.getId());
                    
                    if(user != null){
                    	userProgramDao.persistUserProgram(user, program, right);
                    }
                } 
                catch (JSONException e) {
                    
                }       
        }
        
//        setError("JSON etc");
    }
    
    public void shareToUser(int ownerId, String userToShareName, String programName, String right){
    	
        ProgramDao programDao = new ProgramDao(this.dbSession);
        UserDao userDao = new UserDao(this.dbSession);
        
        User owner = userDao.get(ownerId);
        Program programToShare = programDao.load(programName, owner);
        User userToShare = userDao.loadUser(userToShareName);
        
    	UserProgramDao userProgramDao = new UserProgramDao(this.dbSession);
    	if(right.equals("NONE")){
    		int userProgram = userProgramDao.deleteUserProgram(userToShare, programToShare);
    		setResult(userProgram == 1, "user program deleted.");
    	}else{
    		UserProgram userProgram = userProgramDao.persistUserProgram(userToShare, programToShare, right);
    		setResult(userProgram != null, "user program persisted.");
    	}
    	
        
    	
    }
    
    /**
     * ...
     *
     * @param account
     * @param password
     * @param roleAsString
     * @return the created user object; returns <code>null</code> if creation is unsuccessful (e.g. user already exists)
     */
    
    public  JSONArray usersPerProgram(String programName, int ownerId){
    	

        if ( !Util.isValidJavaIdentifier(programName) ) {
        	
            setError("program name is not a valid identifier: " + programName);
            return null;
            
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
        	
            ProgramDao programDao = new ProgramDao(this.dbSession);
            UserDao userDao = new UserDao(this.dbSession);
            User owner =  userDao.get(ownerId);		
            Program program = programDao.load(programName, owner);
            
            UserProgramDao userProgramDao  = new UserProgramDao(this.dbSession);
            List<UserProgram> userProgramList = userProgramDao.loadUserProgramByProgram(program);
            
            JSONArray usersJSONArray = new JSONArray();
            
            for (UserProgram userProgram: userProgramList){
            	JSONObject userJSON = new JSONObject();
            	
            	User user  = userProgram.getUser();
            	
                try {

                    if(user != null){
                    	
                    	userJSON.put("id",user.getId());
                    	userJSON.put("name", user.getAccount());
                    	userJSON.put("relation", userProgram.getRelation());
                    	usersJSONArray.put(userJSON);
                
                    }
                } 
                catch (JSONException e) {
                	
                }
            }
            setResult(0  != usersJSONArray.length(), "loading  users of program " + programName + ".");
            return usersJSONArray;
        } else {
            setError("program load illegal if not logged in");
            return null;
        }
    }
    
    
}
