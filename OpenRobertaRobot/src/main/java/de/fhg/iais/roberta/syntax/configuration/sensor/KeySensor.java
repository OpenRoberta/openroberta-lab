package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "KEY", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_key", "robBrick_key"})
public final class KeySensor extends ConfigurationComponent {
    private KeySensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
