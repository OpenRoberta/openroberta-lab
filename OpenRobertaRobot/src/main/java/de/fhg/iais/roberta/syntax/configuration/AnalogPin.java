package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ANALOG_PIN", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robActions_write_to_pin", "robConf_analogout"})
public final class AnalogPin extends ConfigurationComponent {
    private AnalogPin() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
