package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbSetup {

    private static final Logger LOG = LoggerFactory.getLogger(DbSetup.class);
    //../OpenRobertaServer/src/main/resources
    private static final String DB_CREATE_TABLES_SQL = "/create-tables.sql";
    private static final String SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED =
        "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PROGRAM'";
    private static final String SQL_RETURNING_POSITIVENUMBER_IF_SETUP_WAS_SUCCESSFUL =
        "select count(*) from CONFIGURATION where NAME = 'Standardkonfiguration'";

    private final DbExecutor dbExecutor;

    public DbSetup(Session session) {
        this.dbExecutor = DbExecutor.make(session);
    }

    public void runDefaultRobertaSetup() {
        runDatabaseSetup(DB_CREATE_TABLES_SQL, SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED, SQL_RETURNING_POSITIVENUMBER_IF_SETUP_WAS_SUCCESSFUL);
    }

    public void runDatabaseSetup(String sqlResource, String sqlReturningPositiveIfSqlFileAlreadyLoaded, String sqlReturningPositiveIfSetupSuccessful) {
        try {
            this.dbExecutor.sqlFile(sqlResource, sqlReturningPositiveIfSqlFileAlreadyLoaded, sqlReturningPositiveIfSetupSuccessful);
        } catch ( Exception e ) {
            LOG.error("failure during execution of sql statements from classpath resource " + sqlResource, e);
        }
    }

    public int getOneInt(String sqlStmt) {
        return ((BigInteger) this.dbExecutor.oneValueSelect(sqlStmt)).intValue();
    }

}
