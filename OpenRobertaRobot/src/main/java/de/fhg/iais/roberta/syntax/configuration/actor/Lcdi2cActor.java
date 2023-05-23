package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LCDI2C", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_lcdi2c"})
public final class Lcdi2cActor extends ConfigurationComponent {
    private Lcdi2cActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
