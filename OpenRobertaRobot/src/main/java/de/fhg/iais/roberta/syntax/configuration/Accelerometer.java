package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ACCELEROMETER", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_accelerometer", "robConf_accelerometer"})
public final class Accelerometer extends ConfigurationComponent {
    private Accelerometer() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
