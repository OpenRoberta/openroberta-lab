package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ENCODER", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_encoder", "robBrick_encoder"})
public final class EncoderSensor extends ConfigurationComponent {
    private EncoderSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
