package de.fhg.iais.roberta;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class ValidationFileAssertRule extends TestWatcher {

    private String suffix = ValidationFileAssert.ValidationFileAssertWithFilename.DEFAULT_SUFFIX;
    private String mask = ValidationFileAssert.DEFAULT_MASK;
    private ValidationFileAssert.Generator generator;

    public ValidationFileAssertRule(String suffix) {
        this.suffix = suffix;
    }

    public ValidationFileAssertRule(String suffix, String mask ) {
        this.mask = mask;
        this.suffix = suffix;
    }


    public ValidationFileAssertRule() {
    }

    @Override
    protected void starting(Description description) {
        String filenamePrefix = String.format("%s-%s", description.getTestClass().getSimpleName(), description.getMethodName());
        this.generator = new ValidationFileAssert.Generator(mask, filenamePrefix, suffix);
    }

    public ValidationFileAssert.ValidationFileAssertWithFilename assertThat(String content) {
        return generator.generateValidationFileAssert(content);
    }
}
