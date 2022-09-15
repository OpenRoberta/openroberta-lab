package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.query.Query;

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
     * Persists a new user in the database.
     *
     * @param userGroup null, if a regular user should be created
     * @param account not null
     * @param password not null
     * @param roleAsString not null, String representation of a valid role
     * @return the created user object; returns <code>null</code> if creation is unsuccessful (e.g. user already exists)
     */
    public User persistNewUser(UserGroup userGroup, String account, String password, String roleAsString) throws Exception {
        Assert.notNull(account);
        Assert.notNull(password);
        Role role = Role.valueOf(roleAsString);
        User user = loadUser(userGroup, account);
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
            hql.setParameter("account", account);
        } else {
            hql = this.session.createQuery("from User where userGroup=:userGroup and account=:account");
            hql.setParameter("userGroup", group);
            hql.setParameter("account", account);
        }
        return getOneOrNoUser(hql);
    }

    @SuppressWarnings("unchecked")
    public List<User> loadUsersOfGroup(UserGroup group) {
        Assert.notNull(group);
        Query hql = this.session.createQuery("from User where userGroup=:group");
        hql.setParameter("group", group);
        return hql.list();
    }

    public User loadUserOfGroupByGroupOwner(User groupOwner, String account) {
        Assert.notNull(account);
        Assert.notNull(groupOwner);

        Query hql = this.session.createQuery("from User where userGroup.owner=:owner and account=:account");
        hql.setParameter("owner", groupOwner);
        hql.setParameter("account", account);

        return getOneOrNoUser(hql);
    }

    public User loadUser(int id) {
        Assert.notNull(id);
        Query hql = this.session.createQuery("from User where id=:id");
        hql.setParameter("id", id);

        return getOneOrNoUser(hql);
    }

    public User loadUserByEmail(String email) {
        Assert.notNull(email);
        Query hql = this.session.createQuery("from User where email=:email");
        hql.setParameter("email", email);

        return getOneOrNoUser(hql);
    }

    /**
     * load a list of users filtered by a tag (looked up in column "tag"), sorted by a column given
     *
     * @param tagFilter filter to restrict result list to users with this tag
     * @param sortBy sort column
     * @param offset the index in the result list for the first user returned to the caller
     * @param maxResults the maximal number of users to be returned
     * @return list of filtered users sorted ascending
     */
    public List<User> loadUserList(String tagFilter, String sortBy, int offset, int maxResults) {
        Query hql = this.session.createQuery("from User where tags=:tag order by " + sortBy);
        hql.setFirstResult(offset);
        hql.setMaxResults(10);
        hql.setParameter("tag", tagFilter);
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