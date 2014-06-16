package de.fhg.iais.roberta.persistence.connector;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.fhg.iais.roberta.dbc.Assert;

/**
 * class, that wraps the session factory of hibernate. Creating the session factory of hibernate is expensive. The session factory hides almost complete the
 * underlying database.<br>
 * - Retrieving sessions from the factory is thread-safe and cheap.<br>
 * - The generated sessions are not thread-safe.<br>
 * <br>
 * The class <b>should</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 * 
 * @author rbudde
 */
@Singleton
public final class SessionFactoryWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(SessionFactoryWrapper.class);
    private static final String CFG_XML = "sqlite-cfg.xml";
    private SessionFactory sessionFactory;

    public SessionFactoryWrapper() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure(CFG_XML);
            ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
            this.sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
            LOG.info("created");
        } catch ( Throwable ex ) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public final SessionWrapper getSession() {
        Assert.notNull(this.sessionFactory, "initialization of session factory failed");
        Session session = this.sessionFactory.openSession();
        return new SessionWrapper(session);
    }
}