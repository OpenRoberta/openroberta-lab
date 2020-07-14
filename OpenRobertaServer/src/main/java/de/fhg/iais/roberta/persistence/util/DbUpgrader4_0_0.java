package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DbUpgrader4_0_0 implements DbUpgraderInterface {

    private static final Logger LOG = LoggerFactory.getLogger(DbUpgrader3_1_0.class);

    private final SessionFactoryWrapper sessionFactoryWrapper;

    DbUpgrader4_0_0(SessionFactoryWrapper sessionFactoryWrapper) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
    }

    @Override
    public boolean isUpgradeDone() {
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        try {
            int userGroupTableCount =
                ((BigInteger) dbExecutor.oneValueSelect("select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'USERGROUP'")).intValue();
            return userGroupTableCount > 0;
        } finally {
            nativeSession.close();
        }
    }

    @Override
    public void run() {
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();

        nativeSession.beginTransaction();

        DbSetup dbSetup = new DbSetup(nativeSession);
        dbSetup
            .sqlFile(
                null, //
                null,
                "/dbUpgrade/4-0-0.sql");

        nativeSession.getTransaction().commit();
        nativeSession.close();

    }

}
