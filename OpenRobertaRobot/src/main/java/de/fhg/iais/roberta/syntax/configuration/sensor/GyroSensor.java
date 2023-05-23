package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "GYRO", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_gyro", "robBrick_gyro"})
public final class GyroSensor extends ConfigurationComponent {
    private GyroSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
