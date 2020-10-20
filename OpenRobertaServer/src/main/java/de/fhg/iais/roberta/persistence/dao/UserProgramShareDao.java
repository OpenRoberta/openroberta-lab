package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserProgramShare;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store share information between users and programs. A DAO object is always bound to a session.
 * This session defines the transactional context, in which the database access takes place.
 */
public class UserProgramShareDao extends AbstractDao<UserProgramShare> {
    /**
     * Create a new DAO for share objects of programs between users. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public UserProgramShareDao(DbSession session) {
        super(UserProgramShare.class, session);
    }

    /**
     * Persist a share object of program with a user in the database. Updates it, if the object already exists.
     *
     * @param user the user whose access rights have to be changed
     * @param program the program affected
     * @param relation the access right of the user for the program
     * @return the share objects
     */
    public UserProgramShare persistUserProgramShare(User user, Program program, Relation relation) {
        Assert.notNull(user);
        Assert.notNull(program);
        Assert.notNull(relation);

        this.lockTable();
        UserProgramShare accessRight = this.loadUserProgramShare(user, program);
        if ( accessRight == null ) {
            accessRight = new UserProgramShare(user, program, relation);
            this.session.save(accessRight);
        } else {
            accessRight.setRelation(relation);
        }
        return accessRight;
    }

    /**
     * Delete a given share object of program with a user
     *
     * @param user the user whose access right has to be deleted
     * @param program the program affected
     */
    public void deleteUserProgramShare(User user, Program program) {
        this.lockTable();
        UserProgramShare toBeDeleted = this.loadUserProgramShare(user, program);
        if ( toBeDeleted != null ) {
            this.session.delete(toBeDeleted);
        }
    }

    /**
     * Load a share object of program with a user from the database, identified by user and program. It is always assumed, that only one share object is allowed
     * for every pair
     * user/program
     *
     * @param user the user whose access rights are loaded
     * @param program the program affected
     * @return the share object, null if no share objects has been found in the DB
     */
    public UserProgramShare loadUserProgramShare(User user, Program program) {
        Assert.notNull(user);
        Assert.notNull(program);

        Query hql = this.session.createQuery("from UserProgramShare where user=:user and program=:program");
        hql.setEntity("user", user);
        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<UserProgramShare> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * Load all share objects of programs between users persisted in the database for a given program
     *
     * @return the list of all share objects, may be an empty list, but never null
     */
    public List<UserProgramShare> loadUserProgramSharesByProgram(Program program) {
        Assert.notNull(program);

        Query hql = this.session.createQuery("from UserProgramShare where program=:program");

        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<UserProgramShare> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * Load all share objects of programs between users persisted in the database for a given user
     *
     * @return the list of all share objects, may be an empty list, but never null
     */
    public List<UserProgramShare> loadUserProgramsSharedWithUser(User user, Robot robot) {
        Assert.notNull(user);
        Assert.notNull(robot);

        int robotId = robot.getId();

        Query hql = this.session.createQuery("from UserProgramShare where user=:user and program.robot.id=:robotId");

        hql.setEntity("user", user);
        hql.setInteger("robotId", robotId);
        @SuppressWarnings("unchecked")
        List<UserProgramShare> il = hql.list();
        return Collections.unmodifiableList(il);

    }

    /**
     * @param userId
     * @param programName
     * @param ownerId
     * @param authorName
     * @return
     */
    public UserProgramShare loadUserProgramShareForUser(int userId, String programName, int ownerId, String authorName) {
        Assert.isTrue(userId > 0);

        Query hql =
            this.session
                .createQuery(
                    "from UserProgramShare where user.id=:userId and program.name=:programName and program.owner.id=:ownerId and program.author.account=:authorName");

        hql.setInteger("userId", userId);
        hql.setString("programName", programName);
        hql.setInteger("ownerId", ownerId);
        hql.setString("authorName", authorName);
        @SuppressWarnings("unchecked")
        List<UserProgramShare> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * create a write lock for the table USER_PROGRAM to avoid deadlocks. This is a no op if concurrency control is not 2PL, but MVCC
     */
    private void lockTable() {
        this.session.createSqlQuery("lock table USER_PROGRAM write").executeUpdate();
        this.session.addToLog("lock", "is now aquired");
    }

}