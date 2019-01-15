package de.fhg.iais.roberta.persistence.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * db-${serverVersionForDb}/openroberta-db a class with a static main method, responsible for copying one database to the other.<br>
 * <br>
 * Start the server for the copy:<br>
 * java -Xmx6G -cp lib/\* org.hsqldb.Server \<br>
 * --database.0 file:db-copy/openroberta-db-copy --dbname.0 openroberta-db-copy \<br>
 * --database.1 file:db-3.0.4/openroberta-db --dbname.1 openroberta-db &<br>
 * <br>
 * shutdown with:<br>
 * ./admin.sh -q --shutdown jdbc:hsqldb:hsql://localhost/openroberta-db-copy<br>
 * ./admin.sh -q --shutdown jdbc:hsqldb:hsql://localhost/openroberta-db<br>
 * <br>
 * create an empty copy database by calling:<br>
 * copier.createDatabaseWithoutConstraints();
 *
 * @author rbudde
 */
public class DatabaseCopier {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseCopier.class);

    private static final int COMMIT_HIGH_WATER_MARK = 1000;
    private static final String DB_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String DB_CONNECTION_COPY_CREATE = "jdbc:hsqldb:file:db-copy/openroberta-db-copy";
    private static final String DB_CONNECTION_COPY = "jdbc:hsqldb:hsql://localhost:9002/openroberta-db-copy";
    private static final String DB_CONNECTION = "jdbc:hsqldb:hsql://localhost:9001/openroberta-db";
    private static final String DB_USER = "orA";
    private static final String DB_PASSWORD = "Pid";

    private DatabaseCopier() {
    }

    public static void main(String[] args) throws Exception {
        DatabaseCopier copier = new DatabaseCopier();
        // copier.createDatabaseWithoutConstraints();
        // copier.copyDatabase();
        // copier.enforceConstraints();
    }

    private void createDatabaseWithoutConstraints() {
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", DB_CONNECTION_COPY_CREATE);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbSetup dbSetup = new DbSetup(nativeSession);
        nativeSession.beginTransaction();
        dbSetup.sqlFile("/create-tables-without-constraints.sql", null, null);
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }

    private void copyDatabase() {
        Connection dbConnectionFrom = null;
        Statement statementFrom = null;
        Connection dbConnectionTo = null;
        try {
            dbConnectionFrom = getDBConnection(DB_CONNECTION);
            statementFrom = dbConnectionFrom.createStatement();
            dbConnectionTo = getDBConnection(DB_CONNECTION_COPY);
            dbConnectionTo.setAutoCommit(false);

            copyTable(statementFrom, dbConnectionTo, "USER", 11);
            copyTable(statementFrom, dbConnectionTo, "LOST_PASSWORD", 4);
            copyTable(statementFrom, dbConnectionTo, "PENDING_EMAIL_CONFIRMATIONS", 4);
            copyTable(statementFrom, dbConnectionTo, "ROBOT", 5);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 0, 30000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 30000, 60000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 60000, 90000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 90000, 120000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 120000, 10000000);
            copyTable(statementFrom, dbConnectionTo, "USER_PROGRAM", 4);
            copyTable(statementFrom, dbConnectionTo, "USER_PROGRAM_LIKE", 6);
            copyTable(statementFrom, dbConnectionTo, "TOOLBOX", 11);
            copyTable(statementFrom, dbConnectionTo, "CONFIGURATION", 11);
            copyTable(statementFrom, dbConnectionTo, "CONFIGURATION_DATA", 2);

        } catch ( Exception e ) {
            LOG.error("exception when connecting to database or copying sql", e);
        } finally {
            closeQuietly(statementFrom);
            closeQuietly(dbConnectionFrom);
            closeQuietly(dbConnectionTo);
        }
    }

    private static void copyTable(Statement statementFrom, Connection dbConnectionTo, String tableName, int numberOfColumns, int minInclusive, int maxExclusive)
        throws Exception {
        PreparedStatement preparedStatementTo = null;
        int counter = 0;
        LOG.info("starting to copy table " + tableName);
        try {
            int numberOfUpdates = 0;
            StringBuilder prep = new StringBuilder();
            prep.append("insert into ").append(tableName).append(" values (");
            boolean first = true;
            for ( int i = 0; i < numberOfColumns; i++ ) {
                if ( first ) {
                    first = false;
                } else {
                    prep.append(",");
                }
                prep.append("?");
            }
            prep.append(")");
            preparedStatementTo = dbConnectionTo.prepareStatement(prep.toString());
            StringBuilder sel = new StringBuilder();
            sel.append("select * from ").append(tableName);
            if ( minInclusive >= 0 && maxExclusive >= 0 ) {
                sel.append(" where ID >= ").append(minInclusive).append(" and ID < ").append(maxExclusive);
            }
            ResultSet rs = statementFrom.executeQuery(sel.toString());
            while ( rs.next() ) {
                for ( int i = 1; i <= numberOfColumns; i++ ) {
                    preparedStatementTo.setObject(i, rs.getObject(i));
                }
                preparedStatementTo.executeUpdate();
                counter++;
                numberOfUpdates++;
                if ( numberOfUpdates > COMMIT_HIGH_WATER_MARK ) {
                    numberOfUpdates = 0;
                    dbConnectionTo.commit();
                    LOG.info("commit: " + counter);
                }
            }
        } catch ( Exception e ) {
            LOG.error("exception when connecting to database or copying sql", e);
        } finally {
            dbConnectionTo.commit();
            LOG.info("number of updates to table " + tableName + ": " + counter);
            closeQuietly(preparedStatementTo);
        }
        // TODO Auto-generated method stub

    }

    private static void copyTable(Statement statementFrom, Connection dbConnectionTo, String tableName, int numberOfColumns) throws Exception {
        copyTable(statementFrom, dbConnectionTo, tableName, numberOfColumns, -1, -1);
    }

    private void enforceConstraints() {
        Connection dbConnectionTo = null;
        Statement statementTo = null;
        try {
            dbConnectionTo = getDBConnection(DB_CONNECTION_COPY);
            dbConnectionTo.setAutoCommit(false);
            statementTo = dbConnectionTo.createStatement();
            sqlFile(statementTo, this.getClass().getResourceAsStream("/enforce-constraints.sql"));
        } catch ( Exception e ) {
            LOG.error("exception when connecting to database or executing sql", e);
        } finally {
            closeQuietly(statementTo);
            closeQuietly(dbConnectionTo);
        }
    }

    private static void sqlFile(Statement statementTo, InputStream sqlStmtFileStream) {
        String line = "";
        int count = 0;
        try {
            Reader reader = new InputStreamReader(sqlStmtFileStream, "UTF-8");
            BufferedReader in = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while ( (line = in.readLine()) != null ) {
                line = line.trim();
                if ( line.startsWith("--") || line.equals("") ) {
                    // next
                } else if ( line.endsWith(";") ) {
                    line = line.substring(0, line.length() - 1);
                    sb.append(line);
                    String sqlStmt = sb.toString().trim();
                    if ( sqlStmt.equals("") ) {
                        // leeres stmt
                    } else {
                        count++;
                        LOG.info("executing: " + sqlStmt);
                        statementTo.executeUpdate(sqlStmt);
                    }
                    sb = new StringBuilder();
                } else {
                    sb.append(line);
                    sb.append(" \n");
                }
            }
            LOG.info(count + " SQL-statements executed");
        } catch ( Exception e ) {
            LOG.error("Exception in sql stmt: " + count, e);
        } finally {
            if ( sqlStmtFileStream != null ) {
                try {
                    sqlStmtFileStream.close();
                } catch ( Throwable t ) {
                    // ok here
                }
            }
        }
    }

    private static void closeQuietly(AutoCloseable statement) {
        if ( statement != null ) {
            try {
                statement.close();
            } catch ( Exception e ) {
                // OK
            }
        }
    }

    private static Connection getDBConnection(String dbUri) {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch ( ClassNotFoundException e ) {
            LOG.error("driver not found", e);
            System.exit(12);
        }

        try {
            dbConnection = DriverManager.getConnection(dbUri, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch ( SQLException e ) {
            LOG.error("db connection failed", e);
            System.exit(12);
        }
        return dbConnection;
    }
}