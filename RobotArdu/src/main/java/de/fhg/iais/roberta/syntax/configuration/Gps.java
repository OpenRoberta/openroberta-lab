package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "GPS", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_gps"})
public class Gps extends ConfigurationComponent {
    private Gps() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
