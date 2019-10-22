package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public class MicrobitFactory extends AbstractMbedFactory {
    public MicrobitFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "py";
    }
}
