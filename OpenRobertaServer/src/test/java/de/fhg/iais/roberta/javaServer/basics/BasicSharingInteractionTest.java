package de.fhg.iais.roberta.javaServer.basics;

import static de.fhg.iais.roberta.testutil.JSONUtil.assertEntityRc;
import static de.fhg.iais.roberta.testutil.JSONUtil.assertJsonEquals;
import static de.fhg.iais.roberta.testutil.JSONUtil.mkD;
import static de.fhg.iais.roberta.testutil.JSONUtil.registerToken;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.nio.file.Files;
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

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.brick.Templates;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestBlocks;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;
import static org.junit.Assert.*;

import org.junit.Test;

public class BasicSharingInteractionTest {
	
    private static final Logger LOG = LoggerFactory.getLogger("workflow");
    private static final int MAX_TOTAL_USERS = 400;

    private BrickCommunicator brickCommunicator;
    private SessionFactoryWrapper sessionFactoryWrapper;
    private String connectionUrl;
    private DbSetup memoryDbSetup;

    private CompilerWorkflow compilerWorkflow;
    private String crosscompilerBasedir;
    
    private RestUser restUser;
    private RestProgram restProgram;
    private RestBlocks restBlocks;
    private String buildXml;
    
    @Before
    public void setup() throws Exception {

        Properties properties = Util.loadProperties("classpath:openRoberta-basicUserInteraction.properties");
        this.connectionUrl = properties.getProperty("hibernate.connection.url");
        this.buildXml = properties.getProperty("crosscompiler.build.xml");
        this.crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");

        this.sessionFactoryWrapper = new SessionFactoryWrapper("my-hybernate-test-cdg.xml",this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();

        
        this.brickCommunicator = new BrickCommunicator();
        this.compilerWorkflow = new CompilerWorkflow(this.crosscompilerBasedir, this.buildXml);

        this.restUser = new RestUser(this.brickCommunicator);
        this.restProgram = new RestProgram(this.sessionFactoryWrapper, this.brickCommunicator, this.compilerWorkflow);
        this.restBlocks = new RestBlocks(new Templates(), this.brickCommunicator);
        

    }

    @Test
    public void runOneUsersShareConcurrent() throws Exception {
        
        boolean success = true;
        
        
        //Workflow to create users and programs
        for(int i = 0; i < 20; i+=1){
            try {
            	creationWorkflow(i);
                success = success && true;
            } catch ( Exception e ) {
                LOG.info("" + i + ";error;");
                LoggerFactory.getLogger("workflowError").error("Workflow " + i + " terminated with errors", e);
                success = success && false;
            }
        }
        
        //User one shares the program with inpair users
        try {
        	sharingWorkflow();
            success = success && true;
        } catch ( Exception e ) {
            LOG.info("sharing error;");
            LoggerFactory.getLogger("workflowError").error("Sharing workflow terminated with errors", e);
            success = success && false;
        }
        

    }
    
    private void creationWorkflow(int userNumber) throws Exception {
    	
        LOG.info("" + userNumber + ";start;");
        HttpSessionState s = HttpSessionState.init();
        assertTrue(!s.isUserLoggedIn());

        // create user "pid-*" with success
        
        //Create user
        Response response =
            this.restUser.command(s, this.sessionFactoryWrapper.getSession(), mkD("{'cmd':'createUser';'accountName':'pid-"
                + userNumber
                + "';'password':'dip-"
                + userNumber
                + "';'userEmail':'cavy@home';'role':'STUDENT';'tag':'RWTH'}"));
        
        assertEntityRc(response, "ok");
        
        //Login with user "pid"
        response = 
            this.restUser.command(
                s,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid-" + userNumber + "';'password':'dip-" + userNumber + "'}"));
        assertEntityRc(response, "ok");
        assertTrue(s.isUserLoggedIn());
        int sId = s.getUserId();
        
      //Create 2 programs
      response = this.restProgram.command(s, mkD("{'cmd':'saveP';'name':'test';'program':'<program>...</program>'}"));
      response = this.restProgram.command(s, mkD("{'cmd':'saveP';'name':'test2';'program':'<program>...</program>'}"));
      assertEntityRc(response, "ok");
        
    }
    
    private void sharingWorkflow() throws Exception{
    	
        HttpSessionState s = HttpSessionState.init();
        
        //Login with user 0
        Response  response = 
            this.restUser.command(
                s,
                this.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'pid-0';'password':'dip-0'}"));
        assertEntityRc(response, "ok");
        assertTrue(s.isUserLoggedIn());
        int sId = s.getUserId();
        
        //Show list of people irrespective of their relation
      response = this.restUser.command(
      			s,
      		 	this.sessionFactoryWrapper.getSession(),
      		 	mkD("{'cmd':'obtainUsers'; 'sortBy':'created';'offset':0;'tagFilter':'RWTH'}"));
      assertEntityRc(response, "ok");
      
//      // Change user_program relations "pid-*" with success
//	response = this.restProgram.command(s, mkD("{'cmd':'setUsersRights';'usersList': "
//			+ "[{'id':46; 'name':'pid-4';'right':'READ'}, "
//			+ "{'id':49; 'name':'pid-7';'right':'WRITE'}];'programName':'test';'userId':"+sId+"}"));
//	  
//      //Show list of people related to a given program
//      response = this.restUser.command(s,this.sessionFactoryWrapper.getSession(),mkD("{'cmd':'usersFromProgram';'programName':'test'}"));
//      assertEntityRc(response, "ok");
//      
//      //Show list of programs rigths related to a given user
//      response = this.restUser.command(s,this.sessionFactoryWrapper.getSession(),mkD("{'cmd':'usersFromProgram';'programName':'test'}"));
//      assertEntityRc(response, "ok");
    
    }



}
