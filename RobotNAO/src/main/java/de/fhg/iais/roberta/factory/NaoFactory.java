package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.sensor.DetectFaceSensorMode;
import de.fhg.iais.roberta.mode.sensor.DetectMarkSensorMode;
import de.fhg.iais.roberta.util.PluginProperties;

public class NaoFactory extends RobotFactory {

    public NaoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    public IMode getDetectMarkMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, DetectMarkSensorMode.class);
    }

    public IMode getDetectFaceMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, DetectFaceSensorMode.class);
    }
}
