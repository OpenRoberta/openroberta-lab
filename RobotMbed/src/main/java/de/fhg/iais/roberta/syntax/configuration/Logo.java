package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LOGOTOUCH", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_logotouch"})
public final class Logo extends ConfigurationComponent {
    private Logo() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
