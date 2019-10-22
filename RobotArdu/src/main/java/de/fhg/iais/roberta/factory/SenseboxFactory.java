package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public class SenseboxFactory extends AbstractRobotFactory {

    public SenseboxFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }
}
