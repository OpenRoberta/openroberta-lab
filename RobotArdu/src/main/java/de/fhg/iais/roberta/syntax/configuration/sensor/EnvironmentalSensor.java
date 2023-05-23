package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ENVIRONMENTAL", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_environmental"})
public final class EnvironmentalSensor extends ConfigurationComponent {
    private EnvironmentalSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
