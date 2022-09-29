package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "RGBLED", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_rgbled"})
public final class Rgbled extends ConfigurationComponent {
    private Rgbled() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
