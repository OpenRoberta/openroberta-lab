package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ULTRASONIC", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_ultrasonic", "robConf_ultrasonicc", "robBrick_ultrasonic"})
public final class UltrasonicSensor extends ConfigurationComponent {
    private UltrasonicSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
