package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LCDI2C", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_lcdi2c"})
public final class Lcdi2c extends ConfigurationComponent {
    private Lcdi2c() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
