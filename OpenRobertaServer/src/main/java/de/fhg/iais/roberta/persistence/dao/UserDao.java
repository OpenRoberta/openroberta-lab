package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
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
     * ...
     *
     * @param account
     * @param password
     * @param roleAsString
     * @return the created user object; returns <code>null</code> if creation is unsuccessful (e.g. user already exists)
     * @throws Exception
     */
    public User persistUser(String account, String password, String roleAsString) throws Exception {
        Assert.notNull(account);
        Assert.notNull(password);
        Role role = Role.valueOf(roleAsString);
        Assert.notNull(role);
        User user = loadUser(account);
        if ( user == null ) {
            user = new User(account);
            user.setPassword(password);
            user.setRole(role);
            this.session.save(user);
            return user;
        } else {
            return null;
        }
    }

    public User loadUser(String account) {
        Assert.notNull(account);
        Query hql = this.session.createQuery("from User where account=:account");
        hql.setString("account", account);

        return checkUserExistance(hql);
    }

    public User loadUser(int id) {
        Assert.notNull(id);
        Query hql = this.session.createQuery("from User where id=:id");
        hql.setInteger("id", id);

        return checkUserExistance(hql);
    }

    public User loadUserByEmail(String email) {
        Assert.notNull(email);
        Query hql = this.session.createQuery("from User where email=:email");
        hql.setString("email", email);

        return checkUserExistance(hql);
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

    private User checkUserExistance(Query hql) {
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