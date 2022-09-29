package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LED_ON_BOARD", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"makeblockActions_leds_on"})
public final class LedOnBoard extends ConfigurationComponent {
    private LedOnBoard() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
