package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "TXT_IMU", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_imu"})
public final class IMUSensor extends ConfigurationComponent {
    private IMUSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
