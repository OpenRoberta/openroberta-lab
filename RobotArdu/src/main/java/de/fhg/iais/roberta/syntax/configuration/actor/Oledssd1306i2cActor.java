package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "OLEDSSD1306I2C", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_oledssd1306i2c"})
public final class Oledssd1306i2cActor extends ConfigurationComponent {
    private Oledssd1306i2cActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
