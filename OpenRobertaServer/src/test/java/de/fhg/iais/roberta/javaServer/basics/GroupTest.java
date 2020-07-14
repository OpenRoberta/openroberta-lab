package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.dao.GroupWorkflow;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupDao;
import de.fhg.iais.roberta.persistence.util.DbExecutor;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;

@Ignore
public class GroupTest {
    private static final Logger LOG = LoggerFactory.getLogger(GroupTest.class);
    private static SessionFactoryWrapper sessionFactoryWrapper;
    private static Random RANDOM = new Random();
    private static final String CHARSET_FOR_RANDOM_STRING = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
    private static final int CHARSET_FOR_RANDOM_STRING_LENGTH = CHARSET_FOR_RANDOM_STRING.length();

    @BeforeClass
    public static void setup() {
        sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", "jdbc:hsqldb:file:./db-xxx/openroberta-db");
    }

    @AfterClass
    public static void tearDown() {
        LOG.info("***** show some data in the db");
        Session session = sessionFactoryWrapper.getNativeSession();
        DbExecutor dbExecutor = DbExecutor.make(session);
        dbExecutor.sqlStmt("select * from USER");
        dbExecutor.sqlStmt("select * from USERGROUP");

        LOG.info("***** shutdown the db");
        session.createSQLQuery("shutdown").executeUpdate();
    }

    @Test
    public void testGroup() {
        {
            LOG.info("***** step 1: set activated = false");
            final DbSession session = sessionFactoryWrapper.getSession();
            UserDao userDao = new UserDao(session);
            User testUser = userDao.loadUser(null, "testUser");
            assertNotNull(testUser);
            testUser.setActivated(false);
            session.close();
        }
        {
            LOG.info("***** step 2: show that activated = false");
            Session session = sessionFactoryWrapper.getNativeSession();
            DbExecutor dbExecutor = DbExecutor.make(session);
            dbExecutor.sqlStmt("select ACCOUNT, ACTIVATED from USER where ACCOUNT = 'testUser'");
            session.close();
        }
        try {
            LOG.info("***** step 3: with activated = false create group must fail");
            final DbSession session = sessionFactoryWrapper.getSession();
            UserDao userDao = new UserDao(session);
            User testUser = userDao.loadUser(null, "testUser");
            assertNotNull(testUser);
            testUser.setActivated(false);
            session.commit();
            UserGroupDao groupDao = new UserGroupDao(session);
            groupDao.persistGroup("minscha", testUser, null);
            fail();
        } catch ( Exception e ) {
            LOG.info("group create failed, that is OK here");
        }
        {
            LOG.info("***** step 4: set activated = true");
            final DbSession session = sessionFactoryWrapper.getSession();
            UserDao userDao = new UserDao(session);
            User testUser = userDao.loadUser(null, "testUser");
            assertNotNull(testUser);
            testUser.setActivated(true);
            session.commit();
            session.close();
        }
        {
            LOG.info("***** step 5: show that activated = true");
            Session session = sessionFactoryWrapper.getNativeSession();
            DbExecutor dbExecutor = DbExecutor.make(session);
            dbExecutor.sqlStmt("select ACCOUNT, ACTIVATED from USER where ACCOUNT = 'testUser'");
            session.close();
        }
        {
            LOG.info("***** step 6: create a group, modify its name until a new name is found :-)");
            final DbSession session = sessionFactoryWrapper.getSession();
            UserDao userDao = new UserDao(session);
            User testUser = userDao.loadUser(null, "testUser");
            assertNotNull(testUser);
            UserGroupDao groupDao = new UserGroupDao(session);
            int number = 0;
            while ( true ) {
                Pair<Key, UserGroup> result = groupDao.persistGroup("minscha" + number++, testUser, null);
                Key resultKey = result.getFirst();
                if ( resultKey == Key.GROUP_CREATE_SUCCESS ) {
                    break;
                } else if ( resultKey == Key.GROUP_CREATE_ERROR_GROUP_ALREADY_EXISTS ) {
                    // expected!
                } else {
                    fail();
                }
            }
            session.commit();
            session.close();
        }
        {
            LOG.info("***** step 7: change access right");
            final DbSession session = sessionFactoryWrapper.getSession();
            GroupWorkflow groupWorkflow = new GroupWorkflow(session);
            groupWorkflow.changeGroupAccessRight("testUser", "minscha0", AccessRight.ADMIN_READ);
            session.commit();
            session.close();
        }

        {
            String newUser = "testUser-" + getAlphaNumericString(6) + "-";

            {
                LOG.info("***** step 8.a: add group accounts");
                final DbSession session = sessionFactoryWrapper.getSession();
                final GroupWorkflow groupWorkflow = new GroupWorkflow(session);
                groupWorkflow.addAccounts("testUser", "minscha0", newUser, 5, 5);
                session.commit();
                session.close();
            }
            {
                LOG.info("***** step 8.b: delete group account");
                //TODO: add program to student account, delete student, check if the program is deleted
                final DbSession session = sessionFactoryWrapper.getSession();
                final GroupWorkflow groupWorkflow = new GroupWorkflow(session);
                groupWorkflow.deleteAccount("testUser", "minscha0", newUser + "8");
                session.commit();
                session.close();
            }
        }
        {
            LOG.info("***** step 9: rename a group");
            final DbSession session = sessionFactoryWrapper.getSession();
            UserDao userDao = new UserDao(session);
            User testUser = userDao.loadUser(null, "testUser");
            assertNotNull(testUser);
            UserGroupDao groupDao = new UserGroupDao(session);
            List<UserGroup> groups = groupDao.loadAll(testUser);
            UserGroup group = groups.get(0);
            String newName = group.getName() + getAlphaNumericString(6);
            group.rename(newName);
            session.commit();
            session.close();
        }

    }

    /**
     * generate a random string of length n
     *
     * @param n the length of the string
     * @return the random string
     */
    private String getAlphaNumericString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for ( int i = 0; i < n; i++ ) {
            sb.append(CHARSET_FOR_RANDOM_STRING.charAt(RANDOM.nextInt(CHARSET_FOR_RANDOM_STRING_LENGTH)));
        }
        return sb.toString();
    }
}
