package de.fhg.iais.roberta.javaServer.basics;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.TmpPassword;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.TmpPasswordDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;

public class PersistTmpPasswordTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private String connectionUrl;

    private static final int TOTAL_USERS = 5;

    @Before
    public void setup() throws Exception {
        Properties properties = Util.loadProperties("classpath:persistTmpPasswordTest.properties");
        this.connectionUrl = properties.getProperty("hibernate.connection.url");
        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();
    }

    @Test
    public void test() throws Exception {
        DbSession hSession = this.sessionFactoryWrapper.getSession();
        UserDao userDao = new UserDao(hSession);
        TmpPasswordDao tmpPasswordDao = new TmpPasswordDao(hSession);

        //Create list of users
        for ( int userNumber = 0; userNumber < PersistTmpPasswordTest.TOTAL_USERS; userNumber++ ) {
            User user = userDao.loadUser("account-" + userNumber);
            if ( user == null ) {
                User user2 = new User("account-" + userNumber);
                user2.setEmail("stuff");
                user2.setPassword("pass-" + userNumber);
                user2.setRole(Role.STUDENT);
                user2.setTags("rwth");
                hSession.save(user2);
                hSession.commit();
            }
        }
        List<User> userList = userDao.loadUserList("created", 0, "rwth");
        System.out.println(userList.size());
        Assert.assertTrue(userList.size() == 5);

        for ( int userNumber = 0; userNumber < PersistTmpPasswordTest.TOTAL_USERS; userNumber++ ) {
            User user = userDao.loadUser("account-" + userNumber);
            TmpPassword tmpPassword = tmpPasswordDao.loadTmpPassword(user.getId());
            if ( tmpPassword == null ) {
                TmpPassword tmpPassword2 = new TmpPassword(user.getId());
                hSession.save(tmpPassword2);
                hSession.commit();
            }
        }

        Assert.assertTrue(tmpPasswordDao.get(2) != null);
        Assert.assertTrue(tmpPasswordDao.get(8) == null);
    }

    @After
    public void tearDown() {
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
    }

}
