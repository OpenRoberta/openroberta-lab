package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "DIGITAL_INPUT", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_digitalin"})
public class DigitalInput extends ConfigurationComponent {
    private DigitalInput() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
