package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DETECT_MARK", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"naoSensors_naoMark"})
public final class DetectMarkSensor extends ConfigurationComponent {
    private DetectMarkSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
