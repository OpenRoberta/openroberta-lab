package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserProgram;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserProgramDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;

@Ignore
public class BasicPersistUserProgram {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private String connectionUrl;

    private static final int TOTAL_USERS = 100;

    @Before
    public void setup() throws Exception {
        Properties properties = Util.loadProperties("classpath:openRoberta-basicPersistUserProgram.properties");
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
        ProgramDao programDao = new ProgramDao(hSession);
        assertEquals(0, getOneInt("select count(*) from USER_PROGRAM"));

        //Create list of users
        for ( int userNumber = 0; userNumber < TOTAL_USERS; userNumber++ ) {
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
        assertTrue(userList.size() == 10);

        //Create one program per user
        for ( int userNumber = 0; userNumber < TOTAL_USERS; userNumber++ ) {
            User owner = userDao.loadUser("account-" + userNumber);
            Program program = programDao.load("program-" + userNumber, owner);
            if ( program == null ) {
                Program program2 = new Program("program-" + userNumber, owner);
                String text = "<program>...</program>";
                program2.setProgramText(text);
                hSession.save(program2);
                hSession.commit();
            }
        }
        List<Program> programList = programDao.loadAll();
        assertTrue(programList.size() == 100);

        //User 0 invites all inpair  users to write to its program
        User owner = userDao.loadUser("account-0");
        Program program = programDao.load("program-0", owner);
        UserProgramDao userProgramDao = new UserProgramDao(hSession);
        for ( int userNumber = 1; userNumber < TOTAL_USERS; userNumber += 2 ) {
            User user = userDao.loadUser("account-" + userNumber);
            if ( user != null ) {
                UserProgram userProgram = userProgramDao.loadUserProgram(user, program);
                if ( userProgram == null ) {
                    UserProgram userProgram2 = new UserProgram(user, program, Relation.WRITE);
                    hSession.save(userProgram2);
                    hSession.commit();
                }
            }
        }

        //Show list of users from program dao
        List<UserProgram> userProgramList = userProgramDao.loadUserProgramByProgram(program);
        assertTrue(userProgramList.size() == 50);
        for ( int userNumber = 1; userNumber < TOTAL_USERS; userNumber += 2 ) {
            User user = userDao.loadUser("account-" + userNumber);
            List<UserProgram> userProgramList2 = userProgramDao.loadUserProgramByUser(user);
            assertTrue(userProgramList2.size() == 1);
        }
    }

    private int getOneInt(String sqlStmt) {
        return this.memoryDbSetup.getOneInt(sqlStmt);
    }
}
