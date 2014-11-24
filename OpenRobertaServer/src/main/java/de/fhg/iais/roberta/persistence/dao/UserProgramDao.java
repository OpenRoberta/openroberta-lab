package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserProgram;
import de.fhg.iais.roberta.persistence.util.DbSession;

/**
 * DAO class to load and store user_programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author cesar
 */
public class UserProgramDao extends AbstractDao<UserProgram> {
    /**
     * create a new DAO for users programs. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public UserProgramDao(DbSession session) {
        super(UserProgram.class, session);
    }
    
    /**
     * persist a user_program relation in the database, if already existent
     * modifies it.
     *
     * @param account
     * @param password
     * @param roleAsString
     * @return the created user object; returns <code>null</code> if creation is unsuccessful (e.g. user already exists)
     */
    
    public UserProgram persistUserProgram(User user, Program program, String relationAsString) {
        Assert.notNull(user);
        Assert.notNull(program);
        Assert.notNull(relationAsString);
        Relation relation = Relation.valueOf(relationAsString);
        Assert.notNull(relation);
        

        UserProgram userProgram = loadUserProgram(user,program);
        if ( userProgram == null ) {
            UserProgram userProgram2 = new UserProgram(user,program,relation);
            userProgram2.setRelation(relation);
            this.session.save(userProgram2);
            return userProgram;
        } else {
            userProgram.setRelation(relation);
            return userProgram;
        }    

    }
    
    /**
     * load a userProgram relation from the database, 
     * identified by its user and its program 
     * only one relationship is allowed by pair
     *
     * @param name the name of the program, never null
     * @param owner user who owns the program, never null
     * @return the program, null if the program is not found
     */
    public UserProgram loadUserProgram(User user,Program program){
        Assert.notNull(user);
        Assert.notNull(program);
    	Query hql = this.session.createQuery("from UserProgram where user=:user and program=:program");
        hql.setEntity("user", user);
        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<UserProgram> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }
    
    /**
     * load all userPrograms relations persisted in the database which are
     * given by one specific program
     *
     * @return the list of all userPrograms relations, may be an empty list, but never null
     */
    public List<UserProgram> loadUserProgramByProgram(Program program){

        Assert.notNull(program);
    	Query hql = this.session.createQuery("from UserProgram where program=:program");

        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<UserProgram> il = hql.list();
        return Collections.unmodifiableList(il);
    }
    
    /**
     * load all userPrograms relations persisted in the database which 
     * pertain to a given user
     *
     * @return the list of all userPrograms relations, may be an empty list, but never null
     */
    public List<UserProgram> loadUserProgramByUser(User user){
    	
        Assert.notNull(user);
    	Query hql = this.session.createQuery("from UserProgram where user=:user");
        hql.setEntity("user", user);

        @SuppressWarnings("unchecked")
        List<UserProgram> il = hql.list();
        return Collections.unmodifiableList(il);
        
        
    }
    
    public int deleteUserProgram(User user, Program program) {
        UserProgram toBeDeleted = loadUserProgram(user, program);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }
    
    
}
