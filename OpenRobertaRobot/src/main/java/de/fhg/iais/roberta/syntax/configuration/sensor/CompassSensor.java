package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "COMPASS", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_compass", "robConf_compass"})
public final class CompassSensor extends ConfigurationComponent {
    private CompassSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
