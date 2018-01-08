package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * medium complex upgrade to version 2.3.0
 *
 * @author rbudde
 */
public class Upgrader_2_3_0 {
    private static final Logger LOG = LoggerFactory.getLogger(Upgrader_2_3_0.class);

    private final SessionFactoryWrapper sessionFactoryWrapper;
    private Session nativeSession;
    private DbSession dbSession;

    Upgrader_2_3_0(SessionFactoryWrapper sessionFactoryWrapper) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
    }

    /**
     * execute the update<br>
     * 1. DDL: create and update tables<br>
     * 2. update CONFIGURATION with hashes and fill new table CONFIGURATION_DATA<br>
     * 3. update PROGRAM with hashes 3. DDL: update table CONFIGURATION<br>
     */
    public void run() {
        this.nativeSession = sessionFactoryWrapper.getNativeSession();
        this.dbSession = new DbSession(nativeSession);
        // step 1
        new DbSetup(nativeSession).createEmptyDatabase(
            "/update-2-3-0--step-1.sql",
            "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'USER_LIKE'",
            "select count(*) from PROGRAM where VIEWED = 0");
        step2();
        new DbSetup(nativeSession).createEmptyDatabase(//
            "/update-2-3-0--step-3.sql",
            null,
            "select count(*) from CONFIGURATION_DATA");
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }

    private void step2() {
        SQLQuery updConf = dbSession.createSqlQuery("update CONFIGURATION set CONFIGURATION_HASH=:hash where CONFIGURATION_TEXT=:text");
        SQLQuery insConfData = dbSession.createSqlQuery("insert into CONFIGURATION_DATA values (:hash,:text)");
        SQLQuery checkConfData = dbSession.createSqlQuery("select count(*) from CONFIGURATION_DATA where CONFIGURATION_HASH=:hash");

        // step 1: load default configurations for all robots
        @SuppressWarnings("unchecked")
        List<String[]> confs = dbSession.createSqlQuery("select NAME, CONFIGURATION_TEXT from CONFIGURATION").list();
        for ( Object[] conf : confs ) {
            Assert.isTrue(conf.length == 2);
            String confName = (String) conf[0];
            String confText = (String) conf[1];
            String confHash = ConfigurationData.createHash(confText);
            updConf.setString("hash", confHash).setString("text", confText).executeUpdate();
            if ( ((BigInteger) checkConfData.setString("hash", confHash).uniqueResult()).intValue() > 0 ) {
                LOG.info("!!! for name " + confName + " hash " + confHash + " is already in CONFIGURATION_DATA");
            } else {
                insConfData.setString("hash", confHash).setString("text", confText).executeUpdate();
                LOG.info("for name " + confName + " hash " + confHash + " is inserted in CONFIGURATION_DATA");
            }
        }
        dbSession.commit();
    }
}
