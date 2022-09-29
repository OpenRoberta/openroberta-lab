package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "COMPASS", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_compass", "robConf_compass"})
public final class Compass extends ConfigurationComponent {
    private Compass() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
