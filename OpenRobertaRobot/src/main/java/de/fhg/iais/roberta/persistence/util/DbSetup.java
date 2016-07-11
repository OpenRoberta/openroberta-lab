package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.Assert;

public class DbSetup {

    private static final Logger LOG = LoggerFactory.getLogger(DbSetup.class);
    //../OpenRobertaServer/src/main/resources
    private static final String DB_CREATE_TABLES_SQL = "/create-tables.sql";
    private static final String SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED =
        "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PROGRAM'";
    private static final String SQL_RETURNING_POSITIVENUMBER_IF_SETUP_WAS_SUCCESSFUL = "select count(*) from CONFIGURATION where NAME = 'ev3Brick'";

    private final DbExecutor dbExecutor;

    public DbSetup(Session session) {
        this.dbExecutor = DbExecutor.make(session);
    }

    public void runDefaultRobertaSetup() {
        runDatabaseSetup(
            DbSetup.DB_CREATE_TABLES_SQL,
            DbSetup.SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED,
            DbSetup.SQL_RETURNING_POSITIVENUMBER_IF_SETUP_WAS_SUCCESSFUL);
    }

    public void runDatabaseSetup(String sqlResource, String sqlReturningPositiveIfSqlFileAlreadyLoaded, String sqlReturningPositiveIfSetupSuccessful) {
        try {
            this.dbExecutor.sqlFile(sqlResource, sqlReturningPositiveIfSqlFileAlreadyLoaded, sqlReturningPositiveIfSetupSuccessful);
        } catch ( Exception e ) {
            DbSetup.LOG.error("failure during execution of sql statements from classpath resource " + sqlResource, e);
        }
    }

    public void deleteAllFromUserAndProgramTmpPasswords() {
        // this shows all tables from us:
        // List<String> openRobertaTables = this.dbExecutor.select("select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = 'PUBLIC'");
        int counter = 0;
        List<String> toDelete = Arrays.asList("PROGRAM", "USER", "LOST_PASSWORD");
        for ( String openRobertaTable : toDelete ) {
            counter += this.dbExecutor.update("delete from " + openRobertaTable);
        }
        this.dbExecutor.ddl("commit");
        DbSetup.LOG.info("committed the deletion of " + counter + " rows in tables " + toDelete + ".");
        Assert.isTrue(getOneBigIntegerAsLong("select count(*) from USER_PROGRAM") == 0, "the table USER_PROGRAM should be empty");
    }

    public long getOneBigIntegerAsLong(String sqlStmt) {
        return ((BigInteger) this.dbExecutor.oneValueSelect(sqlStmt)).longValue();
    }

    @SuppressWarnings("unchecked")
    public <T> T getOne(String sqlStmt) {
        return (T) this.dbExecutor.oneValueSelect(sqlStmt);
    }
}
