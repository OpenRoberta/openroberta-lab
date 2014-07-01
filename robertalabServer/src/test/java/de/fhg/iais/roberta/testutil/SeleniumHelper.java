package de.fhg.iais.roberta.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import de.fhg.iais.roberta.javaServer.jetty.ServerStarter;

public class SeleniumHelper {

    public final int port;
    public final Server server;
    public final WebDriver driver;
    public final String baseUrl;

    public SeleniumHelper(int port, String baseUrl) throws Exception {
        this.port = port;
        this.server = new ServerStarter().start(port);
        this.driver = new FirefoxDriver();
        this.baseUrl = "http://localhost:" + port + "/" + baseUrl + "/";
        this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        this.driver.get(this.baseUrl);
    }

    public void click(Button b) {
        this.driver.findElement(By.id(b.toString())).click();
    }

    public void expectError() {
        awaitTextReadyInElementReady();
        assertEquals("error", this.driver.findElement(By.id("result")).getText());
    }

    public void expectSuccess() {
        awaitTextReadyInElementReady();
        assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());
    }

    public void tearDown() throws Exception {
        this.driver.quit();
        this.server.stop();
    }

    public void assertText(String text, By id) {
        assertEquals(text, this.driver.findElement(id).getText());
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

    public static enum Button {
        B1, B2, B3, B4, B5, B6, B7, B8, B9, B10;
    }
}