package de.fhg.iais.roberta.persistence.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.dbc.Assert;

public class DbExecutor {
    private static Logger LOG = LoggerFactory.getLogger(DbExecutor.class);

    private final Session session;

    public static DbExecutor make(Session session) {
        return new DbExecutor(session);
    }

    private DbExecutor(Session session) {
        this.session = session;
    }

    public void sqlFile(String nameOfResourceContainingSql, String sqlReturningPositiveNumberIfSqlFileAlreadyLoaded) throws Exception {
        int result = ((BigInteger) oneValueSelect(sqlReturningPositiveNumberIfSqlFileAlreadyLoaded)).intValue();
        if ( result == 0 ) {
            sqlFile(this.getClass().getResourceAsStream(nameOfResourceContainingSql));
        } else {
            LOG.info("test sql says, that DDL of  " + nameOfResourceContainingSql + " has already been executed. Skipping execution.");
        }
    }

    public void sqlFile(InputStream sqlStmtFileStream) {
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
                        sqlStmt(sqlStmt);
                    }
                    this.session.flush();
                    sb = new StringBuilder();
                } else {
                    sb.append(line);
                    sb.append(" \n");
                }
            }
            sqlStmtFileStream.close();
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

    public void sqlStmt(String sqlStmt) {
        if ( isSelect(sqlStmt) ) {
            LOG.debug("SQL: " + sqlStmt);
            select(sqlStmt);
        } else if ( isChange(sqlStmt) ) {
            LOG.debug("UPD: " + sqlStmt);
            update(sqlStmt);
        } else if ( isDDL(sqlStmt) ) {
            LOG.debug("DDL: " + sqlStmt);
            ddl(sqlStmt);
        } else {
            LOG.error("Ignored: " + sqlStmt);
        }
    }

    public List<Object[]> select(String sqlStmt) {
        @SuppressWarnings("unchecked")
        List<Object[]> resultSet = this.session.createSQLQuery(sqlStmt).list();
        LOG.debug("got " + resultSet.size() + " rows");
        for ( Object result : resultSet ) {
            if ( result instanceof Object[] ) {
                LOG.debug("  " + Arrays.toString((Object[]) result));
            } else {
                LOG.debug("  " + result);
            }
        }
        return resultSet;
    }

    public Object oneValueSelect(String sqlStmt) {
        @SuppressWarnings("unchecked")
        List<Object> resultSet = this.session.createSQLQuery(sqlStmt).list();
        Assert.isTrue(resultSet.size() == 1);
        Object result = resultSet.get(0);
        LOG.debug(result.toString());
        return result;
    }

    public int update(String sqlStmt) {
        int result = this.session.createSQLQuery(sqlStmt).executeUpdate();
        LOG.debug("rows affected: " + result);
        return result;
    }

    public void ddl(String sqlStmt) {
        int result = this.session.createSQLQuery(sqlStmt).executeUpdate();
        LOG.debug("rows affected (probably 0): " + result);
    }

    private static boolean isSelect(String sqlStmt) {
        return sW(sqlStmt, "select ");
    }

    private static boolean isChange(String sqlStmt) {
        return sW(sqlStmt, "insert ") || sW(sqlStmt, "update ") || sW(sqlStmt, "delete ");
    }

    private static boolean isDDL(String sqlStmt) {
        return sW(sqlStmt, "drop ") || sW(sqlStmt, "create ");
    }

    private static boolean sW(String testString, String expected) {
        if ( testString.length() < expected.length() ) {
            return false;
        }
        return testString.substring(0, expected.length()).equalsIgnoreCase(expected);
    }
}