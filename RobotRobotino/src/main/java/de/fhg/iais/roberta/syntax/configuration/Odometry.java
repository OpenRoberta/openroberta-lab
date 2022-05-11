package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ODOMETRY", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_odometry"})
public final class Odometry extends ConfigurationComponent {
    private Odometry() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}