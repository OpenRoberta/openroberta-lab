package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "RELAY", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_relay"})
public final class Relay extends ConfigurationComponent {
    private Relay() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
