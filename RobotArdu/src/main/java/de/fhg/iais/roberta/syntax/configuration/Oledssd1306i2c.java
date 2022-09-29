package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "OLEDSSD1306I2C", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_oledssd1306i2c"})
public final class Oledssd1306i2c extends ConfigurationComponent {
    private Oledssd1306i2c() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
