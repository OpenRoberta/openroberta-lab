package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import de.fhg.iais.roberta.javaServer.jetty.ServerStarter;

public class SeleniumBasicsTest {
    private static final int port = 1997;

    private Server server;
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private final StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        this.server = new ServerStarter().start(port);
        this.driver = new FirefoxDriver();
        this.baseUrl = "http://localhost:" + port + "/commonTest/";
        this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testBasics() throws Exception {
        this.driver.get(this.baseUrl);
        assertEquals("Status", this.driver.findElement(By.id("ready")).getText());
        assertEquals("Ergebnisse", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t2")).click();
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t1")).click();
        this.driver.findElement(By.id("t3")).click();
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t2")).click();
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t3")).click();
        awaitTextReadyInElementReady();
        assertEquals("error", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t1")).click();
        this.driver.findElement(By.id("t1")).click();
        this.driver.findElement(By.id("t2")).click();
        awaitTextReadyInElementReady();
        assertEquals("error", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t1")).click();
        this.driver.findElement(By.id("t1")).click();
        this.driver.findElement(By.id("t3")).click();
        awaitTextReadyInElementReady();
        assertEquals("error", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t2")).click();
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t4")).click();
        this.driver.findElement(By.id("t2")).click();
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t5")).click();
        this.driver.findElement(By.id("t6")).click();
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t7")).click();
        this.driver.findElement(By.id("t8")).click();
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());

        this.driver.findElement(By.id("t2")).click();
        awaitTextReadyInElementReady();
        assertEquals("error", this.driver.findElement(By.id("result")).getText());
    }

    private void awaitTextReadyInElementReady() {
        for ( int second = 0;; second++ ) {
            if ( second >= 60 ) {
                fail("timeout");
            }
            try {
                if ( "ready".equals(this.driver.findElement(By.id("ready")).getText()) ) {
                    break;
                }
            } catch ( Exception e ) {
            }
            try {
                Thread.sleep(1000);
            } catch ( InterruptedException e ) {
                // OK
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        this.driver.quit();
        String verificationErrorString = this.verificationErrors.toString();
        if ( !"".equals(verificationErrorString) ) {
            fail(verificationErrorString);
        }
        this.server.stop();
    }
}