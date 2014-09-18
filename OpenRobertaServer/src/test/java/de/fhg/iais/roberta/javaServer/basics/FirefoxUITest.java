package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertEquals;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import de.fhg.iais.roberta.javaServer.jetty.ServerStarter;

public class FirefoxUITest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	/**
	 * using our build in OR-Jetty server and a test port
	 */
	static final int port = 1997;
	private Server server;

	@Before
	public void setUp() throws Exception {
		server = new ServerStarter().start(port);
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		baseUrl = "http://localhost:" + port;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUIElements() throws Exception {
		driver.get(baseUrl + "/");

		// Home screen elements, page roundtrip
		driver.findElement(By.id("welcome-brick1")).click();
		driver.findElement(By.linkText("Logo")).click();
		driver.findElement(By.id("welcome-brick3")).click();
		driver.findElement(By.linkText("Logo")).click();
		driver.findElement(By.id("welcome-brick4")).click();

		// Testing WORKPLACE
		// click buttons left & check their labels
		driver.findElement(By.xpath("//*[@id=\":1\"]")).click();
		assertEquals("AKTION",
				driver.findElement(By.xpath("//*[@id=\":1.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":2\"]")).click();
		assertEquals("SENSOREN",
				driver.findElement(By.xpath("//*[@id=\":2.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":3\"]")).click();
		assertEquals("KONTROLLE",
				driver.findElement(By.xpath("//*[@id=\":3.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":4\"]")).click();
		assertEquals("LOGIK",
				driver.findElement(By.xpath("//*[@id=\":4.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":5\"]")).click();
		assertEquals("MATHEMATIK",
				driver.findElement(By.xpath("//*[@id=\":5.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":6\"]")).click();
		assertEquals("TEXT",
				driver.findElement(By.xpath("//*[@id=\":6.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":7\"]")).click();
		assertEquals("FARBEN",
				driver.findElement(By.xpath("//*[@id=\":7.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":8\"]")).click();
		assertEquals("VARIABLEN",
				driver.findElement(By.xpath("//*[@id=\":8.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":9\"]")).click();
		assertEquals("FUNKTIONEN",
				driver.findElement(By.xpath("//*[@id=\":9.label\"]")).getText());

		// MOVE BLOCK
		driver.findElement(By.xpath("//*[@id=\":1\"]")).click();
		// Note: element id changes
		driver.findElement(By.xpath("//*[@id=\"66\"]")).click();
		WebElement dragElement_first = driver.findElement((By
				.xpath("//*[@id=\"66\"]")));
		// Declare Action object
		Actions builder = new Actions(driver);
		// Move start item
		builder.dragAndDropBy(dragElement_first, 10, 50).build().perform();

		// And finally going home again to start screen
		driver.findElement(By.linkText("Logo")).click();
	}

	@After
	public void tearDown() throws Exception {
		this.driver.quit();
		this.server.stop();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
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