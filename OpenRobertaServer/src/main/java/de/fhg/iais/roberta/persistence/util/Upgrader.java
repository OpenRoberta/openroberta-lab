package de.fhg.iais.roberta.persistence.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Upgrader(File databaseParentdir, String[] previousServerVersions, String actualServerVersion) {
        this.databaseParentdir = databaseParentdir;
        this.previousServerVersions = previousServerVersions;
        this.actualServerVersion = actualServerVersion;
    }

    public void upgrade() throws Exception {
        while ( true ) {
            for ( int previousIndex = 0; previousIndex < previousServerVersions.length; previousIndex++ ) {
                String previousServerVersion = previousServerVersions[previousIndex];
                File dbPreviousDir = new File(databaseParentdir, "db-" + previousServerVersion);
                if ( dbPreviousDir.isDirectory() ) {
                    LOG.info("The last version, that was found for this installation, is " + previousServerVersion);
                    boolean upgradeToActualVersion = previousIndex == 0;
                    String nextServerVersion = upgradeToActualVersion ? actualServerVersion : previousServerVersions[previousIndex - 1];
                    File dbActualDir = new File(databaseParentdir, "db-" + nextServerVersion);
                    if ( dbActualDir.exists() ) {
                        LOG.error("Abort: The version " + nextServerVersion + " to upgrade to has already a database directory");
                        System.exit(2);
                    }
                    FileUtils.copyDirectory(dbPreviousDir, dbActualDir);
                    Upgrader.to(nextServerVersion, dbActualDir.getPath().replaceAll("\\\\", "/"));
                    if ( upgradeToActualVersion ) {
                        return;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * execute the updates. Please add new updates (for server versions with higher versions) at the beginning of the if. If the upgrade contains many actions,
     * please delegate to a method or a class whose name contain the server version, e.g. <code>class Upgrader_2_3_0</code><br>
     * <br>
     * <b>If you update the database with SQL, there are no restrictions. If you update the database programmatically, please note, that the Hibernate
     * controlled classes are bound to the actual version and not to the version you transform from and to. Using plain SQL, a result list of Object[] and
     * avoiding Hql and database entities is the <i>only</i> safe method to avoid crashes.</b>
     *
     * @param serverVersion the target version
     * @param pathToDatabaseDirectory path to the directory which contains the database files
     */
    public static void to(String serverVersion, String pathToDatabaseDirectory) {
        if ( serverVersion == null ) {
            LOG.error("Abort: serverVersion to upgrade to is null");
            System.exit(2);
        }
        String dbUrl = "jdbc:hsqldb:file:" + pathToDatabaseDirectory + "/openroberta-db";
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", dbUrl);
        LOG.info("upgrading to server version " + serverVersion);
        if ( serverVersion.equals("2.3.0") ) {
            new Upgrader_2_3_0(sessionFactoryWrapper).run();
        } else if ( serverVersion.equals("2.2.7") ) {
            Session nativeSession = sessionFactoryWrapper.getNativeSession();
            nativeSession.beginTransaction();
            DbSetup dbSetup = new DbSetup(nativeSession);
            dbSetup.runDatabaseSetup(
                "/update-2-2-7.sql",
                "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PENDING_EMAIL_CONFIRMATIONS'",
                "select count(*) from USER where ACCOUNT = 'Gallery'");
            nativeSession.createSQLQuery("shutdown").executeUpdate();
            nativeSession.close();
        } else if ( serverVersion.equals("2.2.6") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.5") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.4") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.3") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.2") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.1") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.0") ) {
            // do nothing
        } else if ( serverVersion.equals("2.1.0") ) {
            // do nothing
        } else if ( serverVersion.equals("1.9.0") ) {
            // do nothing
        } else {
            LOG.error("Abort: serverVersion to upgrade to not valid: " + serverVersion);
            System.exit(2);
        }
    }
}
