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
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
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
import de.fhg.iais.roberta.util.Util;

public class RoundTrip {
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
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionColor() throws Exception {
        String programName = "color";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionControl() throws Exception {
        String programName = "control";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionFunctions() throws Exception {
        String programName = "functions";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionLists() throws Exception {
        String programName = "lists";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionlogic() throws Exception {
        String programName = "logic";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionmathematic() throws Exception {
        String programName = "mathematic";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionSensors() throws Exception {
        String programName = "sensors";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    @Test
    public void actionText() throws Exception {
        String programName = "text";
        loadProgram(programName);
        String resultProgram = saveProgram(programName);
        RoundTrip.blocklyProgram =
            Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + programName + ".xml"), Charsets.UTF_8);
        Helper.assertXML(RoundTrip.blocklyProgram, resultProgram);
    }

    private String saveProgram(String programName) throws InterruptedException, Exception, JSONException {
        Actions actions = new Actions(RoundTrip.driver);
        WebElement onHoverProgramElement1 = RoundTrip.driver.findElement(By.cssSelector("span.head-navi-text"));
        actions.moveToElement(onHoverProgramElement1);
        actions.perform();
        WebElement userProgramSaveAsElement = (new WebDriverWait(RoundTrip.driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("saveAsProg")));
        userProgramSaveAsElement.click();
        RoundTrip.driver.findElement(By.id("programNameSave")).clear();
        RoundTrip.driver.findElement(By.id("programNameSave")).sendKeys(programName);
        RoundTrip.driver.findElement(By.id("saveProgram")).click();
        Thread.sleep(1000);
        RoundTrip.response = RoundTrip.restProgram.command(RoundTrip.s1, mkD("{'cmd':'loadP';'name':'" + programName + "'}"));
        String resultProgram = ((JSONObject) RoundTrip.response.getEntity()).getString("data");
        return resultProgram;
    }

    private void loadProgram(String program) throws InterruptedException {
        Actions actions = new Actions(RoundTrip.driver);
        WebElement onHoverProgramElement = RoundTrip.driver.findElement(By.cssSelector("span.head-navi-text"));
        actions.click(onHoverProgramElement);
        actions.moveToElement(onHoverProgramElement);
        actions.perform();

        int index = Arrays.binarySearch(blocklyPrograms, program);
        WebElement userProgramOpenElement = (new WebDriverWait(RoundTrip.driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("openProg")));
        userProgramOpenElement.click();
        WebElement programTable = RoundTrip.driver.findElement(By.id("programNameTable"));
        WebElement tr = programTable.findElements(By.tagName("tr")).get(index + 1);
        System.out.println(tr.getText());
        tr.findElement(By.tagName("td")).click();

        Thread.sleep(500);
        RoundTrip.driver.findElement(By.id("loadFromListing")).click();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        RoundTrip.driver.quit();
        String verificationErrorString = RoundTrip.verificationErrors.toString();
        if ( !"".equals(verificationErrorString) ) {
            fail(verificationErrorString);
        }
        RoundTrip.server.stop();
    }

    private static void startServerAndLogin() throws IOException {
        RoundTrip.server = new ServerStarter("classpath:openRoberta.properties").start();
        int port = RoundTrip.server.getURI().getPort();
        RoundTrip.driver = new FirefoxDriver();
        RoundTrip.driver.manage().window().maximize();
        RoundTrip.baseUrl = "http://localhost:" + port;
        RoundTrip.driver.get(RoundTrip.baseUrl + "/");
        RoundTrip.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        Actions actions = new Actions(RoundTrip.driver);

        //Login
        WebElement onHoverUserElement = RoundTrip.driver.findElement(By.cssSelector("span.head-navi-icons.user"));
        actions.moveToElement(onHoverUserElement);
        actions.perform();
        WebElement userLoginElement = (new WebDriverWait(RoundTrip.driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("login")));
        userLoginElement.click();
        RoundTrip.driver.findElement(By.id("accountNameS")).clear();
        RoundTrip.driver.findElement(By.id("accountNameS")).sendKeys("orA");
        RoundTrip.driver.findElement(By.id("pass1S")).clear();
        RoundTrip.driver.findElement(By.id("pass1S")).sendKeys("Pid");
        RoundTrip.driver.findElement(By.id("doLogin")).click();
    }

    private static void initialize() {
        Properties properties = Util.loadProperties("classpath:openRoberta.properties");
        buildXml = properties.getProperty("crosscompiler.build.xml");
        connectionUrl = properties.getProperty("hibernate.connection.url");
        crosscompilerBasedir = properties.getProperty("crosscompiler.basedir");

        RoundTrip.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", connectionUrl);
        nativeSession = sessionFactoryWrapper.getNativeSession();
        memoryDbSetup = new DbSetup(nativeSession);
        memoryDbSetup.runDefaultRobertaSetup();
        brickCommunicator = new BrickCommunicator();
        compilerWorkflow = new CompilerWorkflow(crosscompilerBasedir, buildXml);
        restUser = new RestUser(brickCommunicator);
        restProgram = new RestProgram(sessionFactoryWrapper, brickCommunicator, compilerWorkflow);

        s1 = HttpSessionState.init();
    }

    private static void setUpDatabase() throws Exception {
        assertEquals(0, getOneInt("select count(*) from USER"));
        RoundTrip.response =
            RoundTrip.restUser.command(
                RoundTrip.s1,
                RoundTrip.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'createUser';'accountName':'orA';'password':'Pid';'userEmail':'cavy@home';'role':'STUDENT'}"));
        assertEquals(1, getOneInt("select count(*) from USER"));
        assertTrue(!RoundTrip.s1.isUserLoggedIn());
        RoundTrip.response = //
            RoundTrip.restUser.command( //
                RoundTrip.s1,
                RoundTrip.sessionFactoryWrapper.getSession(),
                mkD("{'cmd':'login';'accountName':'orA';'password':'Pid'}"));
        assertEntityRc(RoundTrip.response, "ok");
        assertTrue(RoundTrip.s1.isUserLoggedIn());
        int s1Id = RoundTrip.s1.getUserId();
        assertEquals(0, getOneInt("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        for ( String program : RoundTrip.blocklyPrograms ) {
            RoundTrip.blocklyProgram =
                Resources.toString(BasicPerformanceUserInteractionTest.class.getResource(RoundTrip.resourcePath + program + ".xml"), Charsets.UTF_8);
            JSONObject fullRequest = new JSONObject("{\"log\":[];\"data\":{\"cmd\":\"saveP\";\"name\":\"" + program + "\"}}");
            fullRequest.getJSONObject("data").put("program", RoundTrip.blocklyProgram);
            RoundTrip.response = RoundTrip.restProgram.command(RoundTrip.s1, fullRequest);
            assertEntityRc(RoundTrip.response, "ok");
        }
    }

    private static int getOneInt(String sqlStmt) {
        return RoundTrip.memoryDbSetup.getOneInt(sqlStmt);
    }

}
