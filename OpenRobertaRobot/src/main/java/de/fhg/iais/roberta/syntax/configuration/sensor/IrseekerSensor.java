package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "IRSEEKER", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_irseeker"})
public final class IrseekerSensor extends ConfigurationComponent {
    private IrseekerSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
