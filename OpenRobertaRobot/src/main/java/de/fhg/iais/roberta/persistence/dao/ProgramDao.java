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
     * make a program object and persist it (if the program, identified by owner&name&robot, does not exist) or update it (if the program exists)
     *
     * @param name the name of the program, never null
     * @param user the user who owns the program, never null
     * @param robotId
     * @param programText the program text, maybe null
     * @param timestamp timestamp of the program, never null
     * @param isOwner true, if the owner updates a program; false if a user with access right WRITE updates a program
     * @return true if the program could be persisted successfully
     */
    public Key persistProgramText(String name, User user, Robot robot, String programText, Timestamp timestamp, boolean isOwner) {
        Assert.notNull(name);
        Assert.notNull(user);
        Assert.notNull(robot);
        Assert.notNull(timestamp);

        if ( isOwner ) {
            Program program = load(name, user, robot);
            if ( program == null ) {
                if ( timestamp.equals(new Timestamp(0)) ) {
                    // the program doesn't exist, thus the expected timestamp must not be set
                    program = new Program(name, user, robot);
                    program.setProgramText(programText);
                    this.session.save(program);
                    return Key.PROGRAM_SAVE_SUCCESS; // the only legal key if success
                } else {
                    // otherwise ...
                    ProgramDao.LOG.error("update was requested, but no program with matching timestamp was found");
                    return Key.PROGRAM_SAVE_ERROR_NO_PROGRAM_TO_UPDATE_FOUND;
                }
            } else if ( !timestamp.equals(program.getLastChanged()) ) {
                ProgramDao.LOG.error("update was requested, timestamps don't match. Has another user updated the program in the meantime?");
                return Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING;
            } else {
                program.setProgramText(programText);
                return Key.PROGRAM_SAVE_SUCCESS; // the only legal key if success
            }
        } else {
            Program program = loadSharedForUpdate(name, user, robot);
            if ( program == null ) {
                ProgramDao.LOG.error("update was requested, but no shared program was found");
                return Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION;
            } else if ( !timestamp.equals(program.getLastChanged()) ) {
                ProgramDao.LOG.error("update was requested, timestamps don't match. Has another user updated the program in the meantime?");
                return Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING;
            } else {
                program.setProgramText(programText);
                return Key.PROGRAM_SAVE_SUCCESS; // the only legal key if success
            }
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
