package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MBUILD_ULTRASONIC2", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_mbuild_ultrasonic2"})
public final class MbuildUltrasonic2Sensor extends ConfigurationComponent {
    private MbuildUltrasonic2Sensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
