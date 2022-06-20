package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "COMPASS", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_compass", "robConf_compass"})
public class Compass extends ConfigurationComponent {
    private Compass() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
