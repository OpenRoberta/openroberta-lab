package de.fhg.iais.roberta.testutil;

import java.math.BigInteger;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryDbSetup {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryDbSetup.class);

    private static final String DB_CREATE_TABLES_SQL = "./db/create-tables.sql";
    private static final String SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED =
        "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PROGRAM'";

    private final DbExecutor dbExecutor;

    public InMemoryDbSetup(Session session) {
        this.dbExecutor = DbExecutor.make(session);
    }

    public void runRobertaSetup() {
        run(DB_CREATE_TABLES_SQL, SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED);
    }

    public void run(String pathToFileContainingSql, String sqlReturningPositiveNumberIfSqlFileAlreadyLoaded) {
        try {
            this.dbExecutor.sqlFile(pathToFileContainingSql, SQL_RETURNING_POSITIVENUMBER_IF_SQLFILE_ALREADY_LOADED);
        } catch ( Exception e ) {
            LOG.error("failure during execution of sql statements from file " + pathToFileContainingSql);
        }
    }

    public int getOneInt(String sqlStmt) {
        return ((BigInteger) this.dbExecutor.oneValueSelect(sqlStmt)).intValue();
    }

}
