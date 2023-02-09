package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "ENCODER_RESET", category = "SENSOR", blocklyNames = {"robSensors_encoder_reset"})
public final class EncoderReset extends Sensor {

    @NepoField(name = "SENSORPORT")
    public final String sensorPort;

    public EncoderReset(BlocklyProperties properties, String sensorPort) {
        super(properties);
        this.sensorPort = sensorPort;
        setReadOnly();
    }
}
