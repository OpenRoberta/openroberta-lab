package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public abstract class AbstractArduinoFactory extends AbstractRobotFactory {
    public AbstractArduinoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }
}
