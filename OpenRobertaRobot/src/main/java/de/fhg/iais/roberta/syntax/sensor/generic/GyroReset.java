package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "GYRO_RESET", category = "SENSOR", blocklyNames = {"robSensors_gyro_reset"})
public final class GyroReset extends Sensor {

    @NepoField(name = "SENSORPORT")
    public final String sensorPort;

    public GyroReset(BlocklyProperties properties, String sensorPort) {
        super(properties);
        this.sensorPort = sensorPort;
        setReadOnly();
    }
}
