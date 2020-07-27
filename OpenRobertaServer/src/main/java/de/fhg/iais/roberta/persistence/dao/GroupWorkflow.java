package de.fhg.iais.roberta.persistence.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * TODO: DELETE!
 *
 * @author pmaurer
 */
public class GroupWorkflow {

    private final DbSession session;
    private static final Logger LOG = LoggerFactory.getLogger(GroupWorkflow.class);

    /**
     * create a new GroupWorkflow object.
     */
    public GroupWorkflow(DbSession session) {
        this.session = session;
    }

    /**
     * allows to update access right for the programs of the group
     *
     * @param userName is a name of the group owner, not null, must exist
     * @param groupName is a name of the group owned by userName, not null, must exist
     * @param newAccessRight is a new access right given to the programs of the group, not null
     * @return key that shows the result of the change
     */
    public Key changeGroupAccessRight(String userName, String groupName, AccessRight newAccessRight) {
        UserDao userDao = new UserDao(this.session);
        UserGroupDao groupDao = new UserGroupDao(this.session);
        User groupOwner = userDao.loadUser(null, userName);
        Assert.notNull(groupOwner);
        UserGroup group = groupDao.load(groupName, groupOwner);
        Assert.notNull(group);
        AccessRight accessRightOld = group.getAccessRight();
        if ( accessRightOld == newAccessRight ) {
            return Key.ACCESS_RIGHT_UNCHANGED;
        }
        group.setAccessRight(newAccessRight);
        return Key.ACCESS_RIGHT_CHANGED;
    }

    /**
     * allows to create new student accounts for the group. The initial password is identical to the account name.
     *
     * @param userName is a name of the group owner, not null, must exist
     * @param groupName is a name of the group owned by userName, not null, must exist
     * @param accountPrefix is an account prefix for the group members to be created, not empty
     * @param startNumber is a initial number for the account count, i.e. the first account generated is prefix-startNumber, >=0
     * @param numberOfAccounts is a total number of the accounts to be created, >=0 and <=100
     * @return the result of the user creation
     */
    public Key addAccounts(String userName, String groupName, String accountPrefix, int startNumber, int numberOfAccounts) {
        UserDao userDao = new UserDao(this.session);
        UserGroupDao groupDao = new UserGroupDao(this.session);
        User groupOwner = userDao.loadUser(null, userName);
        Assert.notNull(groupOwner);
        UserGroup group = groupDao.load(groupName, groupOwner);
        Assert.notNull(group);
        Assert.nonEmptyString(accountPrefix);
        Assert.isTrue(startNumber >= 0);
        //TODO: Check for number of users in group, instead of checking for the number or accounts that shall be added.
        Assert.isTrue(numberOfAccounts >= 0 && startNumber + numberOfAccounts <= 99);
        for ( int i = startNumber; i < startNumber + numberOfAccounts; i++ ) {
            if ( userDao.loadUser(group, accountPrefix + i) != null ) {
                //TODO: Also check if the given user is part of the group. If not, its not the fault of the group admin and we need to simply skip it.
                return Key.GROUP_MEMBER_ERROR_ALREADY_EXISTS;
            }
        }
        for ( int i = startNumber; i < startNumber + numberOfAccounts; i++ ) {
            try {
                userDao.persistUser(group, accountPrefix + i, accountPrefix + i, Role.STUDENT.toString());
            } catch ( Exception e ) {
                GroupWorkflow.LOG.error("Add account failed for " + accountPrefix + i + " of group " + groupName, e);
                return Key.GROUP_ADD_ACCOUNT_PARTIAL;
            }
        }
        return Key.GROUP_ADD_ACCOUNT_OK;
    }

    /**
     * allows to delete a student account for a given group
     *
     * @param groupOwnerName is a name of the group owner, not null, must exist
     * @param groupName is a name of the group owned by userName, not null, must exist
     * @param accountToDeleteName is a name of the student account that must be deleted
     * @return the result of the user deletion
     */
    public Key deleteAccount(String groupOwnerName, String groupName, String accountToDeleteName) {
        UserDao userDao = new UserDao(this.session);
        UserGroupDao groupDao = new UserGroupDao(this.session);
        User groupOwner = userDao.loadUser(null, groupOwnerName);
        Assert.notNull(groupOwner);
        UserGroup group = groupDao.load(groupName, groupOwner);
        Assert.notNull(group);
        Assert.nonEmptyString(accountToDeleteName);
        User userToBeDeleted = userDao.loadUser(group, accountToDeleteName);
        Assert.notNull(userToBeDeleted);
        userDao.deleteUser(userToBeDeleted);
        return Key.GROUP_DELETE_USER_OK;
    }
}
