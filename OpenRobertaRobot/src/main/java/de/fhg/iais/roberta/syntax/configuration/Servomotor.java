package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SERVOMOTOR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_servo"})
public final class Servomotor extends ConfigurationComponent {
    private Servomotor() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
