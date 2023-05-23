package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LED", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_led"})
public final class LedActor extends ConfigurationComponent {
    private LedActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
