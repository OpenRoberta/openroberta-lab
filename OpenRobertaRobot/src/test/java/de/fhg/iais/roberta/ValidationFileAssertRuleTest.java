package de.fhg.iais.roberta;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class ValidationFileAssertRuleTest {
    @Rule
    public ValidationFileAssertRule validationFileAssertRule = new ValidationFileAssertRule("","#MASK");

    @Rule
    public ValidationFileAssertRule validationFileAssertRuleWithSuffix = new ValidationFileAssertRule("txt","#MASK");

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
    public void choosesCorrectFilename() {
        String content = "test";
        Assertions.assertThatThrownBy(() -> {
            validationFileAssertRule.assertThat(content).isEqualToValidationFile(".txt");
        })
            .isInstanceOf(AssertionError.class);

        Assertions.assertThat(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesCorrectFilename.txt"))
            .hasContent(ValidationFileAssert.HEADER_LINE + "\n" + content);
        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesCorrectFilename.txt"))
            .hasContent(content);
    }

    @Test
    public void choosesDefaultSuffix() {
        String content = "test";
        Assertions.assertThatThrownBy(() -> {
            validationFileAssertRule.assertThat(content).isEqualToValidationFile();
        })
            .isInstanceOf(AssertionError.class);

        Assertions.assertThat(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesDefaultSuffix.txt"))
            .hasContent(ValidationFileAssert.HEADER_LINE + "\n" + content);
        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesDefaultSuffix.txt"))
            .hasContent(content);
    }

    @Test
    public void choosesCorrectFilenameWithoutDot() {
        String content = "test";
        Assertions.assertThatThrownBy(() -> {
            validationFileAssertRule.assertThat(content).isEqualToValidationFile("txt");
        })
            .isInstanceOf(AssertionError.class);

        Assertions.assertThat(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesCorrectFilenameWithoutDot.txt"))
            .hasContent(ValidationFileAssert.HEADER_LINE + "\n" + content);
        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesCorrectFilenameWithoutDot.txt"))
            .hasContent(content);
    }

    @Test
    public void choosesCorrectFilenameWithSuffix() {
        String content = "test";
        Assertions.assertThatThrownBy(() -> {
            validationFileAssertRuleWithSuffix.assertThat(content).isEqualToValidationFile();
        })
            .isInstanceOf(AssertionError.class);

        Assertions.assertThat(ValidationFileAssert.VALIDATION_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesCorrectFilenameWithSuffix.txt"))
            .hasContent(ValidationFileAssert.HEADER_LINE + "\n" + content);
        Assertions.assertThat(ValidationFileAssert.OUTPUT_DIRECTORY.resolve(getClass().getSimpleName() + "-choosesCorrectFilenameWithSuffix.txt"))
            .hasContent(content);
    }
}
