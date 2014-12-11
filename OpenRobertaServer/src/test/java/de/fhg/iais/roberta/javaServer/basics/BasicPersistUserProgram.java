package de.fhg.iais.roberta.javaServer.basics;

import static de.fhg.iais.roberta.testutil.JSONUtil.assertEntityRc;
import static de.fhg.iais.roberta.testutil.JSONUtil.assertJsonEquals;
import static de.fhg.iais.roberta.testutil.JSONUtil.mkD;
import static de.fhg.iais.roberta.testutil.JSONUtil.registerToken;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.brick.Templates;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestBlocks;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserProgram;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserProgramDao;
import de.fhg.iais.roberta.util.Util;
import static org.junit.Assert.*;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.persistence.util.DbSession;
import org.junit.Test;

public class BasicPersistUserProgram {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private String connectionUrl;
      
    private static final Logger LOG = LoggerFactory.getLogger("workflow");
    private static final int TOTAL_USERS = 100;
    
    @Before
    public void setup() throws Exception {
    	
       
        Properties properties = Util.loadProperties("classpath:openRoberta-basicUserInteraction.properties");
        this.connectionUrl = properties.getProperty("hibernate.connection.url");

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();

    }

    @Test
    public void runOneUsersShareConcurrent() throws Exception {
        
        
        DbSession hSession = this.sessionFactoryWrapper.getSession();
        UserDao userDao = new UserDao(hSession);
        ProgramDao programDao = new ProgramDao(hSession);
        
        //Create list of users
        int userNumber = 0;
        while(userNumber < TOTAL_USERS){

          User user = userDao.loadUser("account-"+userNumber);
          if(user == null){
	          User user2 = new User("account-"+userNumber);
	          user2.setEmail("stuff");
	          user2.setPassword("pass-"+userNumber);
	          user2.setRole(Role.STUDENT);
	          user2.setTags("rwth");
	          hSession.save(user2);
	          hSession.commit();
          }
          userNumber+=1;
        }
        
        List<User> userList = userDao.loadUserList("created", 0, "rwth");
        assertTrue(userList.size() == 10);
        
        //Create one program per user
        userNumber = 0;
        while(userNumber < TOTAL_USERS){
        	
          User owner = userDao.loadUser("account-"+userNumber);
          Program program = programDao.load("program-"+userNumber, owner);
          if(program == null){
        	  Program program2 = new Program("program-"+userNumber,owner);
        	  String text = "<program>...</program>";
        	  program2.setProgramText(text);
              hSession.save(program2);
	          hSession.commit();
          }
          userNumber+=1;
          
        }
       
        List<Program> programList = programDao.loadAll();
        assertTrue(programList.size() == 100);
        
       //User 0 invites all inpair  users to write to its program
      User owner = userDao.loadUser("account-0");
      

      Program program = programDao.load("program-0", owner);
      UserProgramDao userProgramDao = new UserProgramDao(hSession);
      userNumber = 1;
      while(userNumber < TOTAL_USERS){
    	User user = userDao.loadUser("account-"+userNumber);
    	if(user != null){
    		UserProgram userProgram = userProgramDao.loadUserProgram(user, program);
    		if(userProgram == null){
                UserProgram userProgram2 = new UserProgram(user, program,Relation.WRITE);
                hSession.save(userProgram2);
                hSession.commit();
    		}
    	}	        

        userNumber+=2;
      }

      //Show list of users from program dao
      List<UserProgram> userProgramList = userProgramDao.loadUserProgramByProgram(program);
      assertTrue(userProgramList.size() == 50);
      userNumber = 1;
      while(userNumber < TOTAL_USERS){
    	  User user = userDao.loadUser("account-"+userNumber);
          List<UserProgram> userProgramList2 = userProgramDao.loadUserProgramByUser(user);
          assertTrue(userProgramList2.size() == 1);
    	  userNumber+=2;
      }
      

    }
    
}
