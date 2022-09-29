package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ENCODER", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_encoder", "robBrick_encoder"})
public final class Encoder extends ConfigurationComponent {
    private Encoder() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
