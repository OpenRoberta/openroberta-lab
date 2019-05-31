package de.fhg.iais.roberta.components.ev3c4ev3;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ResourceUtils {
    public static File getFileFromResources(String fileName) throws Exception {
        URL url = ResourceUtils.class.getResource(fileName);
        return new File(url.getFile());
    }

    public static byte[] getFileFromResourcesAsByteArray(String fileName) throws Exception {
        File file = getFileFromResources(fileName);
        return FileUtils.readFileToByteArray(file);
    }

    public static String getFileFromResourcesAsString(String fileName) throws Exception {
        File file = getFileFromResources(fileName);
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}
