package de.fhg.iais.roberta.jaxb;

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

public class XmlRobertaTest {
	private static final int port = 1997;

	private Server server;
	private WebDriver driver;
	private String baseUrl;
	private final StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {

		server = new ServerStarter().start(port);
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:" + port + "/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testXmlRoberta() throws Exception {
		driver.get(baseUrl + "/xmlTest/index.html");
		driver.findElement(By.id("test1")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
		driver.findElement(By.id("test2")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
		driver.findElement(By.id("test3")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
		driver.findElement(By.id("test4")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
		driver.findElement(By.id("test5")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
		driver.findElement(By.id("test6")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
		driver.findElement(By.id("test7")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
		driver.findElement(By.id("test8")).click();
		assertEquals("asExpected", driver.findElement(By.id("result"))
				.getText());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
		server.stop();
	}
}
