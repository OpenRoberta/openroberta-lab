package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "GYRO", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_gyro", "robBrick_gyro"})
public final class Gyro extends ConfigurationComponent {
    private Gyro() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
