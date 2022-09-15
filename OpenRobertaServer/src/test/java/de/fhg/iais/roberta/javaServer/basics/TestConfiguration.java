package de.fhg.iais.roberta.javaServer.basics;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.util.DbExecutor;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.dbc.Assert;

public class TestConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(TestConfiguration.class);

    private static String CONNECTION_URL = "jdbc:hsqldb:mem:testInMemoryDb";
    private final SessionFactoryWrapper sessionFactoryWrapper;
    private final DbSetup memoryDbSetup;

    private TestConfiguration() {
        this.sessionFactoryWrapper = new SessionFactoryWrapper("/hibernate-cfg.xml", CONNECTION_URL);
        Session hibernateSession = this.sessionFactoryWrapper.getHibernateSession();
        this.memoryDbSetup = new DbSetup(hibernateSession);
        this.memoryDbSetup.createEmptyDatabase();
    }

    public SessionFactoryWrapper getSessionFactoryWrapper() {
        return this.sessionFactoryWrapper;
    }

    public DbSetup getMemoryDbSetup() {
        return this.memoryDbSetup;
    }

    public void deleteAllFromUserAndProgramTmpPasswords() {
        Session session = sessionFactoryWrapper.getHibernateSession();
        try {
            DbExecutor dbExecutor = DbExecutor.make(session);
            int counter = 0;
            List<String> toDelete = Arrays.asList("PROGRAM", "USER", "LOST_PASSWORD");
            for ( String openRobertaTable : toDelete ) {
                counter += dbExecutor.update("delete from " + openRobertaTable);
            }
            session.flush();
            LOG.info("deleted " + counter + " rows in tables " + toDelete + ".");
        } finally {
            if ( session.getTransaction().isActive() ) {
                session.getTransaction().commit();
            }
            session.close();
        }
        Assert.isTrue(memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_PROGRAM") == 0, "the table USER_PROGRAM should be empty");
    }

    public static TestConfiguration setup() {
        return new TestConfiguration();
    }
}
