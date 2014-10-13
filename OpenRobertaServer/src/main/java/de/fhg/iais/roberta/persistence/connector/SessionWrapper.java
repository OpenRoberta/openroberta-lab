package de.fhg.iais.roberta.persistence.connector;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.dbc.Assert;

/**
 * class for wrapping a hibernate session. This class eases the use of sessions. It creates transactions, after commits a new transaction is created
 * automatically. Closing a session forces a commit.<br>
 * <b>If neither close nor commit are called for this wrapper objects, changes of the database w.r.t. to the wrappped session are <i>not persisted</i>!</b>
 *
 * @author rbudde
 */
public class SessionWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(SessionWrapper.class);
    private Session session;

    /**
     * wrap a hibernate session. Package visible: may only be called from {@link SessionFactoryWrapper}
     *
     * @param session the hibernate session to be wrapped
     */
    SessionWrapper(Session session) {
        LOG.debug("open session + start transaction");
        this.session = session;
        this.session.beginTransaction();
    }

    /**
     * rollback the current transaction
     */
    public void rollback() {
        LOG.info("rollback");
        Transaction transaction = this.session.getTransaction();
        if ( transaction.isActive() ) {
            transaction.rollback();
        }
    }

    /**
     * commit the current transaction and start a new one
     */
    public void commit() {
        LOG.debug("commit + start transaction");
        Transaction transaction = this.session.getTransaction();
        if ( transaction.isActive() ) {
            transaction.commit();
        }
        this.session.beginTransaction();
    }

    /**
     * commit the current transaction and close the session
     */
    public void close() {
        LOG.debug("close session (after commit)");
        Transaction transaction = this.session.getTransaction();
        if ( transaction.isActive() ) {
            transaction.commit();
        }
        this.session.close();
        this.session = null;
    }

    /**
     * @return the hibernate session wrapped by this object. Be careful!
     */
    public Session getSession() {
        Assert.notNull(this.session);
        return this.session;
    }

    /**
     * create a HQL query
     *
     * @param query the SQL query
     * @return the Query object
     */
    public Query createQuery(String query) {
        return this.session.createQuery(query);
    }

    /**
     * persist an entity in the underlying database.
     *
     * @param toBePersisted the entity to be persisted
     * @return the key of the persisted object
     */
    public Serializable save(Object toBePersisted) {
        return this.session.save(toBePersisted);
    }

    /**
     * delete an entity in the underlying database. The session is flushed.
     *
     * @param toBeDeleted the entity to be deleted
     */
    public void delete(Object toBeDeleted) {
        this.session.delete(toBeDeleted);
        this.session.flush();
    }
}
