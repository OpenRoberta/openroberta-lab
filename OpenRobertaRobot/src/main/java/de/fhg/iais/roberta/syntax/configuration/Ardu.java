package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ARDU", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_motor_ardu"})
public final class Ardu extends ConfigurationComponent {
    private Ardu() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
