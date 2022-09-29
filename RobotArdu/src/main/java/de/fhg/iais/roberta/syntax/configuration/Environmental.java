package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ENVIRONMENTAL", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_environmental"})
public final class Environmental extends ConfigurationComponent {
    private Environmental() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
