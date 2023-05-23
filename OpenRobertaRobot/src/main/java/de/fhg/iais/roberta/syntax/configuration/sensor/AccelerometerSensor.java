package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ACCELEROMETER", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_accelerometer", "robConf_accelerometer"})
public final class AccelerometerSensor extends ConfigurationComponent {
    private AccelerometerSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
