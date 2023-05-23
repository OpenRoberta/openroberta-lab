package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LSM9DS1", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_lsm9ds1"})
public final class Lsm9ds1Sensor extends ConfigurationComponent {
    private Lsm9ds1Sensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
