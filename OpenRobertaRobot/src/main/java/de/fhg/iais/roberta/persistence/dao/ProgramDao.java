package de.fhg.iais.roberta.persistence.dao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author rbudde
 */
public class ProgramDao extends AbstractDao<Program> {
    private static final Logger LOG = LoggerFactory.getLogger(ProgramDao.class);

    /**
     * create a new DAO for programs. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public ProgramDao(DbSession session) {
        super(Program.class, session);
    }

    /**
     * persist a program object that is owned by the caller
     *
     * @param name the name of the program, never null
     * @param user the user who owns the program, never null
     * @param robotId the robot the program was written for
     * @param programText the program text
     * @param programTimestamp timestamp of the last change of the program (if it already existed); <code>null</code> if a new program is saved
     * @return a pair of (message-key, program). If the program is persisted successfully, the program is NOT null.
     */
    public Pair<Key, Program> persistOwnProgram(String name, User user, Robot robot, String programText, Timestamp timestamp) {
        Assert.notNull(name);
        Assert.notNull(user);
        Assert.notNull(robot);
        Program program = load(name, user, robot);
        if ( program == null ) {
            if ( timestamp == null ) {
                // save as && the program doesn't exist.
                program = new Program(name, user, robot);
                program.setProgramText(programText);
                this.session.save(program);
                return Pair.of(Key.PROGRAM_SAVE_SUCCESS, program); // the only legal key if success
            } else {
                return Pair.of(Key.PROGRAM_SAVE_ERROR_PROGRAM_TO_UPDATE_NOT_FOUND, null);
            }
        } else {
            if ( timestamp == null ) {
                // save as && the program exists.
                return Pair.of(Key.PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS, null);
            } else if ( timestamp.equals(program.getLastChanged()) ) {
                program.setProgramText(programText);
                return Pair.of(Key.PROGRAM_SAVE_SUCCESS, program); // the only legal key if success
            } else {
                ProgramDao.LOG.error("update was requested, timestamps don't match. Has another user updated the program in the meantime?");
                return Pair.of(Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING, null);
            }
        }
    }

    /**
     * persist a program object that the owner shared with the caller
     *
     * @param name the name of the program, never null
     * @param user the user who owns the program, never null
     * @param robotId the robot the program was written for
     * @param programText the program text
     * @param programTimestamp timestamp of the last change of the program (if it already existed); <code>null</code> if a new program is saved
     * @return a pair of (message-key, program). If the program is persisted successfully, the program is NOT null.
     */
    public Pair<Key, Program> persistSharedProgramText(String name, User user, Robot robot, String programText, Timestamp timestamp) {
        Assert.notNull(name);
        Assert.notNull(user);
        Assert.notNull(robot);

        Program program = loadSharedForUpdate(name, user, robot);
        if ( program == null ) {
            ProgramDao.LOG.error("update was requested, but no shared program was found");
            return Pair.of(Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION, null);
        } else if ( !timestamp.equals(program.getLastChanged()) ) {
            ProgramDao.LOG.error("update was requested, timestamps don't match. Has another user updated the program in the meantime?");
            return Pair.of(Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING, null);
        } else {
            program.setProgramText(programText);
            return Pair.of(Key.PROGRAM_SAVE_SUCCESS, program); // the only legal key if success
        }
    }

    /**
     * load a program from the database, identified by its owner, its name (both make up the "business" key of a program)<br>
     * The timestamp used for optimistic locking is <b>not</b> checked here. <b>The caller is responsible to do that!</b>
     *
     * @param name the name of the program, never null
     * @param owner user who owns the program, never null
     * @return the program, null if the program is not found
     */
    public Program load(String name, User owner, Robot robot) {
        Assert.notNull(name);
        Assert.notNull(owner);
        Assert.notNull(robot);
        Query hql = this.session.createQuery("from Program where name=:name and owner=:owner and robot=:robot");
        hql.setString("name", name);
        hql.setEntity("owner", owner);
        hql.setEntity("robot", robot);
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * load a program from the database, identified by a user with access rights to it and its name
     *
     * @param name the name of the program, never null
     * @param user user with access right WRITE, never null
     * @param robot
     * @return the program, null if the program is not found
     */
    private Program loadSharedForUpdate(String name, User user, Robot robot) {
        Assert.notNull(name);
        Assert.notNull(user);
        Assert.notNull(robot);
        Query hql = this.session.createQuery("from AccessRight where user=:user and program.name=:name and program.robot=:robot");
        hql.setString("name", name);
        hql.setEntity("user", user);
        hql.setEntity("robot", robot);
        @SuppressWarnings("unchecked")
        List<AccessRight> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 1 ) {
            AccessRight accessRight = il.get(0);
            if ( accessRight.getRelation() == Relation.WRITE ) {
                return accessRight.getProgram();
            }
        }
        return null; // .. because the right dowsn't exist, it's no write access :-)
    }

    public int deleteByName(String name, User owner, Robot robot) {
        Program toBeDeleted = load(name, owner, robot);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    /**
     * load all programs persisted in the database which are owned by a user given
     *
     * @return the list of all programs, may be an empty list, but never null
     */
    public List<Program> loadAll(User owner, Robot robot) {
        Query hql = this.session.createQuery("from Program where owner=:owner and robot=:robot");
        hql.setEntity("owner", owner);
        hql.setEntity("robot", robot);
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * load all programs persisted in the database
     *
     * @return the list of all programs, may be an empty list, but never null
     */
    public List<Program> loadAll() {
        Query hql = this.session.createQuery("from Program");
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}
