package de.fhg.iais.roberta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ValidationFileAssert extends AbstractAssert<ValidationFileAssert, String> {

    public static final Path DEFAULT_VALIDATION_DIRECTORY = Paths.get("src/test/resources");
    public static final Path DEFAULT_OUTPUT_DIRECTORY = Paths.get("target/unitTests");

    public static Path VALIDATION_DIRECTORY = DEFAULT_VALIDATION_DIRECTORY;
    public static Path OUTPUT_DIRECTORY = DEFAULT_OUTPUT_DIRECTORY;

    public static final String HEADER_LINE = "<-- This file was automatically generated, if the content is alright, remove this line -->";
    public static final String DEFAULT_MASK = "<MASK>";

    private String actualContent;
    private final String mask;

    public ValidationFileAssert(String content) {
        super(content, ValidationFileAssert.class);
        this.actualContent = content;
        this.mask = DEFAULT_MASK;
    }

    private ValidationFileAssert(String actualContent, String mask) {
        super(actualContent, ValidationFileAssert.class);
        this.mask = mask;
        this.actualContent = actualContent;
    }

    public static ValidationFileAssert assertThat(String content) {
        return new ValidationFileAssert(content);
    }

    public ValidationFileAssert isEqualToValidationFile(String filename) {
        return isEqualToValidationFile(Paths.get(filename));
    }

    public ValidationFileAssert mask(String contentToMask, String maskWith) {
        this.actualContent = this.actualContent.replace(contentToMask, maskWith);
        return this;
    }

    public ValidationFileAssert mask(String... contentToMask) {
        for ( String toMask : contentToMask ) {
            this.actualContent = this.actualContent.replace(toMask, this.mask);
        }
        return this;
    }

    public ValidationFileAssert isEqualToValidationFile(Path filename) {
        Path validationFile = VALIDATION_DIRECTORY.resolve(filename);
        Path outputFile = OUTPUT_DIRECTORY.resolve(filename);

        try {
            createDirectoryIfNotExists(validationFile.getParent());
            createDirectoryIfNotExists(outputFile.getParent());

            writeValidationFileIfNecessary(validationFile);
            writeOutputFile(outputFile);
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

        Assertions.assertThat(outputFile).hasSameTextualContentAs(validationFile);

        return this;
    }

    private void writeOutputFile(Path outputFile) throws IOException {
        Files.write(outputFile, this.actualContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void writeValidationFileIfNecessary(Path validationFile) throws IOException {
        if ( !Files.exists(validationFile) ) {
            String content = generatePlaceHolderValidationFile();
            Files.write(validationFile, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        }
    }

    private String generatePlaceHolderValidationFile() {
        return String.format("%s\n%s", HEADER_LINE, this.actualContent);
    }

    private void createDirectoryIfNotExists(Path validationDirectory) throws IOException {
        if ( !Files.exists(validationDirectory) ) {
            Files.createDirectories(validationDirectory);
        }
    }

    public static class ValidationFileAssertWithFilename extends ValidationFileAssert {

        public static final String DEFAULT_SUFFIX = "txt";
        private final String prefix;

        private final String suffix;
        private ValidationFileAssertWithFilename(String content, String mask, String prefix, String suffix) {
            super(content, mask);
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public ValidationFileAssertWithFilename mask(String contentToMask, String maskWith) {
            super.mask(contentToMask, maskWith);
            return this;
        }

        @Override
        public ValidationFileAssertWithFilename mask(String... contentToMask) {
            super.mask(contentToMask);
            return this;
        }

        public ValidationFileAssertWithFilename isEqualToValidationFile() {
            return isEqualToValidationFile(suffix);
        }

        @Override
        public ValidationFileAssertWithFilename isEqualToValidationFile(String suffix) {
            if ( suffix.trim().isEmpty() ) {
                super.isEqualToValidationFile(String.format("%s.%s", this.prefix, DEFAULT_SUFFIX));
                return this;
            }
            if ( !suffix.contains(".") ) {
                super.isEqualToValidationFile(String.format("%s.%s", this.prefix, suffix));
                return this;
            }
            super.isEqualToValidationFile(this.prefix + suffix);
            return this;
        }

    }
    public static class Generator {

        private final String mask;

        private final String filenamePrefix;
        private final String suffix;
        public Generator(String mask, String filenamePrefix) {
            this.mask = mask;
            this.filenamePrefix = filenamePrefix;
            this.suffix = ValidationFileAssertWithFilename.DEFAULT_SUFFIX;
        }

        public Generator(String mask, String filenamePrefix, String suffix) {
            this.mask = mask;
            this.filenamePrefix = filenamePrefix;
            this.suffix = suffix;
        }

        public Generator(String mask) {
            this.mask = mask;
            this.filenamePrefix = "";
            this.suffix = ValidationFileAssertWithFilename.DEFAULT_SUFFIX;
        }

        public ValidationFileAssertWithFilename generateValidationFileAssert(String content) {
            return new ValidationFileAssertWithFilename(content, mask, filenamePrefix, suffix);
        }

    }

    public static void resetDirectories() {
        VALIDATION_DIRECTORY = DEFAULT_VALIDATION_DIRECTORY;
        OUTPUT_DIRECTORY = DEFAULT_OUTPUT_DIRECTORY;
    }
}
