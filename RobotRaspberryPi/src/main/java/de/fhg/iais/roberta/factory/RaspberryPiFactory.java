package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public class RaspberryPiFactory extends AbstractRobotFactory {

    public RaspberryPiFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

    public void setRaspiIP(String raspiProperty, String raspiIP) {
        this.pluginProperties.getPluginProperties().setProperty(raspiProperty, raspiIP);
    }
}
