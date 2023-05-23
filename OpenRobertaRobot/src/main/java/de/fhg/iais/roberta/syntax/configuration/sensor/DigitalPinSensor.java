package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DIGITAL_PIN", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_digitalout"})
public final class DigitalPinSensor extends ConfigurationComponent {
    private DigitalPinSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
