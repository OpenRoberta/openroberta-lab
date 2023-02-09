package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TIMER_RESET", category = "SENSOR", blocklyNames = {"mbedSensors_timer_reset", "robSensors_timer_reset"})
public final class TimerReset extends Sensor {

    @NepoField(name = "SENSORPORT")
    public final String sensorPort;


    public TimerReset(BlocklyProperties properties, String sensorPort) {
        super(properties);
        this.sensorPort = sensorPort;
        setReadOnly();
    }
}
