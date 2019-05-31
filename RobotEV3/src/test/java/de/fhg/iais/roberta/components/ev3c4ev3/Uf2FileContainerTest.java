package de.fhg.iais.roberta.components.ev3c4ev3;

import org.junit.Test;

import java.io.File;

import static de.fhg.iais.roberta.components.ev3c4ev3.ResourceUtils.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Uf2FileContainerTest {

    @Test
    public void testAddFile() throws Exception {
        Uf2FileContainer container = new Uf2FileContainer();
        File file1 = getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_1.txt");

        container.add(file1, "file_1.txt");
        byte[] uf2File = container.toByteArray();

        byte[] expectedUf2File = getFileFromResourcesAsByteArray("/components/ev3c4ev3/uf2_with_file_1.uf2");
        assertArrayEquals(expectedUf2File, uf2File);
    }

    @Test
    public void testAddTwoFiles() throws Exception  {
        Uf2FileContainer container = new Uf2FileContainer();
        File file1 = getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_1.txt");
        File file2 = getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_2.txt");

        container.add(file1, "file_1.txt");
        container.add(file2, "file_2.txt");
        byte[] uf2File = container.toByteArray();

        byte[] expectedUf2File = getFileFromResourcesAsByteArray("/components/ev3c4ev3/uf2_with_file_1_and_2.uf2");
        assertArrayEquals(expectedUf2File, uf2File);
    }

    @Test
    public void testToBase64  () throws Exception  {
        Uf2FileContainer container = new Uf2FileContainer();
        File file1 = getFileFromResources("/components/ev3c4ev3/uf2_file_to_add_1.txt");

        container.add(file1, "file_1.txt");
        String uf2File = container.toBase64();

        String expectedUf2File = getFileFromResourcesAsString("/components/ev3c4ev3/uf2_with_file_1_base64.txt");
        assertEquals(expectedUf2File, uf2File);
    }

}
