package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import de.fhg.iais.roberta.javaServer.jetty.ServerStarter;

public class SeleniumBasicsTest
{
    private static final int port = 1997;

    private Server server;
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        server = new ServerStarter().start(port);
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:" + port + "/commonTest/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testBasics() throws Exception {
        driver.get(baseUrl);
        assertEquals("Status", driver.findElement(By.id("ready")).getText());
        assertEquals("Ergebnisse", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t2")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t3")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t2")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t3")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("error", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t2")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("error", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t3")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("error", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t2")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t4")).click();
        driver.findElement(By.id("t2")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        driver.findElement(By.id("t5")).click();
        driver.findElement(By.id("t6")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t7")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        driver.findElement(By.id("t8")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t2")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("error", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("i1")).clear();
        driver.findElement(By.id("i1")).sendKeys("0");
        driver.findElement(By.id("t9")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("t1")).click();
        driver.findElement(By.id("i1")).clear();
        driver.findElement(By.id("i1")).sendKeys("5");
        driver.findElement(By.id("t9")).click();
        for ( int second = 0;; second++ ) {
            if ( second >= 60 )
                fail("timeout");
            try {
                if ( "ready".equals(driver.findElement(By.id("ready")).getText()) )
                    break;
            } catch ( Exception e ) {
            }
            Thread.sleep(1000);
        }

        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
        assertEquals("asExpected", driver.findElement(By.id("result")).getText());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if ( !"".equals(verificationErrorString) ) {
            fail(verificationErrorString);
        }
        server.stop();
    }

    @SuppressWarnings("unused")
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch ( NoSuchElementException e ) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch ( NoAlertPresentException e ) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if ( acceptNextAlert ) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}