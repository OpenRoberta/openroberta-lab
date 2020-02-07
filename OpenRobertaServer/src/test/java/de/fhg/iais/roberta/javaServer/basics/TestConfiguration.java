package de.fhg.iais.roberta.javaServer.basics;

import org.hibernate.Session;

import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

public class TestConfiguration {
    private static String CONNECTION_URL = "jdbc:hsqldb:mem:testInMemoryDb;hsqldb.tx=mvcc"; // use MVCC as transaction manager in tests, too!
    private final SessionFactoryWrapper sessionFactoryWrapper;
    private final DbSetup memoryDbSetup;

    private TestConfiguration() {
        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", CONNECTION_URL);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.createEmptyDatabase();
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
    }

    public SessionFactoryWrapper getSessionFactoryWrapper() {
        return sessionFactoryWrapper;
    }

    public DbSetup getMemoryDbSetup() {
        return memoryDbSetup;
    }

    public static TestConfiguration setup() {
        return new TestConfiguration();
    }
}
