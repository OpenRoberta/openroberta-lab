package de.fhg.iais.roberta.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.util.DbExecutor;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

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
     * @throws Exception
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
        switch ( cmd ) {
            case "version":
                println(version());
                return;
            default:
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
        Properties serverProperties = Util.loadProperties(null);
        String version = serverProperties.getProperty("openRobertaServer.version");
        return version;
    }

    private void createEmptyDatabase() {
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbSetup dbSetup = new DbSetup(nativeSession);
        nativeSession.beginTransaction();
        dbSetup.createEmptyDatabase();
        nativeSession.getTransaction().commit();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
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
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        nativeSession.beginTransaction();

        long users = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from USER")).longValue();
        long programs = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from PROGRAM;")).longValue();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String backupFileName = this.args[2] + "/dbBackup-" + now.format(formatter) + "-u" + users + "-p" + programs + ".tgz";

        dbExecutor.ddl("BACKUP DATABASE TO '" + backupFileName + "' NOT BLOCKING;");
        LOG.info("backup succeeded for a database with " + users + " users and " + programs + " programs");

        nativeSession.getTransaction().commit();
        nativeSession.close();
    }

    /**
     * shutdown the database. Needs the second parameter from the main args, which has to be the database URI (e.g.
     * "jdbc:hsqldb:hsql://localhost/openroberta-db")
     */
    private void dbShutdown() {
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        nativeSession.beginTransaction();

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
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        nativeSession.beginTransaction();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while ( true ) {
                System.out.print("Enter sql command: ");
                String line = br.readLine();
                if ( line == null ) {
                    break;
                }
                String sqlStmt = line.trim();
                if ( "".equals(sqlStmt) ) {
                    break;
                }
                if ( DbExecutor.isSelect(sqlStmt) ) {
                    List<Object> resultset = dbExecutor.select(sqlStmt);
                    for ( Object result : resultset ) {
                        if ( result instanceof Object[] ) {
                            println(Arrays.toString((Object[]) result));
                        } else if ( result == null ) {
                            println(null);
                        } else {
                            println(result.toString());
                        }
                    }
                } else {
                    // better execute NOT: dbExecutor.sqlStmt(sqlStmt);
                    println("for safety reasons only SELECT statements are processed");
                }
            }
        } catch ( IOException e ) {
            // termination is OK, it's an sql client
        } finally {
            LOG.info("sqlclient terminates");
        }
    }

    private void sqlexec() {
        expectArgs(3);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        String sqlQuery = this.args[2];
        if ( DbExecutor.isSelect(sqlQuery) ) {
            nativeSession.beginTransaction();
            @SuppressWarnings("unchecked")
            List<Object[]> resultSet = nativeSession.createSQLQuery(sqlQuery).list(); //NOSONAR : no sql injection possible here. Dangerous sql of course :-)
            LOG.info("result set has " + resultSet.size() + " rows");
            for ( Object object : resultSet ) {
                if ( object instanceof Object[] ) {
                    LOG.info(">>>  " + Arrays.toString((Object[]) object));
                } else {
                    LOG.info(">>>  " + object.toString());
                }
            }
            nativeSession.getTransaction().rollback();
            nativeSession.close();
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
        session.commit();
        session.createSqlQuery("shutdown").executeUpdate();
    }

    /*
     * This method loads _all_ programs from the database (potentially slow or takes too much memory?)
     * using the unchecked version of the getter and then saves them back with the setter that checks
     * for XSS. That setter will also print the relevant information about the author.
     */
    @SuppressWarnings("unused")
    private void checkAndPatchAllProgramsForXSS() {
        SessionFactoryWrapper sessionFactory = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        DbSession session = sessionFactory.getSession();
        ProgramDao programDao = new ProgramDao(session);
        List<Program> programList = programDao.loadAll();
        int totalProcessed = 0;
        int totalDescriptions = 0;
        int totalDifferent = 0;
        for ( Program program : programList ) {
            String uncheckedProgramText = program.getUncheckedProgramText();
            totalProcessed += 1;
            if ( uncheckedProgramText == null || uncheckedProgramText.equals("") ) {
                continue;
            }
            totalDescriptions += 1;
            String checkedProgramText = UtilForHtmlXml.checkProgramTextForXSS(uncheckedProgramText);
            if ( !checkedProgramText.equals(uncheckedProgramText) ) {
                totalDifferent += 1;
                try {
                    program.setProgramText(checkedProgramText);
                } catch ( NullPointerException e ) {
                    LOG.error("Program text is empty!", program.getName());
                }
            }
        }
        LOG.info("Total programs processed: " + totalProcessed);
        LOG.info("Total programs descriptions: " + totalDescriptions);
        LOG.info("Total programs different: " + totalDifferent);
        session.commit();
        session.createSqlQuery("shutdown").executeUpdate();
    }

    // -------------------- helper ---------------------------------------------------------------------------------

    @SuppressWarnings("unused")
    private String xml2Ast2xml(String updatedProgram) throws Exception, JAXBException {
        BlockSet program = JaxbHelper.xml2BlockSet(updatedProgram);
        Jaxb2ProgramAst<Void> transformer = new Jaxb2ProgramAst<>(null);
        BlockSet blockSet = astToJaxb(transformer.blocks2Ast(program).getTree());
        return jaxbToXml(blockSet);
    }

    @SuppressWarnings("unused")
    private List<Object[]> selectEV3programByName(Session nativeSession, String sqlGetProgramByName, String name) {
        SQLQuery selectByProgramName = nativeSession.createSQLQuery(sqlGetProgramByName);
        selectByProgramName.setString("name", name);
        @SuppressWarnings("unchecked")
        List<Object[]> result = selectByProgramName.list();
        return result;
    }

    @SuppressWarnings("unused")
    private boolean isSimulationProgram(int robotId) {
        return robotId == 43;
    }

    @SuppressWarnings("unused")
    private String renameBlocksInProgram(String program, Map<String, String> blockNames) {
        for ( Entry<String, String> entry : blockNames.entrySet() ) {
            program = replaceWord(program, entry.getKey(), entry.getValue());
        }
        return program;
    }

    private void expectArgs(int number) {
        if ( this.args == null || this.args.length < number ) {
            Administration.LOG.error("not enough arguments - exit 8");
            System.exit(8);
        }
    }

    private String replaceWord(String source, String oldWord, String newWord) {
        return source.replaceAll(oldWord, newWord);
    }

    private BlockSet astToJaxb(List<List<Phrase<Void>>> astProgram) {
        BlockSet blockSet = new BlockSet();

        Instance instance = new Instance();
        for ( List<Phrase<Void>> tree : astProgram ) {
            for ( Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    blockSet.getInstance().add(instance);
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        blockSet.getInstance().add(instance);
        return blockSet;
    }

    private String jaxbToXml(BlockSet blockSet) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

    private static void println(String msg) {
        System.out.println(msg);
    }
}