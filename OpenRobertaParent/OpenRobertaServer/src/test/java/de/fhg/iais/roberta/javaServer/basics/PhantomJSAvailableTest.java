package de.fhg.iais.roberta.javaServer.basics;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Checks whether phantomjs is available on your local system PATH. Maven downloads phantomjs for JavaScrip browser testing and makes the tool available during
 * phase: test, integration-test of your build.
 */
public class PhantomJSAvailableTest {

    @Test
    @Ignore
    public void shouldHavePhantomJsBinary() {

        String binary = System.getProperty("phantomjs.binary");
        assertNotNull(binary);
        assertTrue(new File(binary).exists());
    }

}