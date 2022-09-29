package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LED_MATRIX", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_led_matrix"})
public final class LedMatrix extends ConfigurationComponent {
    private LedMatrix() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
