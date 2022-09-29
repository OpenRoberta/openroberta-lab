package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "CALLIBOT", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_callibot"})
public final class Callibot extends ConfigurationComponent {
    private Callibot() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
