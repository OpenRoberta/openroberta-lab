package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public class Ev3DevFactory extends AbstractEV3Factory {

    public Ev3DevFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

}
