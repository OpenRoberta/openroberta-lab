package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "TOUCH", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_touch", "robConf_touch"})
public final class TouchSensor extends ConfigurationComponent {
    private TouchSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
