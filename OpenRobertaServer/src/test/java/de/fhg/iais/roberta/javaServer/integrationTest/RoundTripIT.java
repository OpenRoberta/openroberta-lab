package de.fhg.iais.roberta.javaServer.integrationTest;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import org.eclipse.jetty.server.Server;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.javaServer.basics.TestConfiguration;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientProgramController;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientUser;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.testutil.SeleniumHelper;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.XsltTransformer;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

@Ignore
@Category(IntegrationTest.class)
public class RoundTripIT {
    private static final String resourcePath = "/roundtrip/";
    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();
    private static final String[] blocklyPrograms =
        {
            "move",
            "drive",
            "show",
            "sound",
            "status_light",
            "sensors",
            "logic",
            "control_stmt",
            "wait",
            "math",
            "text",
            "list",
            "methods",
            "bluetooth"
        };

    private static WebDriver driver;
    private static String baseUrl;
    private static boolean browserVisibility;
    private static Server server;

    private static SessionFactoryWrapper sessionFactoryWrapper;
    private static DbSetup memoryDbSetup;
    private static RobotCommunicator brickCommunicator;
    private static XsltTransformer xsltTransformer;

    private static ClientUser restUser;
    private static ClientProgramController restProject;

    private static Response response;
    private static HttpSessionState s1;

    private static String blocklyProgram;
    private static StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        // TODO: Does this work? Check and re-engineer
        ServerProperties serverProperties = new ServerProperties(Util.loadProperties("classpath:/openRoberta.properties"));
        browserVisibility = Boolean.parseBoolean(serverProperties.getStringProperty("browser.visibility"));
        brickCommunicator = new RobotCommunicator();
        xsltTransformer = new XsltTransformer();

        TestConfiguration tc = TestConfiguration.setup();
        sessionFactoryWrapper = tc.getSessionFactoryWrapper();
        memoryDbSetup = tc.getMemoryDbSetup();

        restUser = new ClientUser(brickCommunicator, serverProperties, null);
        restProject = new ClientProgramController(serverProperties);
        Map<String, IRobotFactory> robotPlugins = new HashMap<>();
        loadPlugin(robotPlugins);
        s1 = HttpSessionState.initOnlyLegalForDebugging("", robotPlugins, serverProperties, 1);

        driver = SeleniumHelper.runBrowser(browserVisibility);
        setUpDatabase();
        startServerAndLogin();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if ( !"".equals(verificationErrorString) ) {
            Assert.fail(verificationErrorString);
        }
        server.stop();
    }

    @Test
    public void actionMove() throws Exception {
        assertRoundTrip(blocklyPrograms[0]);
    }

    @Test
    public void actionDrive() throws Exception {
        assertRoundTrip(blocklyPrograms[1]);
    }

    @Test
    public void actionShow() throws Exception {
        assertRoundTrip(blocklyPrograms[2]);
    }

    @Test
    public void actionSound() throws Exception {
        assertRoundTrip(blocklyPrograms[3]);
    }

    @Test
    public void actionStatusLight() throws Exception {
        assertRoundTrip(blocklyPrograms[4]);
    }

    @Test
    public void actionSensors() throws Exception {
        assertRoundTrip(blocklyPrograms[5]);
    }

    @Test
    public void actionLogic() throws Exception {
        assertRoundTrip(blocklyPrograms[6]);
    }

    @Test
    public void actionControl() throws Exception {
        assertRoundTrip(blocklyPrograms[7]);
    }

    @Test
    public void actionWait() throws Exception {
        assertRoundTrip(blocklyPrograms[8]);
    }

    @Test
    public void actionMath() throws Exception {
        assertRoundTrip(blocklyPrograms[9]);
    }

    @Test
    public void actionText() throws Exception {
        assertRoundTrip(blocklyPrograms[10]);
    }

    @Test
    public void actionLists() throws Exception {
        assertRoundTrip(blocklyPrograms[11]);
    }

    @Test
    public void actionMethods() throws Exception {
        assertRoundTrip(blocklyPrograms[12]);
    }

    @Test
    public void actionBluetooth() throws Exception {
        assertRoundTrip(blocklyPrograms[13]);
    }

    private void setUpDatabase() throws Exception {
        Assert.assertEquals(1, getOneBigInteger("select count(*) from USER"));
        response =
            restUser
                .createUser(
                    newDbSession(),
                    mkCmd("{'cmd':'createUser';'accountName':'orA';'userName':'orA';'password':'Pid';'userEmail':'cavy@home';'role':'STUDENT'}"));
        Assert.assertEquals(2, getOneBigInteger("select count(*) from USER"));
        Assert.assertTrue(!s1.isUserLoggedIn());
        response = restUser.login(newDbSession(), mkCmd("{'cmd':'login';'accountName':'orA';'password':'Pid'}"));
        JSONUtilForServer.assertEntityRc(response, "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertTrue(s1.isUserLoggedIn());
        int s1Id = s1.getUserId();
        Assert.assertEquals(0, getOneBigInteger("select count(*) from PROGRAM where OWNER_ID = " + s1Id));
        for ( String program : blocklyPrograms ) {
            blocklyProgram = Resources.toString(PerformanceUserIT.class.getResource(resourcePath + program + ".xml"), Charsets.UTF_8);
            JSONObject fullRequest = new JSONObject("{\"log\":[];\"data\":{\"cmd\":\"saveAsP\";\"name\":\"" + program + "\";\"timestamp\":0}}");
            fullRequest.getJSONObject("data").put("program", blocklyProgram);
            response = restProject.saveProgram(newDbSession(), FullRestRequest.make(fullRequest));
            JSONUtilForServer.assertEntityRc(response, "ok", Key.PROGRAM_SAVE_SUCCESS);
        }
    }

    private void startServerAndLogin() throws IOException, InterruptedException {
        List<String> addr = Arrays.asList("server.ip=localhost", "server.port=1998");
        server = new ServerStarter("classpath:/openRoberta.properties", addr).start(EMPTY_STRING_LIST);
        int port = server.getURI().getPort();
        baseUrl = "http://localhost:" + port;
        driver.get(baseUrl + "/");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //Welcome message dismiss
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("startupClose"))).click();

        //Login
        WebElement user = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("head-navi-icon-user")));

        while ( !driver.findElement(By.id("menuLogin")).isDisplayed() ) {
            user.click();
        }

        WebElement userLoginElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("menuLogin")));
        userLoginElement.click();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("doLogin")));
        driver.findElement(By.id("loginAccountName")).clear();
        driver.findElement(By.id("loginAccountName")).sendKeys("orA");
        driver.findElement(By.id("loginPassword")).clear();
        driver.findElement(By.id("loginPassword")).sendKeys("Pid");
        driver.findElement(By.id("doLogin")).click();
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("doLogin")));
    }

    private String saveProgram(String programName) throws InterruptedException, Exception, JSONException {

        WebElement programElement = driver.findElement(By.id("head-navi-icon-program"));

        while ( !driver.findElement(By.id("menuSaveProg")).isDisplayed() ) {
            programElement.click();
        }
        WebElement userProgramSaveAsElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("menuSaveProg")));
        userProgramSaveAsElement.click();

        response = restProject.importProgram(xsltTransformer, mkCmd("{'cmd':'loadP';'name':'" + programName + "';'owner':'orA'}"));
        String resultProgram = ((JSONObject) response.getEntity()).getString("data");
        return resultProgram;
    }

    private void loadProgram(String program) throws InterruptedException {
        WebElement programElement = driver.findElement(By.id("head-navi-icon-program"));

        while ( !driver.findElement(By.id("menuListProg")).isDisplayed() ) {
            programElement.click();
        }

        WebElement userProgramOpenElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("menuListProg")));
        userProgramOpenElement.click();
        WebElement programTable = (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("programNameTable")));

        (new WebDriverWait(driver, 10)).until(ExpectedConditions.textToBePresentInElement(programTable, program));

        Actions actions = new Actions(driver);
        WebElement td = findTableRow(program, programTable);
        actions.doubleClick(td).build();
    }

    protected WebElement findTableRow(String program, WebElement programTable) {
        Iterator<WebElement> it = programTable.findElements(By.tagName("tr")).iterator();
        it.next();
        while ( it.hasNext() ) {
            WebElement webE = it.next();
            WebElement tt = webE.findElement(By.tagName("td"));
            if ( tt.getText().equals(program) ) {
                return webE.findElement(By.tagName("td"));
            }
        }
        return null;
    }

    protected void assertRoundTrip(String programName) throws InterruptedException, Exception, JSONException, IOException {
        loadProgram(programName);

        String resultProgram = saveProgram(programName);
        blocklyProgram = Resources.toString(PerformanceUserIT.class.getResource(resourcePath + programName + ".xml"), Charsets.UTF_8);
        //        Helper.assertXML(blocklyProgram, resultProgram);
    }

    private long getOneBigInteger(String sqlStmt) {
        return memoryDbSetup.getOneBigIntegerAsLong(sqlStmt);
    }

    private static void loadPlugin(Map<String, IRobotFactory> robotPlugins) {
        try {
            @SuppressWarnings("unchecked")
            Class<IRobotFactory> factoryClass = (Class<IRobotFactory>) ServerStarter.class.getClassLoader().loadClass("de.fhg.iais.roberta.factory.EV3Factory");
            Constructor<IRobotFactory> factoryConstructor = factoryClass.getDeclaredConstructor(RobotCommunicator.class);
            robotPlugins.put("ev3", factoryConstructor.newInstance(RoundTripIT.brickCommunicator));
        } catch ( Exception e ) {
            throw new DbcException("robot plugin ev3 has an invalid factory. Check the properties. Server does NOT start", e);
        }
    }

    private static DbSession newDbSession() {
        return sessionFactoryWrapper.getSession();
    }

    private static FullRestRequest mkCmd(String cmdAsString) throws JSONException {
        return JSONUtilForServer.mkFRR(cmdAsString);
    }
}