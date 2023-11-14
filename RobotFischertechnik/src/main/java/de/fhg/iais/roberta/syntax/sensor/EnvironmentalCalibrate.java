package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "ENCODER_RESET", category = "SENSOR", blocklyNames = {"robSensors_environmental_calibrate"})
public final class EnvironmentalCalibrate extends Sensor implements WithUserDefinedPort {

    @NepoField(name = "SENSORPORT")
    public final String sensorPort;

    public EnvironmentalCalibrate(BlocklyProperties properties, String sensorPort) {
        super(properties);
        this.sensorPort = sensorPort;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.sensorPort;
    }
}
