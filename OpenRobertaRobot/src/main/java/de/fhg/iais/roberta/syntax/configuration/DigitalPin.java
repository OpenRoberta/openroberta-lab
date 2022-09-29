package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DIGITAL_PIN", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_digitalout"})
public final class DigitalPin extends ConfigurationComponent {
    private DigitalPin() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
