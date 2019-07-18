package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.Configuration;

public abstract class AbstractConfigurationValidatorVisitor implements IValidatorVisitor<Void> {
    protected int errorCount = 0;
    protected int warningCount = 0;
    protected Configuration robotConfiguration;

    public AbstractConfigurationValidatorVisitor(Configuration configuration) {
        this.robotConfiguration = configuration;
    }

    public abstract void checkConfiguration();

    public abstract String getIncorrectPin();

    public abstract String getFailingBlock();
}
