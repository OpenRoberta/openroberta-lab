package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public abstract class AbstractMbedFactory extends AbstractRobotFactory {

    public AbstractMbedFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "cpp";
    }
}