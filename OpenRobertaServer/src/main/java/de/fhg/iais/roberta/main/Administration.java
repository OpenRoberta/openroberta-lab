package de.fhg.iais.roberta.main;

import java.util.Arrays;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.jaxb.ConfigurationHelper;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
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

    private void expectArgs(int number) {
        if ( this.args == null || this.args.length < number ) {
            Administration.LOG.error("not enough arguments - exit 8");
            System.exit(8);
        }
    }

}
