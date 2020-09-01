package de.fhg.iais.roberta.main;

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

import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

/**
 * <b>COPY A DATABASE TO ANOTHER DATABASE</b><br>
 * <br>
 * Steps:<br>
 * <ul>
 * <li>edit main(...) to run only <code>copier.createDatabaseWithoutConstraints()</code>. This creates (in embedded mode) the database as specified by
 * <code>DB_CONNECTION_COPY_CREATE</code> and executes the DDL SQL commands for table creation found in
 * <code>DbSetup.DB_CREATE_TABLES_WITHOUT_CONSTRAINTS_SQL</code>. Then it closes the created database
 * <li>now start the hsqldb target server like<br>
 * <code>java -Xmx8G -cp [[path-to-hsqldb-2.4.0.jar]] org.hsqldb.Server --port 9002 --database.0 file:db-copy/openroberta-db-copy --dbname.0 openroberta-db-copy &</code><br>
 * this server will later store the database copy in directory <code>db-copy</code>. It listens on the (non default port) 9002
 * <li>now <b>either</b> start the hsqldb source server like<br>
 * <code>java -Xmx8G -cp [[path-to-hsqldb-2.4.0.jar]] org.hsqldb.Server --database.0 file:[[db-3.0.4]]/openroberta-db --dbname.0 openroberta-db &</code><br>
 * this server will later generate the data from directory <code>db-3.0.4</code>. It listens on the default port 9001
 * <li><b>or</b> connect to a remote hsqldb source server by a ssh statement like<br>
 * <code>ssh rbudde@test.open-roberta.org -L9001:localhost:9001</code><br>
 * this tunnel will allow to connect from 'here' the port 9001 to port 9001 at 'there'. Port 9001 at 'there' is the default port for a hsqldb source server
 * already listening
 * <li>edit main(...) to run only <code>copier.copyDatabase()</code>. This will connect to the databases specified in the source URI <code>DB_CONNECTION</code>
 * and the target URI <code>DB_CONNECTION_COPY</code>. Then it copies using <code>copyTable(...)</code> all tables as specified. This takes about 2 minutes (Jan
 * 2019 :-)
 * <li>edit main(...) to run only <code>copier.enforceConstraints()</code>. This will connect to the the hsqldb target server using
 * <code>DB_CONNECTION_COPY</code> and execute the SQL statements from resource <code>DbSetup.DB_CREATE_CONSTRAINTS_SQL</code>. This enforces all constraints.
 * This may take 10 minutes (Jan 2019 :-).
 * <li><b>either</b> terminate the tunnel if used.
 * <li><b>or</b> decide, whether the hsqldb <i>source</i> server should continue to run. If not, connect a sql client to <code>DB_CONNECTION</code> and
 * shutdown. <b>BE CAREFUL <i>NOT</i> TO SHUTDOWN A DATABASE SERVER IN USE (e.g. by the public Jetty server!)</b>
 * <li>edit main(...) to run only <code>copier.shutdownCopy()</code>. This will run a <code>shutdown compact</code> command. This may take 10 minutes (Jan 2019
 * :-). The hsqldb <i>target</i> server will terminate. Have a look at the logging.
 * <li>a SQL client with a GUI is started by<br>
 * <code>java -Xmx4G -cp [[path-to-hsqldb-2.4.0.jar]] org.hsqldb.util.DatabaseManagerSwing</code>
 * </ul>
 *
 * @author rbudde
 */
public class DatabaseCopier {
    private static final int COMMIT_HIGH_WATER_MARK = 1000;
    private static final String DB_DRIVER = "org.hsqldb.jdbcDriver";

    private static final String DB_CONNECTION_COPY_CREATE = "jdbc:hsqldb:file:db-copy/openroberta-db-copy";

    private static final String DB_CONNECTION_COPY = "jdbc:hsqldb:hsql://localhost:9002/openroberta-db-copy";
    private static final String DB_CONNECTION = "jdbc:hsqldb:hsql://localhost:9001/openroberta-db";

    private final String dbUser;
    private final String dbPassword;

    private DatabaseCopier(String dbUser, String dbPassword) {
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public static void main(String[] args) throws Exception {
        p("usage: java -cp OpenRobertaServer/target/resources/\\* de.fhg.iais.roberta.main.DatabaseCopier DBUSER DBPASSWORD");
        if ( args == null || args.length != 2 ) {
            p("invalid parameter. Exit 12");
            System.exit(12);
        }
        @SuppressWarnings("unused")
        DatabaseCopier copier = new DatabaseCopier(args[0], args[1]);
        // copier.createDatabaseWithoutConstraints();
        // copier.copyDatabase();
        // copier.enforceConstraints();
        // copier.shutdownCopy();
    }

    @SuppressWarnings("unused")
    private void createDatabaseWithoutConstraints() {
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", DB_CONNECTION_COPY_CREATE);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbSetup dbSetup = new DbSetup(nativeSession);
        nativeSession.beginTransaction();
        dbSetup.sqlFile(null, null, DbSetup.DB_CREATE_TABLES_WITHOUT_CONSTRAINTS_SQL);
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }

    @SuppressWarnings("unused")
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

            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 0, 10000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 10000, 20000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 20000, 30000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 30000, 40000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 40000, 50000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 50000, 60000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 60000, 70000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 70000, 80000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 80000, 90000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 90000, 100000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 100000, 110000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 110000, 120000);
            copyTable(statementFrom, dbConnectionTo, "PROGRAM", 15, 120000, 10000000);

            copyTable(statementFrom, dbConnectionTo, "USER_PROGRAM", 4);
            copyTable(statementFrom, dbConnectionTo, "USER_PROGRAM_LIKE", 6);
            copyTable(statementFrom, dbConnectionTo, "CONFIGURATION", 11);
            copyTable(statementFrom, dbConnectionTo, "CONFIGURATION_DATA", 2);
        } catch ( Exception e ) {
            p("exception when connecting to database or copying sql", e);
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
        p("starting to copy table " + tableName);
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
                    p("commit: " + counter);
                }
            }
        } catch ( Exception e ) {
            p("exception when connecting to database or copying sql", e);
        } finally {
            dbConnectionTo.commit();
            p("number of updates to table " + tableName + ": " + counter);
            closeQuietly(preparedStatementTo);
        }
    }

    private static void copyTable(Statement statementFrom, Connection dbConnectionTo, String tableName, int numberOfColumns) throws Exception {
        copyTable(statementFrom, dbConnectionTo, tableName, numberOfColumns, -1, -1);
    }

    @SuppressWarnings("unused")
    private void shutdownCopy() {
        Connection dbConnectionTo = null;
        Statement statementTo = null;
        try {
            dbConnectionTo = getDBConnection(DB_CONNECTION_COPY);
            dbConnectionTo.setAutoCommit(false);
            statementTo = dbConnectionTo.createStatement();
            statementTo.executeUpdate("shutdown compact");
        } catch ( Exception e ) {
            p("exception when connecting to database or shutting down the server", e);
        } finally {
            closeQuietly(statementTo);
            closeQuietly(dbConnectionTo);
        }
    }

    @SuppressWarnings("unused")
    private void enforceConstraints() {
        Connection dbConnectionTo = null;
        Statement statementTo = null;
        try {
            dbConnectionTo = getDBConnection(DB_CONNECTION_COPY);
            dbConnectionTo.setAutoCommit(false);
            statementTo = dbConnectionTo.createStatement();
            sqlFile(statementTo, this.getClass().getResourceAsStream(DbSetup.DB_CREATE_CONSTRAINTS_SQL));
        } catch ( Exception e ) {
            p("exception when connecting to database or executing sql", e);
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
                        p("executing: " + sqlStmt);
                        statementTo.executeUpdate(sqlStmt);
                    }
                    sb = new StringBuilder();
                } else {
                    sb.append(line);
                    sb.append(" \n");
                }
            }
            p(count + " SQL-statements executed");
        } catch ( Exception e ) {
            p("Exception in sql stmt: " + count, e);
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

    private Connection getDBConnection(String dbUri) {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch ( ClassNotFoundException e ) {
            p("driver not found", e);
            System.exit(12);
        }

        try {
            dbConnection = DriverManager.getConnection(dbUri, dbUser, dbPassword);
            return dbConnection;
        } catch ( SQLException e ) {
            p("db connection failed", e);
            System.exit(12);
        }
        return dbConnection;
    }

    private static void p(String msg) {
        System.out.println(msg);
    }

    private static void p(String msg, Exception e) {
        System.out.println(msg);
        e.printStackTrace(System.out);
    }
}