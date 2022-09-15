package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.query.Query;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.bo.UserGroupProgramShare;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class UserGroupProgramShareDao extends AbstractDao<UserGroupProgramShare> {
    public UserGroupProgramShareDao(DbSession session) {
        super(UserGroupProgramShare.class, session);
    }

    /**
     * Persist a share object of a program to a user group. If the program is already shared to the user group, its relation will be updated. Otherwise, a new
     * one will be created.
     *
     * @param userGroup the user group whose access rights have to be changed, not null
     * @param program the program affected, not null
     * @param relation the access right of the user group for the program, not null
     * @return the share object for the user group and program with the specified relation
     */
    public UserGroupProgramShare persistUserProgramShare(UserGroup userGroup, Program program, Relation relation) {
        Assert.notNull(userGroup);
        Assert.notNull(program);
        Assert.notNull(relation);
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
     * Delete a share object of a program to a user group
     *
     * @param userGroup the user group for which the program shall no longer be shared
     * @param program the program
     */
    public void deleteUserGroupProgramShare(UserGroup userGroup, Program program) {
        UserGroupProgramShare toBeDeleted = this.loadUserGroupProgramShare(userGroup, program);
        if ( toBeDeleted != null ) {
            this.session.delete(toBeDeleted);
        }
    }

    /**
     * Loads a share object for a user group and a program
     *
     * @param userGroup The user group for which the share object shall be loaded, not null
     * @param program The program for which the share object shall be loaded, not null
     * @return A share object for the user group and program or null, if no such share object exists.
     */
    public UserGroupProgramShare loadUserGroupProgramShare(UserGroup userGroup, Program program) {
        Assert.notNull(userGroup);
        Assert.notNull(program);

        Query hql = this.session.createQuery("from UserGroupProgramShare where userGroup=:userGroup and program=:program");
        hql.setParameter("userGroup", userGroup);
        hql.setParameter("program", program);
        @SuppressWarnings("unchecked")
        List<UserGroupProgramShare> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * Load for a given program all share objects for all user groups
     *
     * @param program The program for which all shares with user groups shall be returned, not null
     * @return The list of all share objects for a user group and a program. May be an empty list, but never will be null.
     */
    public List<UserGroupProgramShare> loadUserGroupProgramSharesByProgram(Program program) {
        Assert.notNull(program);

        Query hql = this.session.createQuery("from UserGroupProgramShare where program=:program");

        hql.setParameter("program", program);
        @SuppressWarnings("unchecked")
        List<UserGroupProgramShare> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * Load for a given user group load all share objects
     *
     * @param userGroup the user group for which all share objects shall be returned, not null
     * @param robot Optionally a robot system to filter those shares
     * @return A list of share objects. May be empty, but never null
     */
    public List<UserGroupProgramShare> loadProgramSharesForUserGroup(UserGroup userGroup, Robot robot) {
        Assert.notNull(userGroup);
        Query hql;

        if ( robot != null ) {
            hql = this.session.createQuery("from UserGroupProgramShare where userGroup=:userGroup and program.robot=:robot");
            hql.setParameter("robot", robot);
        } else {
            hql = this.session.createQuery("from UserGroupProgramShare where userGroup=:userGroup");
        }
        hql.setParameter("userGroup", userGroup);

        @SuppressWarnings("unchecked")
        List<UserGroupProgramShare> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}
