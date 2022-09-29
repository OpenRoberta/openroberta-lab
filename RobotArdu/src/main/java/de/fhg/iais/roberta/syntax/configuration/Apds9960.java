package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "APDS9960", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_apds9960"})
public final class Apds9960 extends ConfigurationComponent {
    private Apds9960() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
