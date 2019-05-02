package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class UserDao extends AbstractDao<User> {
    /**
     * create a new DAO for users. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public UserDao(DbSession session) {
        super(User.class, session);
    }

    /**
     * Persists a user in the database.
     * Currently not used for user group members, because user group members can not be changed.
     *
     * @param account
     * @param password
     * @param roleAsString
     * @return the created user object; returns <code>null</code> if creation is unsuccessful (e.g. user already exists)
     * @throws Exception
     */
    public User persistUser(UserGroup userGroup, String account, String password, String roleAsString) throws Exception {
        Assert.notNull(account);
        Assert.notNull(password);
        Role role = Role.valueOf(roleAsString);
        Assert.notNull(role);
        User user = loadUser(null, account);
        if ( user == null ) {
            user = new User(userGroup, account);
            user.setPassword(password);
            user.setRole(role);
            this.session.save(user);
            return user;
        } else {
            return null;
        }
    }

    public User loadUser(UserGroup group, String account) {
        Assert.notNull(account);
        Query hql;
        if ( group == null ) {
            hql = this.session.createQuery("from User where userGroup is null and account=:account");
            hql.setString("account", account);
        } else {
            hql = this.session.createQuery("from User where userGroup=:userGroup and account=:account");
            hql.setEntity("userGroup", group);
            hql.setString("account", account);
        }
        return getOneOrNoUser(hql);
    }

    @SuppressWarnings("unchecked")
    public List<User> loadUsersOfGroup(UserGroup group) {
        Assert.notNull(group);
        Query hql = this.session.createQuery("from User where userGroup=:group");
        hql.setEntity("group", group);
        return hql.list();
    }

    public User loadUserOfGroupByGroupOwner(User groupOwner, String account) {
        Assert.notNull(account);
        Assert.notNull(groupOwner);

        Query hql = this.session.createQuery("from User where userGroup.owner=:owner and account=:account");
        hql.setEntity("owner", groupOwner);
        hql.setString("account", account);

        return getOneOrNoUser(hql);
    }

    public User loadUser(int id) {
        Assert.notNull(id);
        Query hql = this.session.createQuery("from User where id=:id");
        hql.setInteger("id", id);

        return getOneOrNoUser(hql);
    }

    public User loadUserByEmail(String email) {
        Assert.notNull(email);
        Query hql = this.session.createQuery("from User where email=:email");
        hql.setString("email", email);

        return getOneOrNoUser(hql);
    }

    @Deprecated
    public List<User> loadUserList(String sortBy, int offset, String tagFilter) {
        Query hql = this.session.createQuery("from User where tags=:tag order by " + sortBy);
        hql.setFirstResult(offset);
        hql.setMaxResults(10);
        hql.setString("tag", tagFilter);
        @SuppressWarnings("unchecked")
        List<User> il = hql.list();
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il;
        }
    }

    public int deleteUser(User userToBeDeleted) {
        Assert.notNull(userToBeDeleted);
        this.session.delete(userToBeDeleted);
        return 1;
    }

    /**
     * create a write lock for the table USER to avoid deadlocks. This is a no op if concurrency control is not 2PL, but MVCC
     */
    public void lockTable() {
        this.session.createSqlQuery("lock table USER write").executeUpdate();
        this.session.addToLog("lock", "is now aquired");
    }

    private User getOneOrNoUser(Query hql) {
        @SuppressWarnings("unchecked")
        List<User> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

}