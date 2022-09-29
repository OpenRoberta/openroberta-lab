package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "WIRELESS", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_wireless"})
public final class Wireless extends ConfigurationComponent {
    private Wireless() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
