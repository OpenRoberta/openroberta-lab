package de.fhg.iais.roberta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

public class ValidationFileAssertTest {

    @After
    public void tearDown() throws Exception {
        List<Path> files = Files.list(ValidationFileAssert.VALIDATION_DIRECTORY)
            .filter(s -> s.getFileName().toString().startsWith(getClass().getSimpleName()))
            .collect(Collectors.toList());
        for ( Path file : files ) {
            Files.delete(file);
        }
    }

    @Test
    public void createsValidationFileIfNotExists() {
        String filename = "ValidationFileAssertTest-createValidationFileIfNotExists.txt";
        String content = "Test";
        Assertions.assertThatThrownBy(() -> {
            ValidationFileAssert.assertThat(content)
                .isEqualToValidationFile(Paths.get(filename));
        })
            .isInstanceOf(AssertionError.class);

        Assertions.assertThat(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(filename).toFile())
            .exists()
            .hasContent("<-- This file was automatically generated, if the content is alright, remove this line -->\n" + content);

        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(filename).toFile())
            .exists()
            .hasContent(content);
    }

    @Test
    public void doesNotThrowIfEquals() throws IOException {
        String filename = "ValidationFileAssertTest-doesNotThrowIfEquals.txt";
        String content = "Test";

        Files.write(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(filename), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

        Assertions.assertThatCode(() -> {
            ValidationFileAssert.assertThat(content)
                .isEqualToValidationFile(Paths.get(filename));
        })
            .doesNotThrowAnyException();

        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(filename).toFile())
            .exists()
            .hasContent(content);
    }

    @Test
    public void doesNotThrowWithMasking() throws IOException {
        String filename = "ValidationFileAssertTest-doesNotThrowWithMasking.txt";
        String content = "Test123";
        String expectedContent = content.replaceAll("123", ValidationFileAssert.DEFAULT_MASK);

        Files.write(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(filename), expectedContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

        Assertions.assertThatCode(() -> {
            ValidationFileAssert.assertThat(content)
                .mask("123")
                .isEqualToValidationFile(Paths.get(filename));
        })
            .doesNotThrowAnyException();

        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(filename).toFile())
            .exists()
            .hasContent(expectedContent);
    }

    @Test
    public void doesNotThrowWithCustomMasking() throws IOException {
        String filename = "ValidationFileAssertTest-doesNotThrowWithCustomMasking.txt";
        String content = "Test123";
        String customMask = "MASK321";
        String expectedContent = content.replaceAll("123", customMask);

        Files.write(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(filename), expectedContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        ValidationFileAssert.Generator generator = new ValidationFileAssert.Generator(customMask);
        Assertions.assertThatCode(() -> {
            generator.generateValidationFileAssert(content)
                .mask("123")
                .isEqualToValidationFile(Paths.get(filename));
        })
            .doesNotThrowAnyException();

        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(filename).toFile())
            .exists()
            .hasContent(expectedContent);
    }

    @Test
    public void doesNotThrowWithCustomMaskingOnce() throws IOException {
        String filename = "ValidationFileAssertTest-doesNotThrowWithCustomMaskingOnce.txt";
        String content = "Test123";
        String customMask = "MASK1000";
        String expectedContent = content.replaceAll("123", customMask);

        Files.write(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(filename), expectedContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        Assertions.assertThatCode(() -> {
            ValidationFileAssert.assertThat(content)
                .mask("123", "MASK1000")
                .isEqualToValidationFile(Paths.get(filename));
        })
            .doesNotThrowAnyException();

        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(filename).toFile())
            .exists()
            .hasContent(expectedContent);
    }

    @Test
    public void doesThrowWithMasking() throws IOException {
        String filename = "ValidationFileAssertTest-doesThrowWithMasking.txt";
        String content = "Test123";
        String expectedContent = content.replaceAll("123", ValidationFileAssert.DEFAULT_MASK);

        Files.write(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(filename), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

        Assertions.assertThatCode(() -> {
            ValidationFileAssert.assertThat(content)
                .mask("123")
                .isEqualToValidationFile(Paths.get(filename));
        })
            .isInstanceOf(AssertionError.class);

        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(filename).toFile())
            .exists()
            .hasContent(expectedContent);
    }
}
