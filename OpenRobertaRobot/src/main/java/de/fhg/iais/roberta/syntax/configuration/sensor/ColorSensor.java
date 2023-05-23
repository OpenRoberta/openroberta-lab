package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "COLOR", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_colour"})
public final class ColorSensor extends ConfigurationComponent {
    private ColorSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
