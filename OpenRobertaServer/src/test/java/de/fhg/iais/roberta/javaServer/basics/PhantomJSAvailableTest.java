package de.fhg.iais.roberta.javaServer.basics;

import org.junit.Test;
import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Checks whether phantomjs is available on your local system PATH.
 * Maven downloads phantomjs for JavaScrip browser testing and makes the tool
 * available during phase: test, integration-test of your build.
 */
public class PhantomJSAvailableTest {

  @Test
  public void shouldHavePhantomJsBinary() {
    String binary = System.getProperty("phantomjs.binary");
    assertNotNull(binary);
    assertTrue(new File(binary).exists());
  }

}