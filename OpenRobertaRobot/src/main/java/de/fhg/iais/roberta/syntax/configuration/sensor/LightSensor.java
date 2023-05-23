package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LIGHT", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_light", "robBrick_light"})
public final class LightSensor extends ConfigurationComponent {
    private LightSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
