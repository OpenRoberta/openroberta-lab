package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "I2C_BUS", category = "CONFIGURATION_BUS",
    blocklyNames = {"robConf_i2c_bus"})
public final class I2cBus extends ConfigurationComponent {
    private I2cBus() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
