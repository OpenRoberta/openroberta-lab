package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MOTOR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_motor"})
public final class Motor extends ConfigurationComponent {
    private Motor() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
