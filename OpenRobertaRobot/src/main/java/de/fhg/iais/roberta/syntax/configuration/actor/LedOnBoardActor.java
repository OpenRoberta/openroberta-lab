package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LED_ON_BOARD", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"makeblockActions_leds_on"})
public final class LedOnBoardActor extends ConfigurationComponent {
    private LedOnBoardActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
