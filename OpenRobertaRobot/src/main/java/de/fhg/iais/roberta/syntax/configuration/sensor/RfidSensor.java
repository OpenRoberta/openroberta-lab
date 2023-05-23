package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "RFID", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_rfid", "robBrick_rfide"})
public final class RfidSensor extends ConfigurationComponent {
    private RfidSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
