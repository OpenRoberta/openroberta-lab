package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.Configuration;

public abstract class AbstractConfigurationValidatorVisitor {
    protected int errorCount = 0;
    protected int warningCount = 0;
    protected Configuration robotConfiguration;

    public AbstractConfigurationValidatorVisitor(Configuration configuration) {
        robotConfiguration = configuration;
    }

    public abstract void checkConfiguration();
}
