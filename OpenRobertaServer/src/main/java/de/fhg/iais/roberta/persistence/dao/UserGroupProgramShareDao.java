package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.bo.UserGroupProgramShare;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class UserGroupProgramShareDao extends AbstractDao<UserGroupProgramShare> {
    /**
     * Create a new DAO for share objects of programs to user groups. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public UserGroupProgramShareDao(DbSession session) {
        super(UserGroupProgramShare.class, session);
    }

    /**
     * Persist a share object of a program to a user group in the database. If the program is already shared to the user group,
     * its relation will be updated. Otherwise, a new one will be created.<br/>
     * Asserts, that the userGroup is not null.<br/>
     * Asserts, that the program is not null.<br/>
     * Asserts, that the relation is not null.
     *
     * @param userGroup the user group whose access rights have to be changed
     * @param program the program affected
     * @param relation the access right of the user group for the program
     * @return the share object for the user group and program with the specified relation
     */
    public UserGroupProgramShare persistUserProgramShare(UserGroup userGroup, Program program, Relation relation) {
        Assert.notNull(userGroup);
        Assert.notNull(program);
        Assert.notNull(relation);

        this.lockTable();
        UserGroupProgramShare userGroupProgramShare = this.loadUserGroupProgramShare(userGroup, program);
        if ( userGroupProgramShare == null ) {
            userGroupProgramShare = new UserGroupProgramShare(userGroup, program, relation);
            this.session.save(userGroupProgramShare);
        } else {
            userGroupProgramShare.setRelation(relation);
        }
        return userGroupProgramShare;
    }

    /**
     * Delete a share object of a program to a user group in the database.
     *
     * @param userGroup the user group for which the program shall no longer be shared
     * @param program the program
     */
    public void deleteUserGroupProgramShare(UserGroup userGroup, Program program) {
        this.lockTable();
        UserGroupProgramShare toBeDeleted = this.loadUserGroupProgramShare(userGroup, program);
        if ( toBeDeleted != null ) {
            this.session.delete(toBeDeleted);
        }
    }

    /**
     * Loads a share object for a user group and a program from the database<br/>
     * Asserts, that the userGroup is not null.<br/>
     * Asserts, that the program is not null.
     *
     * @param userGroup The user group for which the share object shall be loaded
     * @param program The program for which the share object shall be loaded
     * @return A share object for the user group and program or null, if no such share object exists.
     */
    public UserGroupProgramShare loadUserGroupProgramShare(UserGroup userGroup, Program program) {
        Assert.notNull(userGroup);
        Assert.notNull(program);

        Query hql = this.session.createQuery("from UserGroupProgramShare where userGroup=:userGroup and program=:program");
        hql.setEntity("userGroup", userGroup);
        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<UserGroupProgramShare> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * Load all user group to program share objects persisted in the database for a given program<br/>
     * Asserts, that the program is not null.
     *
     * @param program The program for which all shares with user groups shall be returned
     * @return The list of all share objects for a user group and a program. May be an empty list, but never will be null.
     */
    public List<UserGroupProgramShare> loadUserGroupProgramSharesByProgram(Program program) {
        Assert.notNull(program);

        Query hql = this.session.createQuery("from UserGroupProgramShare where program=:program");

        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<UserGroupProgramShare> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * Load all share objects for user groups with programs for a given user group<br/>
     * Asserts, that the user group is not null.
     *
     * @param userGroup the user group for which all share objects with programs shall be returned
     * @param robot Optionally a robot system, to only return those shares with programs for the given
     *        user group, that are written for the robot
     * @return A list of share objects for user groups and programs. May be an empty list, but never null
     */
    public List<UserGroupProgramShare> loadUserGroupProgramSharesForUserGroup(UserGroup userGroup, Robot robot) {
        Assert.notNull(userGroup);
        Query hql;

        if ( robot != null ) {
            hql = this.session.createQuery("from UserGroupProgramShare where userGroup=:userGroup and program.robot=:robot");
            hql.setEntity("robot", robot);
        } else {
            hql = this.session.createQuery("from UserGroupProgramShare where userGroup=:userGroup");
        }
        hql.setEntity("userGroup", userGroup);

        @SuppressWarnings("unchecked")
        List<UserGroupProgramShare> il = hql.list();
        return Collections.unmodifiableList(il);

    }

    /**
     * create a write lock for the table USERGROUP_PROGRAM to avoid deadlocks. This is a no op if concurrency control is not 2PL, but MVCC
     */
    private void lockTable() {
        this.session.createSqlQuery("lock table USERGROUP_PROGRAM write").executeUpdate();
        this.session.addToLog("lock", "is now aquired");
    }
}
