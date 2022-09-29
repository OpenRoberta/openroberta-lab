package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ANALOG_INPUT", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_analogin"})
public final class AnalogInput extends ConfigurationComponent {
    private AnalogInput() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
