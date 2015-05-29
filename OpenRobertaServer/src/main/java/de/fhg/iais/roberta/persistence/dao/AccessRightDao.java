package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;

/**
 * DAO class to load and store access rights. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 */
public class AccessRightDao extends AbstractDao<AccessRight> {
    /**
     * create a new DAO for access rights. Access rights allow sharing of programs by users. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public AccessRightDao(DbSession session) {
        super(AccessRight.class, session);
    }

    /**
     * persist an access right in the database. If an access right already exists, update it.
     * modifies it.
     *
     * @param user the user whose access rights have to be changed
     * @param program the program affected
     * @param relation the access right of the user for the program
     * @return the access right object
     */
    public AccessRight persistAccessRight(User user, Program program, Relation relation) {
        Assert.notNull(user);
        Assert.notNull(program);
        Assert.notNull(relation);

        AccessRight accessRight = loadAccessRight(user, program);
        if ( accessRight == null ) {
            accessRight = new AccessRight(user, program, relation);
            this.session.save(accessRight);
        } else {
            accessRight.setRelation(relation);
        }
        return accessRight;
    }

    /**
     * load an access right from the database,
     * identified by user and program. It is always assumed, that
     * only one access right is allowed for every pair user/program
     *
     * @param user the user whose access rights are loaded
     * @param program the program affected
     * @return the access right object, null if no access right has been found in the DB
     */
    public AccessRight loadAccessRight(User user, Program program) {
        Assert.notNull(user);
        Assert.notNull(program);

        Query hql = this.session.createQuery("from AccessRight where user=:user and program=:program");
        hql.setEntity("user", user);
        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<AccessRight> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * load all access rights persisted in the database for a program given
     *
     * @return the list of all access rights, may be an empty list, but never null
     */
    public List<AccessRight> loadAccessRightsByProgram(Program program) {
        Assert.notNull(program);

        Query hql = this.session.createQuery("from AccessRight where program=:program");

        hql.setEntity("program", program);
        @SuppressWarnings("unchecked")
        List<AccessRight> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * load all access rights persisted in the database which pertain to a given user
     *
     * @return the list of all access rights, may be an empty list, but never null
     */
    public List<AccessRight> loadAccessRightsForUser(User user) {
        Assert.notNull(user);

        Query hql = this.session.createQuery("from AccessRight where user=:user");

        hql.setEntity("user", user);
        @SuppressWarnings("unchecked")
        List<AccessRight> il = hql.list();
        return Collections.unmodifiableList(il);

    }

    /**
     * delete a given access right
     *
     * @param user the user whose access right has to be deleted
     * @param program the program affected
     */
    public void deleteAccessRight(User user, Program program) {
        AccessRight toBeDeleted = loadAccessRight(user, program);
        if ( toBeDeleted != null ) {
            this.session.delete(toBeDeleted);
        }
    }
}