package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "EXTERNAL_LED", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_led"})
public final class ExternalLed extends ConfigurationComponent {
    private ExternalLed() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
