package de.fhg.iais.roberta.main;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import de.fhg.iais.roberta.persistence.util.DbExecutor;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

public class ShutdownHook extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(ShutdownHook.class);
    private boolean embeddedDb;
    private Injector injector;

    public ShutdownHook(boolean embeddedDb, Injector injector) {
        this.embeddedDb = embeddedDb;
        this.injector = injector;
    }

    @Override
    public void run() {
        if ( embeddedDb ) {
            SessionFactoryWrapper sessionFactoryWrapper = this.injector.getInstance(SessionFactoryWrapper.class);
            Session nativeSession = sessionFactoryWrapper.getNativeSession();
            DbExecutor dbExecutor = DbExecutor.make(nativeSession);
            nativeSession.beginTransaction();
            try {
                dbExecutor.ddl("SHUTDOWN;");
            } finally {
                LOG.info("Shutdown. Database command SHUTDOWN finished for am embedded database");
            }
        } else {
            LOG.info("Shutdown. Database server not affected. May have been shutdown, may continue to operate - your choice.");
        }
    }

}
