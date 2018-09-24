package de.fhg.iais.roberta.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.util.DbExecutor;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.persistence.util.Upgrader;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

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
     * startup and shutdown of the server. See {@link Administration}. Uses the first element of the args array. This contains the URI of a property file and
     * starts either with "file:" if a path of the file system should be used or "classpath:" if the properties should be loaded as a resource from the
     * classpath. May be <code>null</code>, if the default resource from the classpath should be loaded.
     *
     * @param args first element may contain the URI of a property file.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Administration adminWork = new Administration(args);
        adminWork.run();
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
                System.out.println(version(false));
                return;
            case "version-for-db":
                System.out.println(version(true));
                return;
            default:
                LOG.info("*** " + cmd + " ***");
        }
        switch ( cmd ) {
            case "createemptydb":
                createEmptyDatabase();
                break;
            case "dbBackup":
                dbBackup();
                break;
            case "dbShutdown":
                dbShutdown();
                break;
            case "dbCheckpoint":
                dbCheckpoint();
                break;
            case "checkXSS":
                checkAndPatchAllProgramsForXSS();
                break;
            case "sqlclient":
                sqlclient();
                break;
            case "sql":
                runSql();
                break;
            case "upgrade":
                upgrade();
                break;
            // old stuff for some old problematic upgrades of the database
            case "conf:xml2text":
                // confXml2text();
                break;
            case "user:encryptpasswords":
                // encryptpasswords();
                break;
            case "db:update":
                // update_db();
                break;
            default:
                Administration.LOG.error("invalid argument: " + this.args[0] + " - exit 4");
                System.exit(4);
        }
    }

    private void createEmptyDatabase() {
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbSetup dbSetup = new DbSetup(nativeSession);
        nativeSession.beginTransaction();
        dbSetup.createEmptyDatabase();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }

    /**
     * backup the database. Needs the second parameter from the main args, which has to be the database URI (e.g. "jdbc:hsqldb:hsql://localhost/openroberta-db")
     */
    private void dbBackup() {
        Administration.LOG.info("info: database backup makes sense in SERVER mode ONLY ***");
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        nativeSession.beginTransaction();

        long users = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from USER")).longValue();
        long programs = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from PROGRAM;")).longValue();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String backupFileName = "backup/dbBackup-" + now.format(formatter) + "-u" + users + "-p" + programs + ".tgz";

        dbExecutor.ddl("BACKUP DATABASE TO '" + backupFileName + "' BLOCKING;");
        LOG.info("backup succeeded for a database with " + users + " users and " + programs + " programs");

        nativeSession.getTransaction().commit();
        nativeSession.close();
    }

    /**
     * backup the database. Needs the second parameter from the main args, which has to be the database URI (e.g. "jdbc:hsqldb:hsql://localhost/openroberta-db")
     */
    private void dbCheckpoint() {
        expectArgs(3);
        String checkpoint = "checkpoint" + ("-d".equals(this.args[1]) ? " defrag" : "");
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[2]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(nativeSession);
        nativeSession.beginTransaction();

        long users = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from USER")).longValue();
        long programs = ((BigInteger) dbExecutor.oneValueSelect("select count(*) from PROGRAM;")).longValue();

        dbExecutor.ddl(checkpoint);
        LOG.info(checkpoint + " succeeded for a database with " + users + " users and " + programs + " programs");

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
            dbExecutor.ddl("SHUTDOWN COMPACT;");
        } finally {
            LOG.info("shutdown compact for a database with " + users + " registered users and " + programs + " stored programs");
        }
    }

    /**
     * upgrade the database. Needs as parameter from the main args the database parent directory<br>
     * Accesses the database in embedded mode!
     */
    private void upgrade() {
        expectArgs(2);
        String versionForDb = version(true);
        Upgrader.checkForUpgrade(versionForDb, new File(this.args[1]));
    }

    private void runSql() {
        expectArgs(3);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        String sqlQuery = this.args[2];
        if ( DbExecutor.isSelect(sqlQuery) ) {
            nativeSession.beginTransaction();
            @SuppressWarnings("unchecked")
            List<Object[]> resultSet = nativeSession.createSQLQuery(sqlQuery).list(); //NOSONAR : no sql injection possible here. Dangerous sql of course :-)
            Administration.LOG.info("result set has " + resultSet.size() + " rows");
            for ( Object object : resultSet ) {
                if ( object instanceof Object[] ) {
                    Administration.LOG.info(">>>  " + Arrays.toString((Object[]) object));
                } else {
                    Administration.LOG.info(">>>  " + object.toString());
                }
            }
            nativeSession.getTransaction().rollback();
            nativeSession.close();
        } else {
            // better not: dbExecutor.sqlStmt(sqlQuery);
            System.out.println("for safety reasons only a SELECT statements is processed");
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
                            System.out.println(Arrays.toString((Object[]) result));
                        } else {
                            System.out.println(result);
                        }
                    }
                } else {
                    // better not: dbExecutor.sqlStmt(sqlStmt);
                    System.out.println("for safety reasons only SELECT statements are processed");
                }
            }
        } catch ( IOException e ) {
            // termination is OK
        } finally {
            LOG.info("sqlclient terminates");
        }
    }

    private String version(boolean isForDatabase) {
        Properties robertaProperties = Util1.loadProperties(false, null);
        String version = robertaProperties.getProperty("openRobertaServer.version");
        if ( isForDatabase ) {
            return version.replace("-SNAPSHOT", "");
        } else {
            return version;
        }
    }

    private String xml2Ast2xml(String updatedProgram) throws Exception, JAXBException {
        BlockSet program = JaxbHelper.xml2BlockSet(updatedProgram);
        //        EV3Factory modeFactory = new EV3Factory(null);
        Jaxb2BlocklyProgramTransformer<Void> transformer = new Jaxb2BlocklyProgramTransformer<>(null);
        transformer.transform(program);
        BlockSet blockSet = astToJaxb(transformer.getTree());
        String newXml = jaxbToXml(blockSet);
        return newXml;
    }

    private List<Object[]> selectEV3programByName(Session nativeSession, String sqlGetProgramByName, String name) {
        SQLQuery selectByProgramName = nativeSession.createSQLQuery(sqlGetProgramByName);
        selectByProgramName.setString("name", name);
        @SuppressWarnings("unchecked")
        List<Object[]> result = selectByProgramName.list();
        return result;
    }

    private boolean isSimulationProgram(int robotId) {
        return robotId == 43;
    }

    private void expectArgs(int number) {
        if ( this.args == null || this.args.length < number ) {
            Administration.LOG.error("not enough arguments - exit 8");
            System.exit(8);
        }
    }

    private String renameBlocksInProgram(String program, Map<String, String> blockNames) {
        for ( Entry<String, String> entry : blockNames.entrySet() ) {
            program = replaceWord(program, entry.getKey(), entry.getValue());
        }
        return program;
    }

    /*
     * This method loads _all_ programs from the database (potentially slow or takes too much memory?)
     * using the unchecked version of the getter and then saves them back with the setter that checks
     * for XSS. That setter will also print the relevant information about the author.
     */
    public void checkAndPatchAllProgramsForXSS() {
        SessionFactoryWrapper sessionFactory = new SessionFactoryWrapper("hibernate-cfg.xml", this.args[1]);
        DbSession session = sessionFactory.getSession();
        ProgramDao programDao = new ProgramDao(session);
        List<Program> programList = programDao.loadAll();
        for ( Program program : programList ) {
            program.setProgramText(program.getUncheckedProgramText());
        }
    }

    private String replaceWord(String source, String oldWord, String newWord) {

        return source.replaceAll(oldWord, newWord);
    }

    private BlockSet astToJaxb(ArrayList<ArrayList<Phrase<Void>>> astProgram) {
        BlockSet blockSet = new BlockSet();

        Instance instance = new Instance();
        for ( ArrayList<Phrase<Void>> tree : astProgram ) {
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
}