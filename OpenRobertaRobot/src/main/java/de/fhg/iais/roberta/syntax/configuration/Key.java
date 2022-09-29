package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "KEY", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_key", "robBrick_key"})
public final class Key extends ConfigurationComponent {
    private Key() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
