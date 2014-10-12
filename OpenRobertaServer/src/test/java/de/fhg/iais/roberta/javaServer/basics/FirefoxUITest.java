package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import de.fhg.iais.roberta.javaServer.jetty.ServerStarter;

public class FirefoxUITest {
	private WebDriver driver;
	private String baseUrl;
	private String randomUser;
	private boolean acceptNextAlert = true;
	private final StringBuffer verificationErrors = new StringBuffer();
	static final int port = 1997;
	private Server server;

	@Before
	public void setUp() throws Exception {
		server = new ServerStarter().start(port);
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		baseUrl = "http://localhost:" + port;
		// Creating a random username, length 20, specify char set
		char[] chars = "ABcd!@1246ghijklmnopqrstuvwXYZ".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		randomUser = sb.toString();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void LandingpageUITest() throws Exception {
		driver.get(baseUrl + "/");
		// Home screen elements, page roundtrip
		driver.findElement(By.id("welcome-brick1")).click();
		driver.findElement(By.linkText("Logo")).click();
		driver.findElement(By.id("welcome-brick3")).click();
		driver.findElement(By.linkText("Logo")).click();
		driver.findElement(By.id("welcome-brick4")).click();
	}

	@Test
	public void BlocklyUITest() throws Exception {
		driver.get(baseUrl + "/workplace.html");
		// click buttons left & check their labels
		driver.findElement(By.xpath("//*[@id=\":1\"]")).click();
		assertEquals("Aktion",
				driver.findElement(By.xpath("//*[@id=\":1.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":2\"]")).click();
		assertEquals("Sensoren",
				driver.findElement(By.xpath("//*[@id=\":2.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":3\"]")).click();
		assertEquals("Kontrolle",
				driver.findElement(By.xpath("//*[@id=\":3.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":4\"]")).click();
		assertEquals("Logik",
				driver.findElement(By.xpath("//*[@id=\":4.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":5\"]")).click();
		assertEquals("Mathematik",
				driver.findElement(By.xpath("//*[@id=\":5.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":6\"]")).click();
		assertEquals("Text",
				driver.findElement(By.xpath("//*[@id=\":6.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":7\"]")).click();
		assertEquals("Farben",
				driver.findElement(By.xpath("//*[@id=\":7.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":8\"]")).click();
		assertEquals("Variablen",
				driver.findElement(By.xpath("//*[@id=\":8.label\"]")).getText());
		driver.findElement(By.xpath("//*[@id=\":9\"]")).click();
		assertEquals("Funktionen",
				driver.findElement(By.xpath("//*[@id=\":9.label\"]")).getText());
		// Move Blockly Element
		driver.findElement(By.xpath("//*[@id=\":1.label\"]")).click();
		// Note: element id changes
		driver.findElement(By.xpath("//*[@id=\"75\"]")).click();
		WebElement dragElement_first = driver.findElement(By
				.xpath("//*[@id=\"75\"]"));
		// Declare Action object
		Actions builder = new Actions(driver);
		// Move start item
		builder.dragAndDropBy(dragElement_first, 10, 50).build().perform();
		// And finally going home again to start screen
		driver.findElement(By.linkText("Logo")).click();
	}

	// @Test
	// public void CreateLoginUserUITest() throws Exception {
	// this.driver.get(this.baseUrl + "/workplace.html");
	// this.driver.findElement(By.id("signInIcon")).click();
	// this.driver.findElement(By.id("open-register-user")).click();
	// this.driver.findElement(By.id("accountName")).clear();
	// this.driver.findElement(By.id("accountName")).sendKeys("" +
	// this.randomUser);
	// this.driver.findElement(By.id("pass1")).clear();
	// this.driver.findElement(By.id("pass1")).sendKeys("test1234!");
	// this.driver.findElement(By.id("pass2")).clear();
	// this.driver.findElement(By.id("pass2")).sendKeys("test1234!");
	// this.driver.findElement(By.id("userName")).clear();
	// this.driver.findElement(By.id("userName")).sendKeys("RobertaSelenium");
	// this.driver.findElement(By.id("userEmail")).clear();
	// this.driver.findElement(By.id("userEmail")).sendKeys("test@roberta.de");
	// this.driver.findElement(By.name("role")).click();
	// this.driver.findElement(By.id("saveUser")).click();
	// assertEquals("User created!", closeAlertAndGetItsText());
	// // Try to login, check (at least) if name appears in the UI
	// this.driver.findElement(By.id("signInIcon")).click();
	// this.driver.findElement(By.id("accountNameS")).clear();
	// this.driver.findElement(By.id("accountNameS")).sendKeys("" +
	// this.randomUser);
	// this.driver.findElement(By.id("pass1S")).clear();
	// this.driver.findElement(By.id("pass1S")).sendKeys("test1234!");
	// this.driver.findElement(By.id("signIn")).click();
	// closeAlertAndGetItsText();
	// assertEquals("" + this.randomUser,
	// this.driver.findElement(By.id("setName")).getText());
	// }

	// @Test
	// public void LoginWrongUserUITest() throws Exception {
	// this.driver.get(this.baseUrl + "/workplace.html");
	// this.driver.findElement(By.id("signInIcon")).click();
	// this.driver.findElement(By.id("accountNameS")).clear();
	// this.driver.findElement(By.id("accountNameS")).sendKeys("" +
	// this.randomUser);
	// this.driver.findElement(By.id("pass1S")).clear();
	// this.driver.findElement(By.id("pass1S")).sendKeys("test1234!");
	// this.driver.findElement(By.id("signIn")).click();
	// assertEquals("Wrong user or wrong password!", closeAlertAndGetItsText());
	// }

	@After
	public void tearDown() throws Exception {
		driver.quit();
		server.stop();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
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
