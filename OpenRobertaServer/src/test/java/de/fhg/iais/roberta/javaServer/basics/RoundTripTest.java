package de.fhg.iais.roberta.javaServer.basics;

import static de.fhg.iais.roberta.testutil.JSONUtil.assertEntityRc;
import static de.fhg.iais.roberta.testutil.JSONUtil.mkD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.jetty.server.Server;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.IntegrationTest;
import de.fhg.iais.roberta.util.Util;

@Ignore
@Category(IntegrationTest.class)
public class RoundTripTest {
    private static final String resourcePath = "/roundtrip/";
    private static final String[] blocklyPrograms = {
        "action", "color", "control", "functions", "lists", "logic", "mathematic", "sensors", "text"
    };

    private static WebDriver driver;
    private static String baseUrl;

    private static Server server;

    private static SessionFactoryWrapper sessionFactoryWrapper;
    private static DbSetup memoryDbSetup;
    private static BrickCommunicator brickCommunicator;

    private static String buildXml;
    private static String connectionUrl;
    private static String crosscompilerBasedir;
    private static String robotResourcesDir;

    private static CompilerWorkflow compilerWorkflow;

    private static RestUser restUser;
    private static RestProgram restProgram;

    private static Response response;
    private static HttpSessionState s1;

    private static String blocklyProgram;
    private static Session nativeSession;
    private static StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass
    public static void setUp() throws Exception {
        initialize();
        setUpDatabase();
        startServerAndLogin();
    }

    @Test
    public void actionTest() throws Exception {
        String programName = "action";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionColor() throws Exception {
        String programName = "color";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionControl() throws Exception {
        String programName = "control";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionFunctions() throws Exception {
        String programName = "functions";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionLists() throws Exception {
        String programName = "lists";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionlogic() throws Exception {
        String programName = "logic";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionmathematic() throws Exception {
        String programName = "mathematic";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionSensors() throws Exception {
        String programName = "sensors";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @Test
    public void actionText() throws Exception {
        String programName = "text";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTripTest.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTripTest.blocklyProgram, resultProgram);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        RoundTripTest.driver.quit();
        String verificationErrorString = RoundTripTest.verificationErrors.toString();
        if ( !"".equals(verificationErrorString) ) {
            fail(verificationErrorString);
        }
        RoundTripTest.server.stop();
    }

    private static void initialize() {
        Properties properties = Util.loadProperties("classpath:openRoberta.properties");
        buildXml = properties.getProperty("crosscompiler.build.xml");
        connectionUrl = properties.getProperty("hibernate.connection.url");
        crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");
        robotResourcesDir = properties.getProperty("robot.resources.dir");

        RoundTripTest.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", connectionUrl);
        nativeSession = sessionFactoryWrapper.getNativeSession();
        memoryDbSetup = new DbSetup(nativeSession);
        memoryDbSetup.runDefaultRobertaSetup();
        brickCommunicator = new BrickCommunicator();
        compilerWorkflow = new CompilerWorkflow(crosscompilerBasedir, robotResourcesDir, buildXml);
        restUser = new RestUser(brickCommunicator);
        restProgram = new RestProgram(sessionFactoryWrapper, brickCommunicator, compilerWorkflow);

        s1 = HttpSessionState.init();
    }

    private static void setUpDatabase() throws Exception {
        assertEquals(0, getOneInt("select count(*) from USER"));
        RoundTripTest.response =
            RoundTripTest.restUser.command(
                RoundTripTest.s1,
                RoundTripTest.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'orA';'password':'Pid';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertEquals(1, getOneInt("select count(*) from USER"));
        assertTrue(!RoundTripTest.s1.isUserLoggedIn());
        RoundTripTest.response = //
            RoundTripTest.restUser.command( //
                RoundTripTest.s1,
                RoundTripTest.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'orA';'password':'Pid'}"));
        assertEntityRc(RoundTripTest.response, "ok");
        assertTrue(RoundTripTest.s1.isUserLoggedIn());
        int s1Id = RoundTripTest.s1.getUserId();
        assertEquals(0, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        for ( String program : RoundTripTest.blocklyPrograms ) {
            RoundTripTest.blocklyProgram =
                Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTripTest.resourcePath + program + ".xml"), Charsets.UTF_8);
            JSONObject fullRequest = new JSONObject("{\"log\":[];\"data\":{\"cmd\":\"saveP\";\"name\":\"" + program + "\"}}");
            fullRequest.getJSONObject("data").put("program", RoundTripTest.blocklyProgram);
            RoundTripTest.response = RoundTripTest.restProgram.command(RoundTripTest.s1, fullRequest);
            assertEntityRc(RoundTripTest.response, "ok");
        }
    }

    private static void startServerAndLogin() throws IOException {
        RoundTripTest.server = new ServerStarter("classpath:openRoberta.properties").start();
        int port = RoundTripTest.server.getURI().getPort();
        RoundTripTest.driver = new FirefoxDriver();
        RoundTripTest.driver.manage().window().maximize();
        RoundTripTest.baseUrl = "http://localhost:" + port;
        RoundTripTest.driver.get(RoundTripTest.baseUrl + "/");
        RoundTripTest.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //Login
        WebElement user = RoundTripTest.driver.findElement(By.id("head-navigation-login"));
        user.click();

        WebElement userLoginElement = (new WebDriverWait(RoundTripTest.driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("login")));
        userLoginElement.click();
        RoundTripTest.driver.findElement(By.id("accountNameS")).clear();
        RoundTripTest.driver.findElement(By.id("accountNameS")).sendKeys("orA");
        RoundTripTest.driver.findElement(By.id("pass1S")).clear();
        RoundTripTest.driver.findElement(By.id("pass1S")).sendKeys("Pid");
        RoundTripTest.driver.findElement(By.id("doLogin")).click();
    }

    private String saveProgram(String programName) throws InterruptedException, Exception, JSONException {
        WebElement programElement = RoundTripTest.driver.findElement(By.id("head-navigation-program"));
        programElement.click();
        WebElement userProgramSaveAsElement = (new WebDriverWait(RoundTripTest.driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("saveAsProg")));
        userProgramSaveAsElement.click();
        RoundTripTest.driver.findElement(By.id("programNameSave")).clear();
        RoundTripTest.driver.findElement(By.id("programNameSave")).sendKeys(programName);
        RoundTripTest.driver.findElement(By.id("saveProgram")).click();
        Thread.sleep(1000);
        RoundTripTest.response = RoundTripTest.restProgram.command(RoundTripTest.s1, mkD("{'cmd':'loadP';'name':'" + programName + "'}"));
        String resultProgram = ((JSONObject) RoundTripTest.response.getEntity()).getString("data");
        return resultProgram;
    }

    private void loadProgram(String program) throws InterruptedException {
        WebElement programElement = RoundTripTest.driver.findElement(By.id("head-navigation-program"));
        programElement.click();
        int index = Arrays.binarySearch(blocklyPrograms, program);
        WebElement userProgramOpenElement =
            (new WebDriverWait(RoundTripTest.driver, 10)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("listProg")));
        userProgramOpenElement.click();
        WebElement programTable = RoundTripTest.driver.findElement(By.id("programNameTable"));
        WebElement tr = programTable.findElements(By.tagName("tr")).get(index + 1);
        tr.findElement(By.tagName("td")).click();
        Thread.sleep(500);
        RoundTripTest.driver.findElement(By.id("loadFromListing")).click();
    }

    private static int getOneInt(String sqlStmt) {
        return RoundTripTest.memoryDbSetup.getOneInt(sqlStmt);
    }

}