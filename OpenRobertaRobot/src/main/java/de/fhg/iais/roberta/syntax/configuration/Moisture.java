package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "MOISTURE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_moisture", "robBrick_moisture"})
public class Moisture extends ConfigurationComponent {
    private Moisture() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
