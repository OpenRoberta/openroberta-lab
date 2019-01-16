package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class DbSetup {

    private static final Logger LOG = LoggerFactory.getLogger(DbSetup.class);

    public static final String DB_CREATE_TABLES_WITHOUT_CONSTRAINTS_SQL = "/create-tables-without-constraints.sql";
    public static final String DB_CREATE_CONSTRAINTS_SQL = "/create-constraints.sql";
    public static final String DB_CREATE_INITROWS_SQL = "/create-initrows.sql";
    static final String SQL_RETURNING_POSITIVENUMBER_IF_SQLFILES_ALREADY_LOADED = "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PROGRAM'";
    static final String SQL_RETURNING_POSITIVENUMBER_IF_SETUP_WAS_SUCCESSFUL = "select count(*) from ROBOT";

    private final DbExecutor dbExecutor;

    public DbSetup(Session session) {
        this.dbExecutor = DbExecutor.make(session);
    }

    public void createEmptyDatabase() {
        sqlFile(
            SQL_RETURNING_POSITIVENUMBER_IF_SQLFILES_ALREADY_LOADED,
            SQL_RETURNING_POSITIVENUMBER_IF_SETUP_WAS_SUCCESSFUL, //
            DB_CREATE_TABLES_WITHOUT_CONSTRAINTS_SQL,
            DB_CREATE_CONSTRAINTS_SQL,
            DB_CREATE_INITROWS_SQL);
    }

    /**
     * execute SQL statements read from a array of files
     *
     * @param sqlReturningPositiveIfSqlFileAlreadyLoaded SQL statement returning positive number if this file has already been loaded; maybe null
     * @param sqlReturningPositiveIfSetupSuccessful SQL statement returning positive number if the load of all files was successful; maybe null
     * @param sqlResource
     */
    public void sqlFile(String sqlReturningPositiveIfSqlFileAlreadyLoaded, String sqlReturningPositiveIfSetupSuccessful, String... sqlResources) {
        try {
            int result = 0;
            if ( sqlReturningPositiveIfSqlFileAlreadyLoaded != null ) {
                result = ((BigInteger) this.dbExecutor.oneValueSelect(sqlReturningPositiveIfSqlFileAlreadyLoaded)).intValue();
            }
            if ( result == 0 ) {
                for ( String sqlResource : sqlResources ) {
                    this.dbExecutor.sqlFile(this.getClass().getResourceAsStream(sqlResource));
                }
                if ( sqlReturningPositiveIfSetupSuccessful != null ) {
                    result = ((BigInteger) this.dbExecutor.oneValueSelect(sqlReturningPositiveIfSetupSuccessful)).intValue();
                    if ( result <= 0 ) {
                        throw new DbcException("loading sql from resources " + Arrays.toString(sqlResources) + " was NOT successful");
                    }
                }
            }
        } catch ( Exception e ) {
            DbSetup.LOG.error("failure during execution of sql statements from classpath resource(s) " + Arrays.toString(sqlResources), e);
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
