package de.fhg.iais.roberta.persistence.dao;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroupProgramShare;
import de.fhg.iais.roberta.persistence.bo.UserProgramShare;
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
     * @param programText the program text
     * @param confName the name of the configuration. May be null for anonymous configurations.
     * @param confHash the hash of the configuration. May be null if the default configuration is used.
     * @param user the user who owns the program, never null
     * @param robot the robot the program was written for
     * @param author the user who is the author of this program, never null
     * @param programTimestamp timestamp of the last change of the program (if it already existed); <code>null</code> if a new program is saved
     * @return a pair of (message-key, program). If the program is persisted successfully, the program is NOT null.
     */
    public Pair<Key, Program> persistOwnProgram(
        String name,
        String programText,
        String confName,
        String confHash,
        User user,
        Robot robot,
        User author,
        Timestamp timestamp) //
    {
        checkProgramValidity(name, user, robot, author, programText);

        Program program = load(name, user, robot, author);
        if ( program == null ) {
            if ( timestamp == null ) {
                // save as && the program doesn't exist.
                program = new Program(name, user, robot, author);
                program.setProgramText(programText);
                program.setConfigData(confName, confHash);
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
                program.setConfigData(confName, confHash);
                this.session.getSession().flush();
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
     * @param programText the program text, never null
     * @param confName the name of the configuration. May be null for anonymous configurations.
     * @param confHash the hash of the configuration. May be null if the default configuration is used.
     * @param user the user who owns the program, never null
     * @param robotId the robot the program was written for
     * @param programTimestamp timestamp of the last change of the program (if it already existed); <code>null</code> if a new program is saved
     * @return a pair of (message-key, program). If the program is persisted successfully, the program is NOT null.
     */
    public Pair<Key, Program> persistSharedProgramText(
        String name,
        String programText,
        String configName,
        String configHash,
        User owner,
        Robot robot,
        User author,
        User sharedUser,
        Timestamp timestamp) //
    {
        checkProgramValidity(name, owner, robot, author, programText);
        Program program = loadSharedForUpdate(name, sharedUser, robot, owner, author);
        if ( program == null ) {
            ProgramDao.LOG.error("update was requested, but no shared program was found");
            return Pair.of(Key.PROGRAM_SAVE_ERROR_NO_WRITE_PERMISSION, null);
        } else if ( !timestamp.equals(program.getLastChanged()) ) {
            ProgramDao.LOG.error("update was requested, timestamps don't match. Has another user updated the program in the meantime?");
            return Pair.of(Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING, null);
        } else {
            program.setProgramText(programText);
            program.setConfigData(configName, configHash);
            return Pair.of(Key.PROGRAM_SAVE_SUCCESS, program); // the only legal key if success
        }
    }

    /**
     * load a program from the database, identified by its owner, its name (both make up the "business" key of a program)<br>
     * The timestamp used for optimistic locking is <b>not</b> checked here. <b>The caller is responsible to do that!</b>
     *
     * @param name the name of the program, never null
     * @param owner user who owns the program, never null
     * @param robotId the robot the program was written for
     * @param author the user who is the author of this program, never null
     * @return the program, null if the program is not found
     */
    public Program load(String name, User owner, Robot robot, User author) {
        checkProgramValidity(name, owner, robot, author, null);
        Query hql = this.session.createQuery("from Program where name=:name and owner=:owner and robot=:robot and author=:author");
        hql.setString("name", name);
        hql.setEntity("owner", owner);
        hql.setEntity("robot", robot);
        hql.setEntity("author", author);
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
    private Program loadSharedForUpdate(String name, User user, Robot robot, User owner, User author) {
        checkProgramValidity(name, user, robot, author, null);
        Query hql =
            this.session
                .createQuery(
                    "from UserProgramShare where user=:user and program.name=:name and program.robot=:robot and program.owner=:owner and program.author=:author and relation IN (:relation)");
        hql.setString("name", name);
        hql.setEntity("user", user);
        hql.setEntity("robot", robot);
        hql.setEntity("owner", owner);
        hql.setEntity("author", author);
        hql.setParameterList("relation", Arrays.asList(Relation.WRITE, Relation.X_WRITE));
        @SuppressWarnings("unchecked")
        List<UserProgramShare> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 1 ) {
            return il.get(0).getProgram();
        }

        if ( user.getUserGroup() != null && user.getUserGroup().getOwner().equals(owner) ) {
            hql =
                this.session
                    .createQuery(
                        "from UserGroupProgramShare where userGroup=:userGroup and program.name=:name and program.robot=:robot and program.owner=:owner and relation IN (:relation)");
            hql.setEntity("userGroup", user.getUserGroup());
            hql.setString("name", name);
            hql.setEntity("robot", robot);
            hql.setEntity("owner", owner);
            hql.setParameterList("relation", Arrays.asList(Relation.WRITE, Relation.X_WRITE));
            @SuppressWarnings("unchecked")
            List<UserGroupProgramShare> ugList = hql.list();
            Assert.isTrue(ugList.size() <= 1);
            if ( ugList.size() == 1 ) {
                return ugList.get(0).getProgram();
            }
        }

        return null; // .. because the right dowsn't exist, it's no write access :-)
    }

    private void checkProgramValidity(String name, User user, Robot robot, User author, String programText) {
        Assert.notNull(name);
        Assert.notNull(user);
        Assert.notNull(robot);
        Assert.notNull(author);
        if ( programText != null && programText.contains("robottype=") ) {
            String expectedRobotNameInProgramXml = "robottype=\"" + robot.getName() + "\"";
            if ( !programText.contains(expectedRobotNameInProgramXml) ) {
                Assert.fail("robot selection and robottype don't match. Multiple browser tabs in use?");
            }
        }
    }

    public int deleteByName(String name, User owner, Robot robot, User author) {
        Program toBeDeleted = load(name, owner, robot, author);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    /**
     * load all programs persisted in the database which are owned by a given user and given robot type
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

    public JSONArray loadGallery(int galleryId, int userId, String robotGroup) {
        String galleryProgramSql = "" + //
            "select r.NAME as rname, p.NAME as pname, p.PROGRAM_TEXT as ptext, u.ACCOUNT as owner, p.CREATED as created, p.VIEWED as views," + //
            "       (select count(l1.ID) from USER_PROGRAM_LIKE l1 where l1.PROGRAM_ID=p.ID) as likes," + //
            "       p.TAGS as tags," + //
            "       case when (select count(l2.ID) from USER_PROGRAM_LIKE l2 where l2.PROGRAM_ID=p.ID and l2.USER_ID=:userId)>0 then TRUE " + //
            "            else FALSE " + //
            "       end as iLikedIt " + //
            "from PROGRAM p, ROBOT r, USER u " + //
            "where p.OWNER_ID = :galleryId " + //
            "  and p.ROBOT_ID = r.ID " + //
            "  and p.AUTHOR_ID = u.ID ";

        if ( !robotGroup.isEmpty() ) {
            galleryProgramSql = galleryProgramSql + "" + //
                " and r.NAME = :robotGroup ";
        }

        SQLQuery query = session.createSqlQuery(galleryProgramSql);
        query.setInteger("userId", userId);
        query.setInteger("galleryId", galleryId);
        if ( !robotGroup.isEmpty() ) {
            query.setString("robotGroup", robotGroup);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> galleryList = query.list();

        JSONArray programs = new JSONArray();
        for ( Object[] program : galleryList ) {
            programs.put(Arrays.asList(program));
        }
        return programs;
    }

    /**
     * create a write lock for the table PROGRAM to avoid deadlocks. This is a no op if concurrency control is not 2PL, but MVCC
     */
    public void lockTable() {
        this.session.createSqlQuery("lock table PROGRAM write").executeUpdate();
        this.session.addToLog("lock", "is now aquired");
    }

}
