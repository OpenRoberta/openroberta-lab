package de.fhg.iais.roberta.javaServer.basics;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 
 * Download chrome driver for your OS.
 * http://chromedriver.storage.googleapis.com/index.html
 * Add path as a System property.
 * See example below.
 * System.setProperty("webdriver.chrome.driver", "path to Google chrome driver");
 * 
*/ 

@Ignore
public class QAServerChromeUITest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	/**
	 * Download chrome driver for your os.
	 * http://chromedriver.storage.googleapis.com/index.html
	*/  
	System.setProperty("webdriver.chrome.driver", "/Users/ket/dev/chromedriver");
    driver = new ChromeDriver();
    baseUrl = "http://mp-devel.iais.fraunhofer.de:1999/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testORQAServerUI() throws Exception {
    driver.get(baseUrl + "/");
    //driver.findElement(By.id("welcome-brick1")).click();
    //driver.findElement(By.id("close-tutorials")).click();
    //driver.findElement(By.linkText("Logo")).click();
    driver.findElement(By.id("welcome-brick4")).click();
    driver.findElement(By.id("toolbox1")).click();
    driver.findElement(By.id("toolbox2")).click();
    driver.findElement(By.xpath("//input[@value='Simulator Test']")).click();
    driver.findElement(By.id("closeBtn")).click();
    driver.findElement(By.linkText("Logo")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
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
