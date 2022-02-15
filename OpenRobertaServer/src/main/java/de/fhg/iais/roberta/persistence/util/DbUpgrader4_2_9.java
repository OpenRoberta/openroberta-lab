package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;

import org.hibernate.Session;

final class DbUpgrader4_2_9 implements DbUpgraderInterface {
    private final SessionFactoryWrapper sessionFactoryWrapper;

    DbUpgrader4_2_9(SessionFactoryWrapper sessionFactoryWrapper) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
    }

    @Override
    public boolean isUpgradeDone() {
        Session hibernateSession = this.sessionFactoryWrapper.getHibernateSession();
        DbExecutor dbExecutor = DbExecutor.make(hibernateSession);
        try {
            int mailIdxAlreadyAdded =
                ((BigInteger) dbExecutor.oneValueSelect("select count(*) from INFORMATION_SCHEMA.SYSTEM_INDEXINFO where INDEX_NAME = 'USEREMAILIDX'")).intValue();
            return mailIdxAlreadyAdded > 0;
        } finally {
            hibernateSession.close();
        }
    }

    @Override
    public void run() {
        Session hibernateSession = this.sessionFactoryWrapper.getHibernateSession();
        DbSetup dbSetup = new DbSetup(hibernateSession);
        dbSetup
            .sqlFile(
                null, //
                null,
                "/dbUpgrade/4-2-9.sql");
        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

}
