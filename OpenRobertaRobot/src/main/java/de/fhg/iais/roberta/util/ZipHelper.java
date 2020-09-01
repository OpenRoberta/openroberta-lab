package de.fhg.iais.roberta.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipHelper {

    private static final int BUFFER_SIZE = 1024;

    private ZipHelper() {
    }

    /**
     * Zips the specified files.
     *
     * @param inputFiles the files to be zipped
     * @param outputFile the zipped output file
     * @throws IOException when something goes wrong
     */
    public static void zipFiles(Iterable<? extends Path> inputFiles, Path outputFile) throws IOException {
        if (Files.exists(outputFile)) {
            throw new IllegalArgumentException("File already exists!");
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile.toFile());
            ZipOutputStream zos = new ZipOutputStream(fos)) {
            for ( Path path : inputFiles ) {
                File srcFile = path.toFile();
                try (FileInputStream fis = new FileInputStream(srcFile)) {
                    ZipEntry zipEntry = new ZipEntry(srcFile.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[BUFFER_SIZE];
                    int length;
                    while ( (length = fis.read(bytes)) >= 0 ) {
                        zos.write(bytes, 0, length);
                    }
                }
            }
        }
    }

    /**
     * Unzips the specified file into the given directory.
     *
     * @param inputFile the zip file to be unpacked
     * @param outputDir the destination directory for the files
     * @throws IOException when something goes wrong
     */
    public static void unzipFiles(Path inputFile, Path outputDir) throws IOException {
        if (!Files.isRegularFile(inputFile)) {
            throw new IllegalArgumentException("inputFile is not a file!");
        }
        if (!Files.isDirectory(outputDir)) {
            throw new IllegalArgumentException("outputDir is not a directory!");
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(inputFile.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while ( zipEntry != null ) {
                File newFile = new File(outputDir.toFile(), zipEntry.getName());
                try(FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ( (len = zis.read(buffer)) > 0 ) {
                        fos.write(buffer, 0, len);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }
}
