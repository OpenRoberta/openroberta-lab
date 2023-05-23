package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LED_MATRIX", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robBrick_led_matrix"})
public final class LedMatrixActor extends ConfigurationComponent {
    private LedMatrixActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
