package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "WEDO", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_WeDo-Brick"})
public final class Wedo extends ConfigurationComponent {
    private Wedo() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
