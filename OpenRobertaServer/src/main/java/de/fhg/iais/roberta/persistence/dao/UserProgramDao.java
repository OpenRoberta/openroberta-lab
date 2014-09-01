package de.fhg.iais.roberta.persistence.dao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.persistence.bo.UserProgram;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 * 
 * @author rbudde
 */

public class UserProgramDao extends AbstractDao<UserProgram> {
    /**
     * create a new DAO for programs. This creation is cheap.
     * 
     * @param session the session used to access the database.
     */
    public UserProgramDao(SessionWrapper session) {
        super(UserProgram.class, session);
    }

    /**
     * make an program object and persist it (if the program, identified by project&name, does not exist) or update it (if the program exists)
     * 
     * @param project the project the program belongs to, never null
     * @param programName the name of the program, never null
     * @param programText the program text, maybe null
     * @return the persisted program object
     */
    
    public UserProgram persistUserProgramText(User user, String programName, String programText) {

        Assert.notNull(programName);
        UserProgram userProgram = loadUserProgram(user, programName);
        if (userProgram == null) {
       	 	Date date= new Date();
       	 	Timestamp created = new Timestamp(date.getTime());
            userProgram = new UserProgram(programName, "-", programText,created,created,user,0);
            this.session.save(userProgram);
            
        }
        userProgram.setProgramText(programText);
        return userProgram;
    }
    /**
     * load an program from the database, identified by its project and its name (both make up the "business" key of an program)
     * 
     * @param projectName the project, never null
     * @param programName the name of the program, never null
     * @return the program, null if the program is not found
     */
    public UserProgram loadUserProgram(User user, String programName) {
        Assert.notNull(user);
        Assert.notNull(programName);
        Query hql = this.session.createQuery("from UserProgram where user=:user and programName=:programName");
        hql.setEntity("user", user);
        hql.setString("programName", programName);
        
        @SuppressWarnings("unchecked")
        List<UserProgram> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if(il.size() == 0){
        	return null;
        }else{
       	 	Date date = new Date();
       	 	Timestamp lastUpdated = new Timestamp(date.getTime());
        	il.get(0).setLastUpdated(lastUpdated);
        	return il.get(0);
        }
    }

    public int deleteUserProgramByName(int userId, String programName) {
        Assert.notNull(userId);
        Assert.notNull(programName);
        Query hql = this.session.createQuery("from UserProgram where user.id=:userId and programName=:programName");
        hql.setInteger("userId", userId);
        hql.setString("programName", programName);
        
        @SuppressWarnings("unchecked")
        List<UserProgram> il = hql.list();
        if ( il.size() == 0 ) {
            return 0;
        } else if ( il.size() == 1 ) {
            UserProgram toBeDeleted = il.get(0);
            this.session.delete(toBeDeleted);
            return 1;
        } else {
            throw new DbcException("DB is inconsistent");
        }
    }

    /**
     * load all programs persisted in the database
     * 
     * @return the list of all programs, may be an empty list, but never null
     */
    public List<UserProgram> loadAllUserPrograms(User user) {
        Query hql = this.session.createQuery("from UserProgram where userId=:userId");
        hql.setEntity("user", user);
        @SuppressWarnings("unchecked")
        List<UserProgram> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}
