package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "LIGHT", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_light", "robBrick_light"})
public class Light extends ConfigurationComponent {
    private Light() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
