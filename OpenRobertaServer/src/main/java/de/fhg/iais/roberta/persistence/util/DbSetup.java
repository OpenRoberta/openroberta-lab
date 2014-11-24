package de.fhg.iais.roberta.persistence.util;

import java.math.BigInteger;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbSetup {

    private static final Logger LOG = LoggerFactory.getLogger(DbSetup.class);

    private static final String DB_CREATE_TABLES_SQL = "/myCreate-tables.sql";
    private static final String SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED =
        "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PROGRAM'";

    private final DbExecutor dbExecutor;

    public DbSetup(Session session) {
        this.dbExecutor = DbExecutor.make(session);
    }

    public void runDefaultRobertaSetup() {
        runDatabaseSetup(DB_CREATE_TABLES_SQL, SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED);
    }

    public void runDatabaseSetup(String nameOfResourceContainingSql, String sqlReturningPositiveNumberIfSqlFileAlreadyLoaded) {
        try {
            this.dbExecutor.sqlFile(nameOfResourceContainingSql, SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED);
        } catch ( Exception e ) {
            LOG.error("failure during execution of sql statements from classpath resource " + nameOfResourceContainingSql);
        }
    }

    public int getOneInt(String sqlStmt) {
        return ((BigInteger) this.dbExecutor.oneValueSelect(sqlStmt)).intValue();
    }

}
