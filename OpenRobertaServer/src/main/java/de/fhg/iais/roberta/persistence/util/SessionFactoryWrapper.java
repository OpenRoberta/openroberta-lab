package de.fhg.iais.roberta.persistence.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * class, that wraps the session factory of hibernate. Creating the session factory of hibernate is expensive. The session factory hides almost complete the
 * underlying database.<br>
 * - Retrieving sessions from the factory is thread-safe and cheap.<br>
 * - The generated sessions are not thread-safe.<br>
 * <br>
 * The class <b>should</b> be used as a singleton. We use <b>GUICE</b> to enforce that.
 *
 * @author rbudde
 */
public class SessionFactoryWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(SessionFactoryWrapper.class);
    private SessionFactory sessionFactory;

    /**
     * configure the session factory
     */
    @Inject
    public SessionFactoryWrapper(@Named("hibernate.config.xml") String cfgXml, @Named("hibernate.connection.url") String databaseUrl) {
        for ( int retrycount = 0; retrycount < 3; retrycount++ ) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure(cfgXml);
                configuration.setProperty("hibernate.connection.url", databaseUrl);
                this.sessionFactory = configuration.buildSessionFactory();
                LOG.info("session factory successfully created for dbUrl: " + databaseUrl + " - now checking database upgrade");
                DbUpgrader.checkForUpgrade(this);
                return;
            } catch ( Exception e ) {
                LOG.error("session factory creation failed (" + retrycount + "). Trying again in 5 seconds", e);
                try {
                    Thread.sleep(5000);
                } catch ( InterruptedException ie ) {
                    LOG.error("session factory creation interrupted. Server cannot start.", ie);
                    Thread.currentThread().interrupt();
                }
            }
        }
        String msg = "session factory creation failed. Server cannot start.";
        LOG.error(msg);
        throw new DbcException(msg);
    }

    /**
     * get a new {@link DbSession} from the session factory. This object contains convenience method for working with the data base.
     * It is a wrapper around a Hibernate session. The {@link DbSession} object and the Hibernate session contained are <b>not</b> thread-safe.
     *
     * @return a new db session with an initiated transaction; never null
     */
    public DbSession getSession() {
        Assert.notNull(this.sessionFactory, "previous attempt to initialize the session factory failed");
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        Assert.notNull(session, "creation of session failed");
        return new DbSession(session);
    }

    /**
     * get a new Hibernate session from the session factory. The session is <b>not</b> thread-safe. <b>Use rarely - know what you do</b><br>
     * All parts of the application use {@link DbSession}, which is a wrapper arount a Hibernate session
     *
     * @return the hibernate session, transaction begun, never null
     */
    public Session getHibernateSession() {
        Assert.notNull(this.sessionFactory, "previous attempt to initialize the session factory failed");
        Session session = this.sessionFactory.openSession();
        Assert.notNull(session, "creation of session failed");
        session.beginTransaction();
        return session;
    }
}