package de.fhg.iais.roberta.javaServer.basics;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.LostPassword;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.LostPasswordDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.dbc.Assert;

public class PersistLostPasswordTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private DbSession hSession;
    private UserDao userDao;
    private LostPasswordDao lostPasswordDao;

    private static final int TOTAL_USERS = 5;

    @Before
    public void setup() throws Exception {
        TestConfiguration tc = TestConfiguration.setup();
        this.sessionFactoryWrapper = tc.getSessionFactoryWrapper();
        this.memoryDbSetup = tc.getMemoryDbSetup();

        this.hSession = this.sessionFactoryWrapper.getSession();
        this.userDao = new UserDao(this.hSession);
        this.lostPasswordDao = new LostPasswordDao(this.hSession);

        //Create list of users
        for ( int userNumber = 0; userNumber < PersistLostPasswordTest.TOTAL_USERS; userNumber++ ) {
            User user = this.userDao.loadUser(null, "account-" + userNumber);
            if ( user == null ) {
                User user2 = new User(null, "account-" + userNumber);
                user2.setEmail("stuff-" + userNumber);
                user2.setPassword("pass-" + userNumber);
                user2.setRole(Role.STUDENT);
                user2.setTags("rwth");
                this.hSession.save(user2);
                this.hSession.commit();
            }
        }
    }

    @After
    public void tearDown() {
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
    }

    @Test
    public void createUrls() throws Exception {
        List<User> userList = this.userDao.loadUserList("created", 0, "rwth");
        Assert.isTrue(userList.size() == 5);

        for ( int userNumber = 0; userNumber < PersistLostPasswordTest.TOTAL_USERS; userNumber++ ) {
            User user = this.userDao.loadUser(null, "account-" + userNumber);
            LostPassword lostPassword = this.lostPasswordDao.loadLostPassword(user.getId());
            if ( lostPassword == null ) {
                LostPassword lostPassword2 = new LostPassword(user.getId());
                this.hSession.save(lostPassword2);
                this.hSession.commit();
            }
        }
        Assert.notNull(this.lostPasswordDao.get(2));
        Assert.isNull(this.lostPasswordDao.get(8));
    }

    @Test
    public void resetPassword() throws Exception {
        User user2 = this.userDao.loadUserByEmail("stuff-2");
        Assert.notNull(user2);
        Assert.isTrue(user2.getAccount().equals("account-2"));
        LostPassword lostPassword = this.lostPasswordDao.persistLostPassword(user2.getId());
        Assert.notNull(lostPassword);
        this.hSession.save(lostPassword);
        this.hSession.commit();

        User user = this.userDao.get(lostPassword.getUserID());
        if ( user != null ) {
            user.setPassword("1");
            this.hSession.save(user);
            this.hSession.commit();
        }
        User userChanged = this.userDao.loadUser(null, "account-2");
        Assert.isTrue(userChanged.isPasswordCorrect("1"));
    }

}
