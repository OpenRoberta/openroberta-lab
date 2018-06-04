package de.fhg.iais.roberta.persistence.util;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Util1;

/**
 * assembles all actions to be done to upgrade the server version. This may include<br>
 * - executing SQL scripts and<br>
 * - loading, transforming and updating rows in the database<br>
 * the method {@link Upgrader#to(String)} either contains small updates or should delegate to a method for updates with many actions
 *
 * @author rbudde
 */
public class Upgrader {
    private static final Logger LOG = LoggerFactory.getLogger(Upgrader.class);
    private final File databaseParentdir;
    private final String[] previousServerVersions;
    private final String actualServerVersion;

    public static void checkForUpgrade(String serverVersionForDbDirectory, File databaseParentdir) {
        if ( !databaseParentdir.isDirectory() ) {
            LOG.error("Abort: database parent directory is invalid: " + databaseParentdir.getAbsolutePath());
            System.exit(2);
        }
        File databaseDir = new File(databaseParentdir, "db-" + serverVersionForDbDirectory);
        if ( databaseDir.isDirectory() ) {
            LOG.info("database version " + serverVersionForDbDirectory + " is uptodate");
        } else {
            // server version upgrade is necessary
            Properties robertaProperties = Util1.loadProperties(false, null);
            String[] previousServerVersions = robertaProperties.getProperty("openRobertaServer.history").split(",");
            if ( previousServerVersions.length > 0 && serverVersionForDbDirectory.equals(previousServerVersions[0]) ) {
                // if the first entry is the actual version, remove the entry ...
                previousServerVersions = Arrays.copyOfRange(previousServerVersions, 1, previousServerVersions.length);
            }
            try {
                new Upgrader(databaseParentdir, previousServerVersions, serverVersionForDbDirectory).upgrade();
            } catch ( Exception e ) {
                LOG.error("Abort: server version upgrade fails", e);
                System.exit(2);
            }
        }
    }

    private Upgrader(File databaseParentdir, String[] previousServerVersions, String actualServerVersion) {
        this.databaseParentdir = databaseParentdir;
        this.previousServerVersions = previousServerVersions;
        this.actualServerVersion = actualServerVersion;
    }

    private void upgrade() throws Exception {
        while ( true ) {
            boolean oldVersionFound = false;
            for ( int previousIndex = 0; previousIndex < this.previousServerVersions.length; previousIndex++ ) {
                String previousServerVersion = this.previousServerVersions[previousIndex];
                File dbPreviousDir = new File(this.databaseParentdir, "db-" + previousServerVersion);
                if ( dbPreviousDir.isDirectory() ) {
                    LOG.info("The last version, that was found for this installation, is " + previousServerVersion);
                    boolean upgradeToActualVersion = previousIndex == 0;
                    String nextServerVersion = upgradeToActualVersion ? this.actualServerVersion : this.previousServerVersions[previousIndex - 1];
                    File dbActualDir = new File(this.databaseParentdir, "db-" + nextServerVersion);
                    if ( dbActualDir.exists() ) {
                        LOG.error("Abort: The version " + nextServerVersion + " to upgrade to has already a database directory");
                        System.exit(2);
                    }
                    FileUtils.copyDirectory(dbPreviousDir, dbActualDir);
                    Upgrader.to(nextServerVersion, dbActualDir.getPath().replaceAll("\\\\", "/"));
                    if ( upgradeToActualVersion ) {
                        return;
                    } else {
                        oldVersionFound = true;
                        break;
                    }
                }
            }
            if ( !oldVersionFound ) {
                LOG.error("No old version to upgrade found. Upgrade failed. System cannot run!");
                System.exit(2);
            }
        }
    }

    /**
     * <i>execute the updates to the version given as parameter</i>.<br>
     * <b>Note 1</b> If no update is required (that means: the old database structure and the old database content are that, what is required by the new
     * version, there has nothing to be done here.<br>
     * <b>Note 2</b> If updates are required, please add them at the beginning of the if. If the upgrade contains many actions, please delegate to a method or a
     * class whose name contain the server version, e.g. <code>class Upgrader_2_3_0</code><br>
     * <br>
     * <b>If you update the database with SQL, there are no restrictions. If you update the database programmatically, please note, that the Hibernate
     * controlled classes are bound to the actual version and not to the version you transform from and to. Using plain SQL, a result list of Object[] and
     * avoiding Hql and database entities is the <i>only</i> safe method to avoid crashes.</b>
     *
     * @param versionToUpgradeTo the target version
     * @param pathToDatabaseDirectory path to the directory which contains the database files
     */
    private static void to(String versionToUpgradeTo, String pathToDatabaseDirectory) {
        if ( versionToUpgradeTo == null ) {
            LOG.error("Abort: serverVersion to upgrade to is null");
            System.exit(2);
        }
        String dbUrl = "jdbc:hsqldb:file:" + pathToDatabaseDirectory + "/openroberta-db;ifexists=true";
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", dbUrl);
        // if the version is not detected in the conditions of if/else-if, it is expected, that NOTHING CHANGES.
        if ( versionToUpgradeTo.equals("2.3.0") ) {
            LOG.info("upgrade to 2.3.0 WITH database changes");
            new Upgrader_2_3_0(sessionFactoryWrapper).run();
        } else if ( versionToUpgradeTo.equals("2.2.7") ) {
            LOG.info("upgrade to 2.2.7 WITH database changes");
            Session nativeSession = sessionFactoryWrapper.getNativeSession();
            nativeSession.beginTransaction();
            DbSetup dbSetup = new DbSetup(nativeSession);
            dbSetup.createEmptyDatabase(
                "/update-2-2-7.sql",
                "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PENDING_EMAIL_CONFIRMATIONS'",
                "select count(*) from USER where ACCOUNT = 'Gallery'");
            nativeSession.createSQLQuery("shutdown").executeUpdate();
            nativeSession.close();
        } else {
            LOG.info("upgrade to " + versionToUpgradeTo + " without database changes");
        }
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        nativeSession.beginTransaction();
        try {
            dbExecutor.ddl("SHUTDOWN COMPACT;");
        } finally {
            LOG.info("shutdown compact succeeded for a database db-" + versionToUpgradeTo);
        }
    }
}
