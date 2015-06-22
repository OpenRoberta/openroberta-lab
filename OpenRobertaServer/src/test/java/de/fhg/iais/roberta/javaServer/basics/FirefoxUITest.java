package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.util.testsetup.IntegrationTest;

@Ignore
@Category(IntegrationTest.class)
public class FirefoxUITest {
    public static WebDriver driver;
    private static Actions driverActions;
    private static String baseUrl;
    private static String randomUser;
    private final static StringBuffer verificationErrors = new StringBuffer();
    static final int port = 1999;
    private static Server server;

    @BeforeClass
    public static void setUp() throws Exception {
        // do the setup

        server = new ServerStarter(null).start();
        int port = server.getURI().getPort();
        driver = new PhantomJSDriver();
        driver.manage().window().maximize();
        baseUrl = "http://localhost:" + port;
        // Creating a random username, length 20, specify char set
        char[] chars = "ABcd!@1246ghijklmnopqrstuvwXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for ( int i = 0; i < 20; i++ ) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        randomUser = sb.toString();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.get(baseUrl + "/index.html");
    }

    @Test
    public void BlocklyUITest() throws Exception {
        // click buttons left & check their labels
        driver.findElement(By.xpath("//*[@id=\":1\"]")).click();
        assertEquals("Aktion", driver.findElement(By.xpath("//*[@id=\":1.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":2\"]")).click();
        assertEquals("Sensoren", driver.findElement(By.xpath("//*[@id=\":2.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":3\"]")).click();
        assertEquals("Kontrolle", driver.findElement(By.xpath("//*[@id=\":3.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":4\"]")).click();
        assertEquals("Logik", driver.findElement(By.xpath("//*[@id=\":4.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":5\"]")).click();
        assertEquals("Mathematik", driver.findElement(By.xpath("//*[@id=\":5.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":6\"]")).click();
        assertEquals("Text", driver.findElement(By.xpath("//*[@id=\":6.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":7\"]")).click();
        assertEquals("Farben", driver.findElement(By.xpath("//*[@id=\":7.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":8\"]")).click();
        assertEquals("Variablen", driver.findElement(By.xpath("//*[@id=\":8.label\"]")).getText());
        driver.findElement(By.xpath("//*[@id=\":9\"]")).click();
        assertEquals("Funktionen", driver.findElement(By.xpath("//*[@id=\":9.label\"]")).getText());
        // Move Blockly Element
        driver.findElement(By.xpath("//*[@id=\":1.label\"]")).click();
        // Note: element id changes
        driver.findElement(By.xpath("//*[@id=\"74\"]")).click();
        WebElement dragElement_first = driver.findElement(By.xpath("//*[@id=\"74\"]"));
        // Move start item
        driverActions.dragAndDropBy(dragElement_first, 10, 50).build().perform();
    }

    @Test
    public void CreateLoginUserUITest() throws Exception {
        Thread.sleep(500);
        driver.findElement(By.cssSelector("span.head-navi-icons.user")).click();
        Thread.sleep(500);
        driver.findElement(By.id("new")).click();
        Thread.sleep(500);
        driver.findElement(By.id("accountName")).clear();
        driver.findElement(By.id("accountName")).sendKeys("" + randomUser);
        driver.findElement(By.id("pass1")).clear();
        driver.findElement(By.id("pass1")).sendKeys("test1234!");
        driver.findElement(By.id("pass2")).clear();
        driver.findElement(By.id("pass2")).sendKeys("test1234!");
        driver.findElement(By.id("userName")).clear();
        driver.findElement(By.id("userName")).sendKeys("RobertaSelenium");
        driver.findElement(By.id("userEmail")).clear();
        driver.findElement(By.id("userEmail")).sendKeys("test@roberta.de");
        driver.findElement(By.name("role")).click();
        driver.findElement(By.id("saveUser")).click();
        // Try to login, check (at least) if name appears in the UI
        driver.findElement(By.cssSelector("span.head-navi-icons.user")).click();
        Thread.sleep(500);
        driver.findElement(By.id("login")).click();
        Thread.sleep(500);
        driver.findElement(By.id("accountNameS")).clear();
        driver.findElement(By.id("accountNameS")).sendKeys("" + randomUser);
        driver.findElement(By.id("pass1S")).clear();
        driver.findElement(By.id("pass1S")).sendKeys("test1234!");
        driver.findElement(By.id("doLogin")).click();
        Thread.sleep(500);
        assertEquals("" + randomUser, driver.findElement(By.id("displayLogin")).getText());
        // Delete the newly created user
        driver.findElement(By.cssSelector("span.head-navi-icons.user")).click();
        Thread.sleep(500);
        driver.findElement(By.id("logout")).click();
        assertEquals("", driver.findElement(By.id("displayLogin")).getText());
    }

    @Test
    public void LoginWrongUserUITest() throws Exception {
        Thread.sleep(500);
        driver.findElement(By.cssSelector("span.head-navi-icons.user")).click();
        Thread.sleep(500);
        driver.findElement(By.id("login")).click();
        Thread.sleep(500);
        driver.findElement(By.id("accountNameS")).clear();
        driver.findElement(By.id("accountNameS")).sendKeys("Wrong user");
        driver.findElement(By.id("pass1S")).clear();
        driver.findElement(By.id("pass1S")).sendKeys("test1234!");
        driver.findElement(By.id("doLogin")).click();
        WebElement result = driver.findElement(By.id("message7"));
        assertEquals("message7", result.getAttribute("lkey"));

    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
        server.stop();
        String verificationErrorString = verificationErrors.toString();
        if ( !"".equals(verificationErrorString) ) {
            fail(verificationErrorString);
        }
    }
}
