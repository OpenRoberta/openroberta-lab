package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MEDIUM", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_motor_middle"})
public final class Medium extends ConfigurationComponent {
    private Medium() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
