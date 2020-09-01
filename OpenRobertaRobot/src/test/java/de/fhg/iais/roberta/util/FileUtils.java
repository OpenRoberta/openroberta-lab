package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class FileUtils {
    /**
     * return a stream of all files found in a resource directory sorted using natural order<br>
     * <b>Note:</b> this is a method used in tests. This method itself is tested below ... .
     *
     * @param directory whose files are requested
     * @return the stream of all files
     */
    public static Stream<String> fileStreamOfResourceDirectory(String directory) {
        try {
            final String[] fileNameList = Paths.get(Util.class.getResource(directory).toURI()).toFile().list();
            Arrays.sort(fileNameList);
            return Arrays.stream(fileNameList);
        } catch ( URISyntaxException e ) {
            throw new DbcException("getting a file stream from a resource directory failed for: " + directory, e);
        }
    }

    @Test
    public void testJavaIdentifier() {
        assertTrue(Util.isValidJavaIdentifier("P"));
        assertTrue(Util.isValidJavaIdentifier("Pid"));
        assertTrue(Util.isValidJavaIdentifier("€Pid_diP€"));
        assertTrue(Util.isValidJavaIdentifier("_ö_ä_ü_ß_"));
        assertTrue(Util.isValidJavaIdentifier("ö_ä_ü_ß"));
        assertTrue(Util.isValidJavaIdentifier("__üäö$€"));
        assertFalse(Util.isValidJavaIdentifier(null));
        assertFalse(Util.isValidJavaIdentifier(""));
        assertFalse(Util.isValidJavaIdentifier("1qay"));
        assertFalse(Util.isValidJavaIdentifier(" Pid"));
        assertFalse(Util.isValidJavaIdentifier("class"));
        assertFalse(Util.isValidJavaIdentifier("for"));
    }

    @Test
    public void testLoadResource() {
        assertNotNull(FileUtils.fileStreamOfResourceDirectory("/yaml"));
        try {
            FileUtils.fileStreamOfResourceDirectory("yaml");
            fail("resource found and that is an error");
        } catch ( Exception e ) {
            // ok!
        }
        assertNotNull(Util.getInputStream(true, "classpath:/yaml/y1.yml"));
        try {
            Util.getInputStream(true, "classpath:yaml/y1.yml");
            fail("resource found and that is an error");
        } catch ( Exception e ) {
            // ok!
        }
    }

}
