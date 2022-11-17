package de.fhg.iais.roberta.main;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForXmlTransformation;
import de.fhg.iais.roberta.util.XsltTransformer;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

/**
 * <b>transform old xml to new (for mbed-plugins)</b><br>
 * <br>
 * We need access to a hsqldb target server, which -for testing- can be started by<br>
 * <code>java -Xmx8G -cp [[path-to-hsqldb.jar]] org.hsqldb.Server --database.0 file:[[db-directory]]/openroberta-db --dbname.0 openroberta-db &</code><br>
 * This server will later SELECT data and UPDATE rows after modifications.<br>
 * BE CAREFUL <i>NOT</i> TO SHUTDOWN A DATABASE IN USE (e.g. by the public OpenRobertaLab server!)<br>
 * Do not forget to inspect the changes in the database by running a GUI-tool (e.g. by using <code>./admin.sh ... sql-gui</code>)
 *
 * @author rbudde
 */
public class DatabaseTransformer {
    private static final String USAGE = "usage: java -cp OpenRobertaServer/target/resources/\\* de.fhg.iais.roberta.main.DatabaseEnhancement DBCONNECTION ROBOT_ID PLUGIN_NAME";
    private static final String DB_DRIVER = "org.hsqldb.jdbcDriver";
    private static Pattern BUTTON_PAT = Pattern.compile("<field name=\"KEY\">BUTTON_");

    static {
        AstFactory.loadBlocks();
    }

    // select the programs to transform, add for testing e.g.: fetch first 300 rows only
    private static final String SELECT_PROGS_PRE = "select ID, NAME, OWNER_ID, AUTHOR_ID, ROBOT_ID from PROGRAM where ROBOT_ID = ";
    private static final String SELECT_PROGS_SUF = " and not PROGRAM_TEXT like '%xmlversion=\"3.1\"%' fetch first 300 rows only";
    private static final int COMMIT_LIMIT = 100;

    private final String dbConnectionUrl;
    private final int robotId;
    private final String pluginName;

    private SessionFactoryWrapper sessionFactoryWrapper = null;
    private DbSession dbSession;
    private final XsltTransformer xsltTransformer = new XsltTransformer();
    private RobotFactory robotFactory;
    private List<Object[]> progDescrArrayList;

    int successfulTransformations = 0;
    int errorsWhenTransforming = 0;

    public static void main(String[] args) throws Exception {
        p(USAGE);
        System.exit(12);
        args = new String[3];
        args[0] = "jdbc:hsqldb:hsql://localhost/openroberta-db";
        args[1] = "193";
        args[2] = "calliope2017NoBlue";

        if ( args == null || args.length != 3 ) {
            p("invalid parameter. Exit 12");
            System.exit(12);
        }
        @SuppressWarnings("unused")
        DatabaseTransformer enhance = new DatabaseTransformer(args[0], args[1], args[2]);
        // enhance.runFromDb();
    }

    private DatabaseTransformer(String dbConnectionUrl, String robotIdAsString, String pluginName) {
        this.dbConnectionUrl = dbConnectionUrl;
        this.robotId = Integer.parseInt(robotIdAsString);
        this.pluginName = pluginName;
    }

    private void runFromDb() {

        try {
            p("connecting to the database");
            this.sessionFactoryWrapper = new SessionFactoryWrapper("/hibernate-cfg.xml", this.dbConnectionUrl);
            this.dbSession = sessionFactoryWrapper.getSession();
            p("configuring the robot factory");
            robotFactory = Util.configureRobotPlugin(pluginName, ".", ".", Collections.emptyList());

            p("selecting the programs to transform");
            Session hibernateSession = this.sessionFactoryWrapper.getHibernateSession();
            NativeQuery select = hibernateSession.createSQLQuery(SELECT_PROGS_PRE + robotId + SELECT_PROGS_SUF); // no injection possible
            progDescrArrayList = select.list();
            hibernateSession.close();

            p("processing the programs");
        } catch ( Exception e ) {
            p("exception when connecting to the database");
            e.printStackTrace(System.out);
            System.exit(12);
        }

        for ( Object[] progDescrArray : progDescrArrayList ) {
            int id = (int) progDescrArray[0];
            String name = (String) progDescrArray[1];
            int ownerId = (int) progDescrArray[2];
            int authorId = (int) progDescrArray[3];
            int robotId = (int) progDescrArray[4];
            try {
                UserDao userDao = new UserDao(dbSession);
                RobotDao robotDao = new RobotDao(dbSession);
                ProgramDao programDao = new ProgramDao(dbSession);
                ConfigurationDao configurationDao = new ConfigurationDao(dbSession);
                User owner = userDao.get(ownerId);
                User author = userDao.get(authorId);
                Robot robot = robotDao.get(robotId);
                Program program = programDao.load(name, owner, robot, author);
                String programText = program.getProgramText();
                programText = BUTTON_PAT.matcher(programText).replaceAll("<field name=\"KEY\">");
                String configurationText = getConfigurationFromProgram(dbSession, program);
                String transformedProgramText = xsltTransformer.transform(programText);
                Pair<String, String> progConfPair = UtilForXmlTransformation.transformBetweenVersions(robotFactory, transformedProgramText, configurationText);
                program.setProgramText(progConfPair.getFirst());
                String transformedConfiguration = progConfPair.getSecond();
                if ( program.getConfigName() == null & program.getConfigHash() == null ) {
                    p("id: " + id + ", name: " + name + ", owner_id: " + ownerId + " config:default");
                    // default config. Do nothing
                } else if ( program.getConfigName() != null ) {
                    configurationDao.persistConfigurationText(program.getConfigName(), owner, robot, transformedConfiguration, true);
                    p("id: " + id + ", name: " + name + ", owner_id: " + ownerId + " config:named");
                } else if ( program.getConfigHash() != null ) {
                    String newConfigurationHash = configurationDao.persistConfigurationHash(transformedConfiguration);
                    program.setConfigData(null, newConfigurationHash);
                    p("id: " + id + ", name: " + name + ", owner_id: " + ownerId + " config:anom");
                }
                if ( successfulTransformations++ % COMMIT_LIMIT == 0 ) {
                    this.dbSession.close();
                    this.dbSession = sessionFactoryWrapper.getSession();
                    p("successful: " + successfulTransformations + ", errors: " + errorsWhenTransforming);
                }
            } catch ( Exception e ) {
                errorsWhenTransforming++;
                p("id: " + id + ", name: " + name + ", owner_id: " + ownerId + " exception:  " + e.getMessage());
            }
        }
        this.dbSession.close();
        p("TERMINATION. successful: " + successfulTransformations + ", errors: " + errorsWhenTransforming);

    }

    private void runFromFile() {
        try {
            p("configuring the robot factory");
            robotFactory = Util.configureRobotPlugin(pluginName, ".", ".", Collections.emptyList());

            p("setting the program to transform");
            String xmlText = Util.readFileContent("D:/tmp/transformation/v20-export.xml");
            String transformedXml = xsltTransformer.transform(xmlText);
            Export jaxbImportExport = JaxbHelper.xml2Element(transformedXml, Export.class);
            String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
            String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
            if ( robotType1.equals(robotFactory.getGroup()) && robotType2.equals(robotFactory.getGroup()) ) {
                Pair<String, String> progConfPair =
                    UtilForXmlTransformation.transformBetweenVersions(
                        robotFactory,
                        JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet()),
                        JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet()));
                String program = progConfPair.getFirst();
                String conf = progConfPair.getSecond();
                p("ok");
            } else {
                throw new DbcException("robot name invalid");
            }
            p("successful transformation");
        } catch ( Exception e ) {
            p("exception during transformation");
            e.printStackTrace(System.out);
        }
    }

    private String getConfigurationFromProgram(DbSession dbSession, Program program) {
        ConfigurationDao configDao = new ConfigurationDao(dbSession);
        String configName = program.getConfigName();
        String configHash = program.getConfigHash();
        if ( configName != null ) {
            Configuration config = configDao.load(configName, program.getOwner(), program.getRobot());
            if ( config == null ) {
                throw new DbcException("config name invalid");
            } else {
                ConfigurationData configData = configDao.load(config.getConfigurationHash());
                if ( configData == null ) {
                    throw new DbcException("config data invalid");
                } else {
                    return configData.getConfigurationText();
                }
            }
        } else if ( configHash != null ) {
            ConfigurationData configData = configDao.load(configHash);
            if ( configData == null ) {
                throw new DbcException("config hash invalid");
            } else {
                return configData.getConfigurationText();
            }
        } else {
            return null; // null to indicate, that the default configuration has to be used.
        }
    }

    private static void p(String msg) {
        System.out.println(msg);
    }
}
