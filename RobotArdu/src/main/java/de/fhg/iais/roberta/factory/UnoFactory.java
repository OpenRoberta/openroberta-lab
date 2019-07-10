package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.validate.AbstractConfigurationValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.ArduinoConfigurationValidatorVisitor;

public class UnoFactory extends AbstractArduinoFactory {

    public UnoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public AbstractConfigurationValidatorVisitor getRobotConfigurationCheckVisitor(Configuration configuration) {
        return new ArduinoConfigurationValidatorVisitor(configuration);
    }
}