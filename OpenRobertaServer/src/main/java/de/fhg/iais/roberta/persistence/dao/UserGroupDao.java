package de.fhg.iais.roberta.persistence.dao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to operate on userGroup objects. A DAO object is always bound to a session. This session defines the transactional context, in which the database
 * access takes place.
 *
 * @author eovchinnik & pmaurer
 */
public class UserGroupDao extends AbstractDao<UserGroup> {

    /**
     * create a new DAO for groups. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public UserGroupDao(DbSession session) {
        super(UserGroup.class, session);
    }

    /**
     * persist a user group, identified by a name and an owner.<br/>
     * Does not check, whether or not the group name is a valid name for a user group.<br/>
     * Asserts, that the groupName is not null.<br/>
     * Asserts, that the groupOwner is not null.
     *
     * @param groupName the name of the group, never null
     * @param groupOwner the user who owns this group, never null
     * @param timestamp timestamp of the last change of the group (if this call is an update of the group);
     *        <code>null</code> if a new group is saved
     * @return a pair of (message-key, group). If the group is persisted successfully, the group is NOT null.
     */
    public Pair<Key, UserGroup> persistGroup(String groupName, User groupOwner, Timestamp timestamp) //
    {
        Assert.notNull(groupName);
        Assert.notNull(groupOwner);

        UserGroup group = this.load(groupName, groupOwner);
        if ( group == null ) {
            if ( timestamp == null ) {
                group = new UserGroup(groupName, groupOwner);
                this.session.save(group);
                return Pair.of(Key.GROUP_CREATE_SUCCESS, group);
            } else {
                return Pair.of(Key.GROUP_UPDATE_ERROR_NOT_FOUND, null);
            }
        } else {
            if ( timestamp == null ) {
                return Pair.of(Key.GROUP_CREATE_ERROR_GROUP_ALREADY_EXISTS, null);
            } else {
                //TODO: Implement update persisting. Currently, nothing of a user group can be changed.
                throw new NotImplementedException("Persist UserGroup update");
            }
        }
    }

    /**
     * load a group from the database, identified by its owner and its name (both make up the "business" key of a group)<br>
     * The timestamp used for optimistic locking is <b>not</b> checked here. <b>The caller is responsible to do that!</b><br/>
     * Asserts, that the groupName is not null.<br/>
     * Asserts, that the groupOwner is not null.
     *
     * @param groupName the name of the group, never null
     * @param groupOwner the user who owns this group, never null
     * @return the group, maybe null
     */
    public UserGroup load(String groupName, User groupOwner) {
        Assert.notNull(groupName);
        Assert.notNull(groupOwner);

        Query hql = this.session.createQuery("from UserGroup where name=:groupName and owner=:groupOwner");
        hql.setString("groupName", groupName);
        hql.setEntity("groupOwner", groupOwner);

        @SuppressWarnings("unchecked")
        List<UserGroup> il = hql.list();
        Assert.isTrue(il.size() <= 1);

        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * load a group from the database, identified by its owner and its name (both make up the "business" key of a group)<br>
     * Asserts, that the groupName is not null.<br/>
     * Asserts, that the groupOwner is not null.
     *
     * @param groupName the name of the group, never null
     * @param groupOwner the user who owns this group, never null
     * @return The number of user groups that were deleted by this call.
     */
    public int delete(String groupName, User groupOwner) {
        //Assert checks on not null happens in the load method, so they are not required here
        UserGroup toBeDeleted = this.load(groupName, groupOwner);

        if ( toBeDeleted == null ) {
            return 0;
        }

        this.session.delete(toBeDeleted);
        return 1;
    }

    /**
     * load all userGroups persisted in the database which are owned by a given user<br/>
     * Asserts, that the groupOwner is not null.
     *
     * @param groupOwner the user who owns this group, never null
     * @return the list of all userGroups. May be an empty list, but never null
     */
    public List<UserGroup> loadAll(User groupOwner) {
        Assert.notNull(groupOwner);

        Query hql = this.session.createQuery("from UserGroup g where owner=:owner order by g.name asc");
        hql.setEntity("owner", groupOwner);
        @SuppressWarnings("unchecked")
        List<UserGroup> il = hql.list();

        return Collections.unmodifiableList(il);
    }

    /**
     * gets the number of user groups that are owned by the specified user<br/>
     * Asserts, that the groupOwner is not null.
     *
     * @param groupOwner the user who owns this group, never null
     * @return the number of user groups the user owns. 0 or bigger
     */
    public long getNumberOfGroupsOfOwner(User groupOwner) {
        Assert.notNull(groupOwner);

        Query hql = this.session.createQuery("select count(id) from UserGroup g where owner=:owner");
        hql.setEntity("owner", groupOwner);

        @SuppressWarnings("unchecked")
        Iterator<Long> resultIterator = hql.iterate();

        return resultIterator.hasNext() ? resultIterator.next() : 0L;
    }

    /**
     * load all userGroups persisted in the database which have a share relation with a specified program<br/>
     * Asserts, that the program is not null.
     *
     * @param program The program that is shared to the groups in question
     * @return the list of userGroups. May be an empty list, but never null
     */
    public List<UserGroup> loadAllWithWhichProgramIsShared(Program program) {
        Assert.notNull(program);

        Query hql = this.session.createQuery("select g from UserGroup as g join g.programs as p with p.id = :programId order by g.name asc");
        hql.setInteger("programId", program.getId());
        @SuppressWarnings("unchecked")
        List<UserGroup> il = hql.list();

        return Collections.unmodifiableList(il);
    }

    /**
     * renames a user group, identified by its owner and its name (both make up the "business" key of a group)<br/>
     * Asserts, that the groupName is not null.<br/>
     * Asserts, that the newGroupName is not null.<br/>
     * Asserts, that the groupOwner is not null.<br/>
     * Asserts, that a user group for the given data exists.<br/>
     * Asserts, that no other user group with the new name exists.<br/>
     *
     * @param groupName the name of the group, never null
     * @param newGroupName the new name of the group, never null
     * @param groupOwner the user who owns the group, never null
     * @return a pair of (message-key, group), if the group's name is changed successfully. The group is NOT null.
     */
    public Pair<Key, UserGroup> renameGroup(String groupName, String newGroupName, User groupOwner) {
        Assert.notNull(groupName);
        Assert.notNull(newGroupName);
        Assert.notNull(groupOwner);

        UserGroup group = this.load(groupName, groupOwner);
        Assert.notNull(group);

        UserGroup newGroup = this.load(newGroupName, groupOwner);
        Assert.isNull(newGroup);

        group.rename(newGroupName);
        return Pair.of(Key.GROUP_RENAME_SUCCESS, group);
    }

    /**
     * create a write lock for the table USERGROUP to avoid deadlocks. This is a no op if concurrency control is not 2PL, but MVCC
     */
    public void lockTable() {
        this.session.createSqlQuery("lock table USERGROUP write").executeUpdate();
        this.session.addToLog("lock", "is now aquired");
    }
}
