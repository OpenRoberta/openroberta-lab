package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "PULSE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_pulse", "robBrick_pulse"})
public final class PulseSensor extends ConfigurationComponent {
    private PulseSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
