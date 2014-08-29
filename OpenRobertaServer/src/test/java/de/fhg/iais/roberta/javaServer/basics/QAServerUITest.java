package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.fail;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import de.fhg.iais.roberta.javaServer.jetty.ServerStarter;

public class QAServerUITest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    /**
     * using our build in OR-Jetty server and a test port
     */
    private int port;
    private Server server;
    
    
    @Before
    public void setUp() throws Exception {
    	this.port = 1997;
    	this.server = new ServerStarter().start(port);
        this.driver = new FirefoxDriver();
        this.baseUrl = "http://localhost:" + port;
        this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testORQAServerUI() throws Exception {
        this.driver.get(this.baseUrl + "/");
        this.driver.findElement(By.id("welcome-brick1")).click();
        this.driver.findElement(By.id("close-tutorials")).click();
        this.driver.findElement(By.linkText("Logo")).click();
        this.driver.findElement(By.id("welcome-brick4")).click();
        this.driver.findElement(By.id("toolbox1")).click();
        this.driver.findElement(By.id("toolbox2")).click();
        this.driver.findElement(By.xpath("//input[@value='Simulator Test']")).click();
        //this.driver.findElement(By.id("closeBtn")).click();
        this.driver.findElement(By.linkText("Logo")).click();
    }

    @After
    public void tearDown() throws Exception {
        this.driver.quit();
        this.server.stop();
        String verificationErrorString = this.verificationErrors.toString();
        if ( !"".equals(verificationErrorString) ) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            this.driver.findElement(by);
            return true;
        } catch ( NoSuchElementException e ) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            this.driver.switchTo().alert();
            return true;
        } catch ( NoAlertPresentException e ) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = this.driver.switchTo().alert();
            String alertText = alert.getText();
            if ( this.acceptNextAlert ) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            this.acceptNextAlert = true;
        }
    }
}
