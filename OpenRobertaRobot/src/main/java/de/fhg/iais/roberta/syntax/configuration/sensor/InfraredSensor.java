package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "INFRARED", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_infrared", "robBrick_infrared"})
public final class InfraredSensor extends ConfigurationComponent {
    private InfraredSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
