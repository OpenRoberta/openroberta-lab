package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "ROBOT", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_robot"})
public class Robot extends ConfigurationComponent {
    private Robot() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
