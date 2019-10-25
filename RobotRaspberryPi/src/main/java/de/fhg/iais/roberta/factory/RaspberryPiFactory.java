package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public class RaspberryPiFactory extends RobotFactory {

    public RaspberryPiFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    public void setRaspiIP(String raspiProperty, String raspiIP) {
        this.pluginProperties.getPluginProperties().setProperty(raspiProperty, raspiIP);
    }
}
