package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "I2C", category = "CONFIGURATION_BUS",
    blocklyNames = {"robConf_i2c_port_txt4"})
public final class TxtI2C extends ConfigurationComponent {
    private TxtI2C() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
