package de.fhg.iais.roberta.persistence.connector;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.dbc.Assert;

/**
 * class defining a singleton, that wraps the session factory of hibernate. Creating the session factory of hibernate is expensive. The session factory needs a
 * configuration file, that defines the sql dialect to be used. The session factory hides almost complete the underlying database.<br>
 * - Retrieving sessions from the factory is thread-safe and cheap.<br>
 * - The generated sessions are not thread-safe.
 * 
 * @author rbudde
 */
public final class SessionFactoryWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(SessionFactoryWrapper.class);
    private static final String CFG_XML = "sqlite-cfg.xml";
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure(CFG_XML);
            ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
        } catch ( Throwable ex ) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * @return a hibernate session wrapped inside a session-wrapper object. See {@link SessionWrapper} for details.
     */
    public static synchronized final SessionWrapper getSession() {
        Assert.notNull(sessionFactory, "initialization of session factory failed");
        Session session = sessionFactory.openSession();
        return new SessionWrapper(session);
    }
}