package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LEDBAR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_ledbar"})
public final class Ledbar extends ConfigurationComponent {
    private Ledbar() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
