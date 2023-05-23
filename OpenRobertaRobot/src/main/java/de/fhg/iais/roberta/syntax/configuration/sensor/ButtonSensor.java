package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "BUTTON", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_key_"})
public final class ButtonSensor extends ConfigurationComponent {
    private ButtonSensor() {
        super(null, "SENSOR", null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
