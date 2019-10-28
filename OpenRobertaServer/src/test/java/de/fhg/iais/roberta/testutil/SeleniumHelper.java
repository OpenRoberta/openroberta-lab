package de.fhg.iais.roberta.testutil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.hibernate.Session;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class SeleniumHelper {
    private static final List<String> EMPTY_STRING_LIST = Collections.emptyList();

    public int port;
    public final ServerStarter serverStarter;
    public final Server server;
    public WebDriver driver;
    public final String baseUrl;
    public boolean browserVisibility;

    public SeleniumHelper(String baseUrl) throws Exception {
        Properties properties = Util.loadProperties("classpath:/openRoberta.properties");
        this.browserVisibility = Boolean.parseBoolean(properties.getProperty("browser.visibility"));
        List<String> addr = Arrays.asList("server.ip=localhost", "server.port=1998");
        this.serverStarter = new ServerStarter("classpath:/openRoberta.properties", addr);
        this.server = this.serverStarter.start(EMPTY_STRING_LIST);
        Session session = this.serverStarter.getInjectorForTests().getInstance(SessionFactoryWrapper.class).getNativeSession();
        new DbSetup(session).createEmptyDatabase();
        this.driver = SeleniumHelper.runBrowser(this.browserVisibility);
        this.port = this.server.getURI().getPort();
        this.baseUrl = "http://localhost:" + this.port + "/" + baseUrl + "/";
        this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        this.driver.get(this.baseUrl);
    }

    public void click(Button b) {
        this.driver.findElement(By.id(b.toString())).click();
    }

    public void expectError() {
        awaitTextReadyInElementReady();
        Assert.assertEquals("error", this.driver.findElement(By.id("result")).getText());
    }

    public void expectSuccess() {
        awaitTextReadyInElementReady();
        Assert.assertEquals("asExpected", this.driver.findElement(By.id("result")).getText());
    }

    public void tearDown() throws Exception {
        this.driver.quit();
        this.server.stop();
    }

    public void assertText(String text, By id) {
        Assert.assertEquals(text, this.driver.findElement(id).getText());
    }

    private void awaitTextReadyInElementReady() {
        for ( int second = 0;; second++ ) {
            if ( second >= 60 ) {
                Assert.fail("timeout");
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

    public static WebDriver runBrowser(boolean browserVisibility) {
        throw new DbcException("PhantomDriver not yet supported");
        //        WebDriver driver;
        //        if ( browserVisibility ) {
        //            LOG.info("browserVisibility: true");
        //            FirefoxProfile fp = new FirefoxProfile();
        //            fp.setEnableNativeEvents(false);
        //            fp.setPreference("xpinstall.signatures.required", false);
        //            driver = new FirefoxDriver(fp);
        //            driver.manage().window().maximize();
        //        } else {
        //            String phantomjsBinaryPath = System.getProperty("phantomjs.binary");
        //            LOG.info("browserVisibility: false; phantomjsBinaryPath: " + phantomjsBinaryPath);
        //            DesiredCapabilities caps = DesiredCapabilities.firefox();
        //            caps.setCapability("nativeEvents", false);
        //            caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjsBinaryPath);
        //            driver = new PhantomJSDriver(caps);
        //            driver.manage().window().setSize(new Dimension(1920, 1080));
        //        }
        //        return driver;
    }
}
