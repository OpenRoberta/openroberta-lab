package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "HUMIDITY", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_humidity", "robBrick_humidity"})
public final class HumiditySensor extends ConfigurationComponent {
    private HumiditySensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
