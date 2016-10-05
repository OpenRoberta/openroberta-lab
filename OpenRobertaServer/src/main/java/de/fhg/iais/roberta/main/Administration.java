package de.fhg.iais.roberta.main;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.Encryption;

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
        Administration adminWork = new Administration(args);
        Administration.LOG.info("*** administrative work is started ***");
        adminWork.expectArgs(1);
        switch ( args[0] ) {
            case "createemptydb":
                adminWork.runDatabaseSetup();
                break;
            case "sql":
                adminWork.runSql();
                break;
            case "conf:xml2text":
                adminWork.confXml2text();
                break;
            case "user:encryptpasswords":
                adminWork.encryptpasswords();
                break;
            case "db:update":
                adminWork.update_db();
                break;
            default:
                Administration.LOG.error("invalid argument: " + args[0] + " - exit 4");
                System.exit(4);
                break;
        }
    }

    private void runDatabaseSetup() {
        Administration.LOG.info("*** runDatabaseSetup started ***");
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
        Administration.LOG.info("*** runsql ***");
        expectArgs(3);
        String sqlQuery = this.args[2];
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        nativeSession.beginTransaction();
        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlQuery).list();
        Administration.LOG.info("result set has " + resultSet.size() + " rows");
        for ( Object[] object : resultSet ) {
            Administration.LOG.info("  " + Arrays.toString(object));
        }
        nativeSession.getTransaction().rollback();
        nativeSession.close();
    }

    private void confXml2text() throws Exception {
        Administration.LOG.info("*** confXml2text ***");
        //        expectArgs(2);
        //        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        //        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        //        nativeSession.beginTransaction();
        //        String sqlGetQuery = "select ID, NAME, CONFIGURATION_TEXT from CONFIGURATION";
        //        String sqlUpdQuery = "update CONFIGURATION set CONFIGURATION_TEXT=:text where ID=:id";
        //        SQLQuery upd = nativeSession.createSQLQuery(sqlUpdQuery);
        //        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlGetQuery).list();
        //        Administration.LOG.info("there are " + resultSet.size() + " configurations in the data base");
        //        for ( Object[] object : resultSet ) {
        //            try {
        //                String textString = ConfigurationHelper.xmlString2textString((String) object[1], (String) object[2]);
        //                upd.setInteger("id", (Integer) object[0]);
        //                upd.setString("text", textString);
        //                int count = upd.executeUpdate();
        //                Administration.LOG.info("!!! processed configuration name: " + object[1] + ". Update count: " + count);
        //            } catch ( Exception e ) {
        //                Administration.LOG
        //                    .info("??? exception when transforming: name: " + object[1] + ", content: " + ((String) object[2]).substring(0, 50).replace("\n", " "));
        //            }
        //        }
        //        nativeSession.getTransaction().commit();
        //        nativeSession.createSQLQuery("shutdown").executeUpdate();
        //        nativeSession.close();
    }

    private void conftext2Xml() throws Exception {
        Administration.LOG.info("*** conftext2Xml ***");
        //        expectArgs(2);
        //        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        //        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        //        nativeSession.beginTransaction();
        //        String sqlGetQuery = "select ID, NAME, CONFIGURATION_TEXT from CONFIGURATION";
        //        String sqlUpdQuery = "update CONFIGURATION set CONFIGURATION_TEXT=:text where ID=:id";
        //        SQLQuery upd = nativeSession.createSQLQuery(sqlUpdQuery);
        //        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlGetQuery).list();
        //        Administration.LOG.info("there are " + resultSet.size() + " configurations in the data base");
        //        for ( Object[] object : resultSet ) {
        //            try {
        //                Option<String> textString = ConfigurationHelper.textString2xmlString((String) object[2]);
        //                upd.setInteger("id", (Integer) object[0]);
        //                upd.setString("text", textString.getVal());
        //                int count = upd.executeUpdate();
        //                Administration.LOG.info("!!! processed configuration name: " + object[1] + ". Update count: " + count);
        //            } catch ( Exception e ) {
        //                Administration.LOG
        //                    .info("??? exception when transforming: name: " + object[1] + ", content: " + ((String) object[2]).substring(0, 50).replace("\n", " "));
        //            }
        //        }
        //        nativeSession.getTransaction().commit();
        //        nativeSession.createSQLQuery("shutdown").executeUpdate();
        //        nativeSession.close();
    }

    private void encryptpasswords() throws Exception {
        Administration.LOG.info("*** encryptpasswords ***");
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        nativeSession.beginTransaction();
        String sqlGetQuery = "select ID, PASSWORD from USER";
        String sqlUpdQuery = "update USER set PASSWORD=:password where ID=:id";
        SQLQuery upd = nativeSession.createSQLQuery(sqlUpdQuery);
        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlGetQuery).list();
        Administration.LOG.info("there are " + resultSet.size() + " users in the data base");
        for ( Object[] object : resultSet ) {
            try {
                String password = Encryption.createHash((String) object[1]);
                upd.setInteger("id", (Integer) object[0]);
                upd.setString("password", password);
                int count = upd.executeUpdate();
                Administration.LOG.info("!!! processed user name: " + object[0] + ". Update count: " + count);
            } catch ( Exception e ) {
                Administration.LOG.info("??? exception when transforming user: id: " + object[0]);
            }
        }
        nativeSession.getTransaction().commit();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }

    private void update_db() throws Exception {

        // before execution this text do :
        // ALTER TABLE PUBLIC.EMPLOYEE ADD  USER_NAME varchar(255)

        //        create table LOST_PASSWORD (
        //            ID INTEGER not null generated by default as identity (start with 1),
        //            USER_ID INTEGER not null,
        //            URL_POSTFIX varchar(255),
        //            CREATED timestamp not null,
        //
        //            primary key (ID),
        //            foreign key (USER_ID) references USER(ID) ON DELETE CASCADE
        //        );

        HashMap<String, String> blockNames = new HashMap<>();
        blockNames.put("math_change", "robMath_change");
        blockNames.put("controls_forEach", "robControls_forEach");
        blockNames.put("controls_for", "robControls_for");
        blockNames.put("lists_repeat", "robLists_repeat");
        blockNames.put("lists_length", "robLists_length");
        blockNames.put("lists_isEmpty", "robLists_isEmpty");
        blockNames.put("lists_indexOf", "robLists_indexOf");
        blockNames.put("lists_getIndex", "robLists_getIndex");
        blockNames.put("lists_setIndex", "robLists_setIndex");
        blockNames.put("text_append", "robText_append");
        blockNames.put("lists_create_empty", "robLists_create_with");
        blockNames.put("elseIf", "elseif");
        blockNames.put("sim_colour_getSample", "robSensors_colour_getSample");
        blockNames.put("robGlobalvariables_declare", "robGlobalVariables_declare");
        blockNames.put("variables_declare", "robGlobalVariables_declare");
        blockNames.put("<mutation list_type=\"", "<mutation items=\"0\" list_type=\"");
        blockNames.put("<block_set>", "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\">");

        Administration.LOG.info("*** update database ***");
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        nativeSession.beginTransaction();

        String sqlGetQuery = "select ID, PROGRAM_TEXT, ROBOT_ID, NAME from PROGRAM";
        String sqlGetProgramByName = "select ID, NAME from PROGRAM where ROBOT_ID=42 and NAME=:name";
        String sqlUpdQuery = "update PROGRAM set PROGRAM_TEXT=:program where ID=:id";
        String sqlUpdRobotIdQuery = "update PROGRAM set ROBOT_ID=42, NAME=:name where ID=:id";

        SQLQuery upd = nativeSession.createSQLQuery(sqlUpdQuery);
        SQLQuery updRobotId = nativeSession.createSQLQuery(sqlUpdRobotIdQuery);

        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlGetQuery).list();

        Administration.LOG.info("there are " + resultSet.size() + " programs in the data base");

        int counter = 0;
        for ( Object[] object : resultSet ) {
            int programId = (int) object[0];
            String programText = (String) object[1];
            int robotId = (int) object[2];
            String programName = (String) object[3];

            try {
                String updatedProgram = renameBlocksInProgram(programText, blockNames);
                String newXml = xml2Ast2xml(updatedProgram);

                if ( !newXml.equals(object[1]) ) {
                    System.out.println(programId);
                    upd.setInteger("id", programId);
                    upd.setString("program", newXml);
                    int count = upd.executeUpdate();
                    Administration.LOG.info("!!! processed program id: " + programId + ". Update count: " + count);
                }

                if ( isSimulationProgram(robotId) ) {
                    List<Object[]> programs = selectEV3programByName(nativeSession, sqlGetProgramByName, programName);
                    if ( programs.size() > 0 ) {
                        programName += "-Sim";
                    }
                    updRobotId.setInteger("id", programId);
                    updRobotId.setString("name", programName);
                    int count = updRobotId.executeUpdate();
                    Administration.LOG.info("!!! processed program id: " + programId + ". Update Robot id count: " + count);

                }
            } catch ( Exception e ) {
                Administration.LOG.info("??? exception when transforming program: id: " + programId + "msg: " + e);
                Administration.LOG.info(programText);
                counter++;
            }
        }
        System.out.println(resultSet.size());
        System.out.println(counter);
        nativeSession.getTransaction().commit();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();

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

    private String replaceWord(String source, String oldWord, String newWord) {

        return source.replaceAll(oldWord, newWord);
    }

    private BlockSet astToJaxb(ArrayList<ArrayList<Phrase<Void>>> astProgram) {
        BlockSet blockSet = new BlockSet();

        Instance instance = null;
        for ( ArrayList<Phrase<Void>> tree : astProgram ) {
            for ( Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    blockSet.getInstance().add(instance);
                    instance = new Instance();
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        blockSet.getInstance().add(instance);
        return blockSet;
    }

    public String jaxbToXml(BlockSet blockSet) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

}
