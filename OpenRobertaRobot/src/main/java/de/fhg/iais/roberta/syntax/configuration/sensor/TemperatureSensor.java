package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "TEMPERATURE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_temperature", "robBrick_temperature"})
public final class TemperatureSensor extends ConfigurationComponent {
    private TemperatureSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
