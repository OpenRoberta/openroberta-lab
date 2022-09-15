package de.fhg.iais.roberta.persistence.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.WithSurrogateId;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * class for wrapping a hibernate session. This class eases the use of sessions. It creates transactions, after commits a new transaction is created
 * automatically. Closing a session forces a commit.<br>
 * <b>If neither close nor commit are called for this wrapper object, changes of the database w.r.t. to the wrappped session are <i>not persisted</i>!</b>
 *
 * @author rbudde
 */
public class DbSession {
    private static final Logger LOG = LoggerFactory.getLogger(DbSession.class);
    /**
     * if a db session is older than this value, it will be logged as a potential resource misuse
     */
    private static final long DURATION_TIMEOUT_MSEC_FOR_LOGGING = TimeUnit.SECONDS.toMillis(10);
    /**
     * if a db session is older than this value, it will be closed and removed to avoid a resource leak
     */
    private static final long DURATION_TIMEOUT_MSEC_FOR_CLEANUP = TimeUnit.MINUTES.toMillis(5);

    private Session session;

    // data for analyzing db session usage. Global storage, access MUST be atomically/synchronized
    private static final int DBSESSION_LOG_LIMIT = 10000; // after so many db sessions created, log the open sessions for debugging
    private static final AtomicLong dbSessionsSinceLastDebugLog = new AtomicLong(0);
    private static final AtomicLong currentOpenSessionCounter = new AtomicLong(0);
    private static final AtomicLong cleanedSessionCounter = new AtomicLong(0);
    private static final AtomicLong unusedSessionCounter = new AtomicLong(0);
    private static final AtomicLong sessionIdGenerator = new AtomicLong(0);
    private static final Map<Long, DbSession> sessionMap = new ConcurrentHashMap<>(); // potentially dangerous resource usage!

    // data for analyzing db session usage.
    private final long sessionId;
    private final long creationTime;
    private StringBuilder actions = new StringBuilder();
    private long numberOfActions = 0;

    /**
     * wrap a hibernate session.<br>
     * <b><i>Be very careful:</i> may only be called from {@link SessionFactoryWrapper}</b>
     *
     * @param session the hibernate session to be wrapped
     */
    DbSession(Session session) {
        LOG.debug("open session + start transaction");
        this.session = session;

        // for analyzing db session usage.
        currentOpenSessionCounter.incrementAndGet();
        this.sessionId = sessionIdGenerator.incrementAndGet();
        this.creationTime = new Date().getTime();
        sessionMap.put(this.sessionId, this);
    }

    /**
     * rollback the current transaction
     */
    public void rollback() {
        addTransaction("rollback"); // for analyzing db session usage.
        if ( this.session != null ) {
            LOG.info("rollback");
            Transaction transaction = this.session.getTransaction();
            transaction.rollback();
            this.session.close();
            this.session = null;
        } else {
            LOG.info("rollback attempt - session was removed already");
        }

    }

    /**
     * commit the current transaction and start a new one
     */
    public void commit() {
        LOG.debug("commit + start transaction");
        addTransaction("commit"); // for analyzing db session usage.
        this.session.getTransaction().commit();
        this.session.beginTransaction();
    }

    /**
     * commit the current transaction and close the session
     */
    public void close() {
        if ( this.session != null ) {
            addTransaction("commit + close"); // for analyzing db session usage.
            Transaction transaction = this.session.getTransaction();
            if ( transaction.isActive() ) {
                transaction.commit();
            }
            this.session.close();
            this.session = null;
        } else {
            addTransaction("close (but was already closed)");
        }

        // for analyzing db session usage
        long currentDbSessionsSinceLastDebugLog = dbSessionsSinceLastDebugLog.incrementAndGet();
        boolean logAnyway = currentDbSessionsSinceLastDebugLog >= DBSESSION_LOG_LIMIT;
        long sessionAge = new Date().getTime() - this.creationTime;
        boolean logTooOld = sessionAge > DURATION_TIMEOUT_MSEC_FOR_LOGGING;
        String reasonForLog = "";
        if ( logAnyway ) {
            dbSessionsSinceLastDebugLog.set(0);
            reasonForLog = " shown for debugging";
        }
        if ( logTooOld ) {
            reasonForLog = " is too old: created " + sessionAge + "msec ago";
        }
        if ( logAnyway || logTooOld ) {
            LOG.error("db session " + this.sessionId + reasonForLog + ". State of sessions is:\n" + getInfoAboutOpenDbDessions());
        }
        if ( this.numberOfActions == 0 ) {
            unusedSessionCounter.getAndIncrement();
        }
        if ( sessionMap.remove(this.sessionId) == null ) {
            LOG.error("FATAL: could not remove db session " + this.sessionId);
        } else {
            currentOpenSessionCounter.decrementAndGet();
        }
    }

    /**
     * load an object persisted in the database, identified by its key.<br>
     * <b>It is assumed, that the object exists! If in doubt, use get :-)</b> If the object doesn't reside in the db, accesssing the proxy returned will
     * generate a org.hibernate.ObjectNotFoundException later. This is probably <i>not</i> what you want.
     *
     * @param type the type of the object to be retrieved
     * @param id the primary key (usually a <i>technical</i> key)
     * @return the database object, never null
     */
    public <T> T load(Class<T> type, int id) {
        this.actions.append("load of " + type.getSimpleName() + " with id " + id + "\n"); // for analyzing db session usage.
        Object entityMayNotExist = this.session.load(type, id);
        ((WithSurrogateId) entityMayNotExist).getId();
        @SuppressWarnings("unchecked")
        T entity = (T) entityMayNotExist;
        return entity;
    }

    /**
     * get an object persisted in the database, identified by its key.
     *
     * @param type the type of the object to be retrieved
     * @param id the primary key (usually a <i>technical</i> key)
     * @return the database object, always initialized; null, if not found in the db
     */
    public <T> T get(Class<T> type, int id) {
        this.actions.append("get of " + type.getSimpleName() + " with id " + id + "\n"); // for analyzing db session usage.
        @SuppressWarnings("unchecked")
        T entity = (T) this.session.get(type, id);
        return entity;
    }

    /**
     * @return the hibernate session wrapped by this object. Be careful!
     */
    public Session getSession() {
        Assert.notNull(this.session);
        this.actions.append("getSession() - try to avoid that!\n"); // for analyzing db session usage.
        return this.session;
    }

    /**
     * create a HQL query
     *
     * @param query the HQL query
     * @return the Query object
     */
    public Query createQuery(String query) {
        addToLog("hql", query); // for analyzing db session usage.
        return this.session.createQuery(query);
    }

    /**
     * create a SQL query
     *
     * @param query the SQL query
     * @return the Query object
     */
    public NativeQuery createSqlQuery(String query) {
        addToLog("sql", query); // for analyzing db session usage.
        return this.session.createSQLQuery(query);
    }

    /**
     * persist an entity in the underlying database.
     *
     * @param toBePersisted the entity to be persisted
     * @return the key of the persisted object
     */
    public Serializable save(Object toBePersisted) {
        addSaveOrDelete("save", toBePersisted); // for analyzing db session usage.
        Serializable persisted = this.session.save(toBePersisted);
        this.session.flush();
        return persisted;
    }

    /**
     * delete an entity in the underlying database. The session is flushed.
     *
     * @param toBeDeleted the entity to be deleted
     */
    public void delete(Object toBeDeleted) {
        addSaveOrDelete("delete", toBeDeleted); // for analyzing db session usage.
        this.session.delete(toBeDeleted);
        this.session.flush();
    }

    // helper for analyzing db session usage.

    public void addToLog(String cmd, String query) {
        this.actions.append(cmd).append(": ").append(query).append("\n");
        this.numberOfActions++;
    }

    private void addTransaction(String kind) {
        addToLog("transaction", kind);
    }

    private void addSaveOrDelete(String cmd, Object object) {
        addToLog(cmd, object == null ? "null" : (object.getClass().getSimpleName() + " " + object.toString()));
    }

    /**
     * rollback and remove all those db sessions, that are outdated (older than {@link #DURATION_TIMEOUT_MSEC_FOR_CLEANUP}). This is a VERY dangerous operation!
     */
    public static void cleanupSessions() {
        final long now = new Date().getTime();
        long sessionIdToRemove = 0;
        boolean somethingExpired = false;
        for ( Entry<Long, DbSession> entry : sessionMap.entrySet() ) {
            try {
                sessionIdToRemove = entry.getKey();
                DbSession sessionToCheck = entry.getValue();
                if ( now - sessionToCheck.creationTime > DURATION_TIMEOUT_MSEC_FOR_CLEANUP ) {
                    LOG.error("rollback and remove of the expired database session " + sessionIdToRemove);
                    sessionToCheck.rollback();
                    sessionMap.remove(sessionIdToRemove);
                    currentOpenSessionCounter.decrementAndGet();
                    cleanedSessionCounter.incrementAndGet();
                    somethingExpired = true;
                }
            } catch ( Exception e ) {
                LOG.error("rollback and remove of the expired database session " + sessionIdToRemove + " FAILED", e);
                somethingExpired = true;
            }
        }
        if ( !somethingExpired ) {
            LOG.info("no sessions expired");
        }
    }

    /**
     * @return the number of open db sessions. Should be 0 or very close to zero, if no deadlock has occured.
     */
    public static long getOpenSessionCounter() {
        return currentOpenSessionCounter.get();
    }

    public static long getCleanedSessionCounter() {
        return cleanedSessionCounter.get();
    }

    /**
     * @return the number of unused db sessions since server start. Should be 0
     */
    public static long getUnusedSessionCounter() {
        return unusedSessionCounter.get();
    }

    /**
     * @return a full info about the state of open db sessions. May be a VERY LONG String!
     */
    public static String getInfoAboutOpenDbDessions() {
        StringBuilder sb = new StringBuilder();
        sb.append("number of db sessions created: ").append(sessionIdGenerator).append("\n");
        sb.append("number of db sessions created but not used (should be 0): ").append(unusedSessionCounter).append("\n");
        sb.append("number of db sessions currently in use (should be small): ").append(currentOpenSessionCounter).append("\n");
        sb.append("number of db sessions closed by the db cleanup thread: ").append(cleanedSessionCounter).append("\n");
        for ( DbSession dbSession : sessionMap.values() ) {
            sb.append("***** ").append(dbSession.sessionId).append(":\n").append(dbSession.actions);
        }
        return sb.toString();
    }

}
