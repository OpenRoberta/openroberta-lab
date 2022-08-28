package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * upgrade to version 3.1.0
 *
 * @author rbudde
 */
public class DbUpgrader3_1_0 implements DbUpgraderInterface {
    private static final Logger LOG = LoggerFactory.getLogger(DbUpgrader3_1_0.class);

    private final SessionFactoryWrapper sessionFactoryWrapper;

    DbUpgrader3_1_0(SessionFactoryWrapper sessionFactoryWrapper) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
    }

    /**
     * this db upgrade is NEEDED, if in table ROBOT the name 'ardu' exists and at least one program uses this id. 'ardu' should be replaced by 'botnroll'<br>
     * Thus it is "done", i.e. not needed, if 'ardu' is missing or unused. TODO: It is unclear, why in the prod db an unused robot 'ardu' exists. Should be
     * removed
     */
    @Override
    public boolean isUpgradeDone() {
        Session hibernateSession = this.sessionFactoryWrapper.getHibernateSession();
        DbExecutor dbExecutor = DbExecutor.make(hibernateSession);
        try {
            int ardus = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from ROBOT where NAME = 'ardu'")).intValue();
            int botnroll = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from ROBOT where NAME = 'botnroll'")).intValue();
            if ( ardus == 0 ) {
                return true;
            } else if ( botnroll == 0 ) {
                return false;
            } else {
                return true;
            }
        } finally {
            hibernateSession.close();
        }
    }

    /**
     * execute the update<br>
     * 1. robot_id for 'ardu' (192) becomes id of 'botnroll' (203) in both programs and configurations<br>
     * 2. update xml in PROGRAM and CONFIGURATION_DATA to reflect the new name 'botnroll' instead of 'ardu'
     */
    @Override
    public void run() {
        LOG.info("upgrade of the database starts");
        Session hibernateSession = this.sessionFactoryWrapper.getHibernateSession();
        DbExecutor dbExecutor = DbExecutor.make(hibernateSession);
        LOG.info("inserting 'botnroll' into the ROBOT table");
        dbExecutor.update("insert into ROBOT values (DEFAULT,'botnroll',now,null,0)");
        // TODO: why is 'ardu' inserted. Compatibility reasons?
        if ( ((BigInteger) dbExecutor.oneValueSelect("select count(*) from ROBOT where NAME = 'ardu'")).longValue() == 0 ) {
            LOG.info("inserting 'ardu' into the ROBOT table");
            dbExecutor.update("insert into ROBOT values (DEFAULT,'ardu',now,null,0)");
        } else {
            LOG.info("'ardu' found in the ROBOT table");
        }
        DbSetup dbSetup = new DbSetup(hibernateSession);
        dbSetup
            .sqlFile(
                null, //
                null,
                "/dbUpgrade/3-1-0.sql");
        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }
}
