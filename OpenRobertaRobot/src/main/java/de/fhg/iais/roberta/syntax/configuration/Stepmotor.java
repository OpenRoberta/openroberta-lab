package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "STEPMOTOR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_stepmotor"})
public final class Stepmotor extends ConfigurationComponent {
    private Stepmotor() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
