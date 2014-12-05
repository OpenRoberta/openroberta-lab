package de.fhg.iais.roberta.main;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

/**
 * a class with a static main method, responsible for some administrative work, like<br>
 * <br>
 * - database setup<br>
 *
 * @author rbudde
 */
public class Administration {
    private static final Logger LOG = LoggerFactory.getLogger(Administration.class);

    private final String[] args;

    /**
     * create the administration object
     */
    public Administration(String[] args) {
        this.args = args;
    }

    /**
     * startup and shutdown of the server. See {@link Administration}. Uses the first element of the args array. This contains the URI of a property file and
     * starts either with "file:" if a path of the file system should be used or "classpath:" if the properties should be loaded as a resource from the
     * classpath. May be <code>null</code>, if the default resource from the classpath should be loaded.
     *
     * @param args first element may contain the URI of a property file.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Administration main = new Administration(args);
        LOG.info("*** administrative work is started ***");
        main.expectArgs(1);
        switch ( args[0] ) {
            case "createemptydb":
                main.runDatabaseSetup();
                break;
            case "sql":
                main.runSql();
                break;
            default:
                LOG.error("invalid argument: " + args[0] + " - exit 4");
                System.exit(4);
                break;
        }
    }

    private void runDatabaseSetup() {
        LOG.info("*** runDatabaseSetup started ***");
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbSetup dbSetup = new DbSetup(nativeSession);
        nativeSession.beginTransaction();
        dbSetup.runDefaultRobertaSetup();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }

    private void runSql() {
        LOG.info("*** runsql ***");
        expectArgs(3);
        String sqlQuery = this.args[2];
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        nativeSession.beginTransaction();
        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlQuery).list();
        LOG.info("result set has " + resultSet.size() + " rows");
        for ( Object[] object : resultSet ) {
            LOG.info("  " + Arrays.toString(object));
        }
        nativeSession.getTransaction().rollback();
        nativeSession.close();
    }

    private void expectArgs(int number) {
        if ( this.args == null || this.args.length < 1 ) {
            LOG.error("not enough arguments - exit 8");
            System.exit(8);
        }
    }

}
