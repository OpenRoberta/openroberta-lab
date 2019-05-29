package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;

public class Ev3C4ev3Factory extends AbstractEV3Factory {
    public Ev3C4ev3Factory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    //  TODO: override getRobotCompilerWorkflow

    @Override
    public String getFileExtension() {
        return "c";
    }
}
