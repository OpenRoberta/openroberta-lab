package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "TOUCH", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_touch", "robConf_touch"})
public final class Touch extends ConfigurationComponent {
    private Touch() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
