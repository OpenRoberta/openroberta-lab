package de.fhg.iais.roberta.components.ev3c4ev3;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Uf2FileContainerTest {

    @Test
    public void testAddFile() throws Exception {
        Uf2FileContainer container = new Uf2FileContainer();
        File file1 = this.getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_1.txt");

        container.add(file1, "file_1.txt");
        byte[] uf2File = container.toByteArray();

        byte[] expectedUf2File = this.getFileFromResourcesAsByteArray("/components/ev3c4ev3/uf2_with_file_1.uf2");
        assertTrue(Arrays.equals(expectedUf2File, uf2File));
    }

    @Test
    public void testAddTwoFiles() throws Exception  {
        Uf2FileContainer container = new Uf2FileContainer();
        File file1 = this.getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_1.txt");
        File file2 = this.getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_2.txt");

        container.add(file1, "file_1.txt");
        container.add(file2, "file_2.txt");
        byte[] uf2File = container.toByteArray();

        byte[] expectedUf2File = this.getFileFromResourcesAsByteArray("/components/ev3c4ev3/uf2_with_file_1_and_2.uf2");
        assertTrue(Arrays.equals(expectedUf2File, uf2File));
    }

    @Test
    public void testToBase64  () throws Exception  {
        Uf2FileContainer container = new Uf2FileContainer();
        File file1 = this.getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_1.txt");

        container.add(file1, "file_1.txt");
        String uf2File = container.toBase64();

        String expectedUf2File = this.getFileFromResourcesAsString("/components/ev3c4ev3/uf2_with_file_1_base64.txt");
        assertEquals(expectedUf2File, uf2File);
    }

    private File getFileFromResources(String fileName) throws Exception {
        URL url = Uf2FileContainerTest.class.getResource(fileName);
        return new File(url.getFile());
    }

    private byte[] getFileFromResourcesAsByteArray(String fileName) throws Exception {
        File file = getFileFromResources(fileName);
        return FileUtils.readFileToByteArray(file);
    }

    private String getFileFromResourcesAsString(String fileName) throws Exception {
        File file = getFileFromResources(fileName);
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}
