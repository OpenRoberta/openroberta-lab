package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LED", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_led"})
public final class Led extends ConfigurationComponent {
    private Led() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
