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
import de.fhg.iais.roberta.jaxb.ConfigurationHelper;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.Encryption;
import de.fhg.iais.roberta.util.Option;

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
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        nativeSession.beginTransaction();
        String sqlGetQuery = "select ID, NAME, CONFIGURATION_TEXT from CONFIGURATION";
        String sqlUpdQuery = "update CONFIGURATION set CONFIGURATION_TEXT=:text where ID=:id";
        SQLQuery upd = nativeSession.createSQLQuery(sqlUpdQuery);
        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlGetQuery).list();
        Administration.LOG.info("there are " + resultSet.size() + " configurations in the data base");
        for ( Object[] object : resultSet ) {
            try {
                String textString = ConfigurationHelper.xmlString2textString((String) object[1], (String) object[2]);
                upd.setInteger("id", (Integer) object[0]);
                upd.setString("text", textString);
                int count = upd.executeUpdate();
                Administration.LOG.info("!!! processed configuration name: " + object[1] + ". Update count: " + count);
            } catch ( Exception e ) {
                Administration.LOG
                    .info("??? exception when transforming: name: " + object[1] + ", content: " + ((String) object[2]).substring(0, 50).replace("\n", " "));
            }
        }
        nativeSession.getTransaction().commit();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
    }

    private void conftext2Xml() throws Exception {
        Administration.LOG.info("*** conftext2Xml ***");
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        nativeSession.beginTransaction();
        String sqlGetQuery = "select ID, NAME, CONFIGURATION_TEXT from CONFIGURATION";
        String sqlUpdQuery = "update CONFIGURATION set CONFIGURATION_TEXT=:text where ID=:id";
        SQLQuery upd = nativeSession.createSQLQuery(sqlUpdQuery);
        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlGetQuery).list();
        Administration.LOG.info("there are " + resultSet.size() + " configurations in the data base");
        for ( Object[] object : resultSet ) {
            try {
                Option<String> textString = ConfigurationHelper.textString2xmlString((String) object[2]);
                upd.setInteger("id", (Integer) object[0]);
                upd.setString("text", textString.getVal());
                int count = upd.executeUpdate();
                Administration.LOG.info("!!! processed configuration name: " + object[1] + ". Update count: " + count);
            } catch ( Exception e ) {
                Administration.LOG
                    .info("??? exception when transforming: name: " + object[1] + ", content: " + ((String) object[2]).substring(0, 50).replace("\n", " "));
            }
        }
        nativeSession.getTransaction().commit();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();
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
        //        blockNames.put(
        //            "(<block type=\"robControls_start\".*deletable=\"false\">)(<\\/block>)",
        //            "$1<mutation declare=\"false\"></mutation><field name=\"DEBUG\">FALSE</field>$2");

        //        blockNames.put("(<block type=\"robSensors_.*_getSample\" id=\"\\d*\">)(<field name=\"MODE\">(PRESENCE)<\\/field>)", "$1<mutation mode=\"$3\"/>$2");
        //        blockNames.put("(<block type=\"robSensors_.*_getSample\" id=\"\\d*\">)(<field name=\"MODE\">(DISTANCE)<\\/field>)", "$1<mutation mode=\"$3\"/>$2");
        //        blockNames.put("(<block type=\"robSensors_.*_getSample\" id=\"\\d*\">)(<field name=\"MODE\">(COLOUR)<\\/field>)", "$1<mutation mode=\"$3\"/>$2");
        //        blockNames.put("(<block type=\"robSensors_.*_getSample\" id=\"\\d*\">)(<field name=\"MODE\">(RED)<\\/field>)", "$1<mutation mode=\"$3\"/>$2");
        //        blockNames.put("(<block type=\"robSensors_.*_getSample\" id=\"\\d*\">)(<field name=\"MODE\">(RGB)<\\/field>)", "$1<mutation mode=\"$3\"/>$2");
        //        blockNames.put("(<block type=\"robSensors_.*_getSample\" id=\"\\d*\">)(<field name=\"MODE\">(AMBIENTLIGHT)<\\/field>)", "$1<mutation mode=\"$3\"/>$2");
        //        blockNames.put("<field name=\"MODE\">PRESENCE</field>", "<mutation mode=\"PRESENCE\"/><field name=\"MODE\">PRESENCE</field>");
        //        blockNames.put("<field name=\"MODE\">DISTANCE</field>", "<mutation mode=\"DISTANCE\"/><field name=\"MODE\">DISTANCE</field>");
        //        blockNames.put("<field name=\"MODE\">COLOUR</field>", "<mutation mode=\"COLOUR\"/><field name=\"MODE\">COLOUR</field>");
        //        blockNames.put("<field name=\"MODE\">RED</field>", "<mutation mode=\"RED\"/><field name=\"MODE\">RED</field>");
        //        blockNames.put("<field name=\"MODE\">RGB</field>", "<mutation mode=\"RED\"/><field name=\"MODE\">RGB</field>");
        //        blockNames.put("<field name=\"MODE\">AMBIENTLIGHT</field>", "<mutation mode=\"RED\"/><field name=\"MODE\">AMBIENTLIGHT</field>");

        Administration.LOG.info("*** update database ***");
        expectArgs(2);
        SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:" + this.args[1]);
        Session nativeSession = sessionFactoryWrapper.getNativeSession();
        nativeSession.beginTransaction();
        String sqlGetQuery = "select ID, PROGRAM_TEXT from PROGRAM";
        String sqlUpdQuery = "update PROGRAM set PROGRAM_TEXT=:program where ID=:id";
        SQLQuery upd = nativeSession.createSQLQuery(sqlUpdQuery);
        List<Object[]> resultSet = nativeSession.createSQLQuery(sqlGetQuery).list();
        Administration.LOG.info("there are " + resultSet.size() + " programs in the data base");
        int counter = 0;
        for ( Object[] object : resultSet ) {
            try {
                String updatedProgram = renameBlocksInProgram((String) object[1], blockNames);
                BlockSet program = JaxbHelper.xml2BlockSet(updatedProgram);
                Jaxb2BlocklyProgramTransformer<Void> transformer = new Jaxb2BlocklyProgramTransformer<>();
                transformer.transform(program);

                BlockSet blockSet = astToJaxb(transformer.getTree());
                String newXml = jaxbToXml(blockSet);
                if ( !newXml.equals(object[1]) ) {
                    System.out.println((int) object[0]);
                    upd.setInteger("id", (Integer) object[0]);
                    upd.setString("program", newXml);
                    int count = upd.executeUpdate();
                    Administration.LOG.info("!!! processed program id: " + object[0] + ". Update count: " + count);

                }
            } catch ( Exception e ) {
                Administration.LOG.info("??? exception when transforming program: id: " + object[0] + "msg: " + e);
                Administration.LOG.info((String) object[1]);
                counter++;
            }
        }
        System.out.println(resultSet.size());
        System.out.println(counter);
        nativeSession.getTransaction().commit();
        nativeSession.createSQLQuery("shutdown").executeUpdate();
        nativeSession.close();

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
                if ( phrase.getKind() == BlockType.LOCATION ) {
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
