package de.fhg.iais.roberta.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.util.DbExecutor;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;

/**
 * a class with a static main method, responsible for some administrative work, like<br>
 * <br>
 * - database setup<br>
 * - database backup<br>
 *
 * @author rbudde
 */
public class Administration {
    private static final Logger LOG = LoggerFactory.getLogger(Administration.class);
    private static int exitCode = 0; // in case of error, set to 12

    private final String[] args;

    /**
     * startup and shutdown of the server. See {@link Administration}. Uses the first element of the args array. This contains the URI of a property file and
     * starts either with "file:" if a path of the file system should be used or "classpath:" if the properties should be loaded as a resource from the
     * classpath. May be <code>null</code>, if the default resource from the classpath should be loaded.
     *
     * @param args first element may contain the URI of a property file.
     * @throws Exception if something goes wrong
     */
    public static void main(String[] args) throws Exception {
        try {
            Administration adminWork = new Administration(args);
            adminWork.run();
        } catch ( Exception e ) {
            LOG.error("exception in run method", e);
            exitCode = 12;
        }
        System.exit(exitCode);
    }

    /**
     * create the administration object
     */
    private Administration(String[] args) {
        this.args = args;
    }

    private void run() {
        expectArgs(1);
        String cmd = this.args[0];
        if ( "version".equals(cmd) ) {
            println(version());
            return;
        } else {
            LOG.info("*** " + cmd + " ***");
        }
        switch ( cmd ) {
            case "create-empty-db":
                createEmptyDatabase();
                break;
            case "db-backup":
                dbBackup();
                break;
            case "db-shutdown":
                dbShutdown();
                break;
            case "sql-client":
                sqlclient();
                break;
            case "sql-exec":
                sqlexec();
                break;
            // old stuff for some old problematic upgrades of the database
            case "configuration-clean-up":
                // removeUnusedConfigurations();
                break;
            case "check-xss":
                // checkAndPatchAllProgramsForXSS();
                break;
            case "conf:xml2text":
                // confXml2text();
                break;
            case "user:encryptpasswords":
                // encryptpasswords();
                break;
            default:
                Administration.LOG.error("invalid argument: " + this.args[0] + " - exit 12");
                System.exit(12);
        }
    }

    private String version() {
        Properties serverProperties = Util.loadPropertiesRecursively("classpath:/openRoberta.properties");
        return serverProperties.getProperty("openRobertaServer.version");
    }

    private void createEmptyDatabase() {
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session hibernateSession = sessionFactoryWrapper.getHibernateSession();
        DbSetup dbSetup = new DbSetup(hibernateSession);
        dbSetup.createEmptyDatabase();
        hibernateSession.getTransaction().commit();
        hibernateSession.beginTransaction();
        hibernateSession.createSQLQuery("shutdown").executeUpdate();
        hibernateSession.close();
    }

    /**
     * backup the database. Needs as arg parameter<br>
     * 1. the database URI (e.g. "jdbc:hsqldb:hsql://localhost/openroberta-db")<br>
     * 2. the base directory to store the backup into<br>
     * &nbsp;&nbsp;&nbsp;a. if the db server is running in a docker container, the path is probably "/opt/administration/dbBackup"<br>
     * &nbsp;&nbsp;&nbsp;b. otherwise the path is probably "./backup"<br>
     */
    private void dbBackup() {
        Administration.LOG.info("info: database backup makes sense in SERVER mode ONLY ***");
        expectArgs(3);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session hibernateSession = sessionFactoryWrapper.getHibernateSession();
        DbExecutor dbExecutor = DbExecutor.make(hibernateSession);

        long users = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from USER")).longValue();
        long programs = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from PROGRAM;")).longValue();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String backupFileName = this.args[2] + "/dbBackup-" + now.format(formatter) + "-u" + users + "-p" + programs + ".tgz";

        dbExecutor.ddl("BACKUP DATABASE TO '" + backupFileName + "' NOT BLOCKING;");
        LOG.info("backup succeeded for a database with " + users + " users and " + programs + " programs");

        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

    /**
     * shutdown the database. Needs the second parameter from the main args, which has to be the database URI (e.g.
     * "jdbc:hsqldb:hsql://localhost/openroberta-db")
     */
    private void dbShutdown() {
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session hibernateSession = sessionFactoryWrapper.getHibernateSession();
        DbExecutor dbExecutor = DbExecutor.make(hibernateSession);

        long users = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from USER")).longValue();
        long programs = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from PROGRAM;")).longValue();

        try {
            dbExecutor.ddl("SHUTDOWN;");
        } finally {
            LOG.info("shutdown for a database with " + users + " registered users and " + programs + " stored programs");
        }
    }

    /**
     * runs a sql client. Reads commands from a terminal and executes them.<br>
     * Needs a second parameter from the main args, which has to be the database URI (e.g. "jdbc:hsqldb:hsql://localhost/openroberta-db")
     */
    private void sqlclient() {
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session hibernateSession = sessionFactoryWrapper.getHibernateSession();
        DbExecutor dbExecutor = DbExecutor.make(hibernateSession);

        try {
            System.out.println("Enter sql commands. You may use many lines. Terminate the command by a semicolon ';'");
            System.out.println("Terminate this sql client by 'BYE' or a command that contains a semicolon ';' only");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while ( true ) {
                System.out.print("sql> ");
                String sqlStmt = dbExecutor.readSqlStmtLinesUntilSemicolon(br);
                if ( sqlStmt == null ) {
                    break;
                } else if ( true || DbExecutor.isSelect(sqlStmt) ) {
                    // execute all sql stmts. If you think this is too dangerous, remove the true above
                    try {
                        dbExecutor.sqlStmt(sqlStmt);
                    } catch ( Exception e ) {
                        LOG.error("stmt could not be executed", e);
                    }
                } else {
                    println("for safety reasons this statements is not processed");
                }
            }
        } finally {
            LOG.info("sqlclient terminates");
        }
    }

    private void sqlexec() {
        expectArgs(3);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session hibernateSession = sessionFactoryWrapper.getHibernateSession();
        String sqlQuery = this.args[2];
        if ( DbExecutor.isSelect(sqlQuery) ) {
            @SuppressWarnings("unchecked")
            List<Object[]> resultSet = hibernateSession.createSQLQuery(sqlQuery).list(); //NOSONAR : no sql injection possible here. Dangerous sql of course :-)
            LOG.info("result set has " + resultSet.size() + " rows");
            for ( Object object : resultSet ) {
                if ( object instanceof Object[] ) {
                    LOG.info(">>>  " + Arrays.toString((Object[]) object));
                } else {
                    LOG.info(">>>  " + object.toString());
                }
            }
            hibernateSession.getTransaction().rollback();
            hibernateSession.close();
        } else {
            // better not: dbExecutor.sqlStmt(sqlQuery);
            println("for safety reasons only SELECT statements is processed");
        }

    }

    @SuppressWarnings("unused")
    private void removeUnusedConfigurations() {
        SessionFactoryWrapper sessionFactory = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        DbSession session = sessionFactory.getSession();
        ConfigurationDao configurationDao = new ConfigurationDao(session);
        List<Configuration> configurationList = configurationDao.loadAll();
        LOG.info("Total configurations: " + configurationList.size());
        Set<String> hashSet = new HashSet<>();
        int unusedConfigCounter = 0;
        for ( Configuration configuration : configurationList ) {
            hashSet.add(configuration.getConfigurationHash());
        }
        LOG.info("Total unique configurations: " + hashSet.size());

        List<ConfigurationData> configurationDataList = configurationDao.loadAllConfigurationData();
        LOG.info("Total configuration data entries: " + configurationDataList.size());

        for ( ConfigurationData configurationData : configurationDataList ) {
            if ( !hashSet.contains(configurationData.getConfigurationHash()) ) {
                unusedConfigCounter += 1;
            }
        }
        LOG.info("Amount of unused configurations: " + unusedConfigCounter);
        // seek all unused configurations and destroy them
        String deleteUnusedConfigurationsSQL = "DELETE FROM ConfigurationData WHERE configurationHash NOT IN (SELECT configurationHash FROM Configuration)";
        session.createQuery(deleteUnusedConfigurationsSQL).executeUpdate();
        session.commit(); // implicitly a new transaction is started
        session.createSqlQuery("shutdown").executeUpdate();
    }

    private void expectArgs(int number) {
        if ( this.args == null || this.args.length < number ) {
            Administration.LOG.error("not enough arguments - exit 8");
            System.exit(8);
        }
    }

    private static void println(String msg) {
        System.out.println(msg);
    }
}