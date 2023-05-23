package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MOISTURE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_moisture", "robBrick_moisture"})
public final class MoistureSensor extends ConfigurationComponent {
    private MoistureSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
