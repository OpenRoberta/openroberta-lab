package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "BUTTON", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_key_"})
public class Button extends ConfigurationComponent {
    private Button() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
