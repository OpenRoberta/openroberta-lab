package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DROP", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_drop", "robBrick_drop"})
public final class Drop extends ConfigurationComponent {
    private Drop() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
