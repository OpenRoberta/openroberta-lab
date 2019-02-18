package de.fhg.iais.roberta.persistence.util;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * medium complex upgrade to version 2.3.0
 *
 * @author rbudde
 */
public class Upgrader_3_1_0 {
    private static final Logger LOG = LoggerFactory.getLogger(Upgrader_3_1_0.class);

    private final SessionFactoryWrapper sessionFactoryWrapper;
    private Session nativeSession;

    Upgrader_3_1_0(SessionFactoryWrapper sessionFactoryWrapper) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
    }

    /**
     * execute the update<br>
     * 1. robot_id for 'ardu' (192) becomes id of 'botnroll' (203) in both programs and configurations<br>
     * 2. update xml in PROGRAM and CONFIGURATION_DATA to reflect the new name 'botnroll' instead of 'ardu'
     */
    public void run() {
        this.nativeSession = sessionFactoryWrapper.getNativeSession();
        new DbSetup(nativeSession)
            .sqlFile(
                "select count(*) from ROBOT where NAME = 'botnroll'", //
                "select count(*) from ROBOT where NAME = 'botnroll'", //
                "/update-3-1-0.sql");
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }
}
