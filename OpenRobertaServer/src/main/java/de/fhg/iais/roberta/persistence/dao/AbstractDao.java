package de.fhg.iais.roberta.persistence.dao;

import de.fhg.iais.roberta.persistence.util.DbSession;

/**
 * parent class for all DAO classes, that load and store business objects. This class contains convenience methods, that may be called from DAO classes
 *
 * @param <T> the type of the DAO class that extends this abstract class
 * @author rbudde
 */
public abstract class AbstractDao<T> {
    private Class<T> type;
    protected DbSession session;

    public AbstractDao(Class<T> type, DbSession session) {
        this.type = type;
        this.session = session;
    }

    /**
     * load an object persisted in the database, identified by its key.<br>
     * <b>It is assumed, that the object exists! If in doubt, use get :-)</b> If the object doesn't reside in the db, accesssing the proxy returned will
     * generate a org.hibernate.ObjectNotFoundException later. This is probably <i>not</i> what you want.
     *
     * @param id the primary key (usually a <i>technical</i> key)
     * @return the database object, never null
     */
    public T load(int id) {
        return this.session.load(this.type, id);
    }

    /**
     * get an object persisted in the database, identified by its key.
     *
     * @param id the primary key (usually a <i>technical</i> key)
     * @return the database object, always initialized; null, if not found in the db
     */
    public T get(int id) {
        return this.session.get(this.type, id);
    }
}
