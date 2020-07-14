package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actions to be done to upgrade the database by executing SQL scripts. Expects, that the database is already accessable.<br>
 * <b>workflow:</b><br>
 * 1. has a list of all server versions, that expect a database upgrade<br>
 * 2. for each if these upgrades check by executing a specific sql question, whether the changes have been applied earlier<br>
 * 3. if not, run the upgrade
 *
 * @author rbudde
 */
public class DbUpgrader {
    private static final Logger LOG = LoggerFactory.getLogger(DbUpgrader.class);

    /**
     * step through all database upgrade classes, check, whether the upgrade has been applied, if not execute the upgrade.<br>
     * There is one special case inwhich no upgrade should be done: if an empty db is created. In this case table USER does NOT exist TODO: the way,
     * inconsistencies are detected, is not optimal and will become error prone, if many upgrades exists in the future
     *
     * @param sessionFactoryWrapper
     */
    public static void checkForUpgrade(SessionFactoryWrapper sessionFactoryWrapper) {
        try {
            if ( isAnEmptyDatabaseBeingCreated(sessionFactoryWrapper) ) {
                LOG.info("a new, empty db is being created. No upgrades, of course");
                return;
            }

            /*
             * 3.1.0 renames 'ardu' to 'botnroll'. It doesn't add tables etc.
             * If no ardu programs exists, this upgrade must not be run. It never conflicts with other upgrades, if ...
             * ... if NEVER in the future a robot is called 'ardu'.
             */
            DbUpgrader3_1_0 dbUpgrader3_1_0 = new DbUpgrader3_1_0(sessionFactoryWrapper);
            boolean upgradeDone3_1_0 = dbUpgrader3_1_0.isUpgradeDone();
            if ( !upgradeDone3_1_0 ) {
                dbUpgrader3_1_0.run();
            }
            /*
             * 4.0.0 user group feature. A lot of new tables, a lot of DDL
             */
            DbUpgrader4_0_0 dbUpgrader4_0_0 = new DbUpgrader4_0_0(sessionFactoryWrapper);
            boolean upgradeDone4_0_0 = dbUpgrader4_0_0.isUpgradeDone();
            if ( !upgradeDone4_0_0 ) {
                dbUpgrader4_0_0.run();
            }

            /*
             * x.x.x ... ... ... copy the implementation from above. Start with a clone of the upgrader class DbUpgrader3_1_0
             */

            /*
             * check if at least one upgrade was performed
             */
            boolean atLeastOneUpgrade = !upgradeDone3_1_0 || !upgradeDone4_0_0; // OR of !upgradeDone*
            if ( !atLeastOneUpgrade ) {
                LOG.info("no db upgrades needed");
            } else {
                LOG.info("at least one db upgrade was EXECUTED!");
            }
        } catch ( Exception e ) {
            LOG.error("Abort: database upgrade fails. System exit 2", e);
            System.exit(2);
        }
    }

    /**
     * this check returns true, if an empty database is being created. This is true if the table USER is missing.
     *
     * @param sessionFactoryWrapper
     * @return true if an empty database is being created; false otherwise
     */
    private static boolean isAnEmptyDatabaseBeingCreated(SessionFactoryWrapper sessionFactoryWrapper) {
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        String sqlStmt = "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'USER'";
        int result = ((BigInteger) dbExecutor.oneValueSelect(sqlStmt)).intValue();
        nativeSession.close();
        return result == 0;
    }

}
