package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;

import org.hibernate.Session;

final class DbUpgrader4_0_0 implements DbUpgraderInterface {
    private final SessionFactoryWrapper sessionFactoryWrapper;

    DbUpgrader4_0_0(SessionFactoryWrapper sessionFactoryWrapper) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
    }

    @Override
    public boolean isUpgradeDone() {
        Session hibernateSession = this.sessionFactoryWrapper.getHibernateSession();
        DbExecutor dbExecutor = DbExecutor.make(hibernateSession);
        try {
            int userGroupTableCount =
                ((BigInteger) dbExecutor.oneValueSelect("select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'USERGROUP'")).intValue();
            return userGroupTableCount > 0;
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
                "/dbUpgrade/4-0-0.sql");
        hibernateSession.getTransaction().commit();
        hibernateSession.close();

    }

}
