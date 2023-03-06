package de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "PIN_SET_TOUCH_MODE", category = "SENSOR", blocklyNames = {"robSensors_set_pin_mode"})
public final class PinSetTouchMode extends Sensor {

    @NepoField(name = "MODE")
    public final String mode;
    
    @NepoField(name = "SENSORPORT")
    public final String sensorport;

    public PinSetTouchMode(BlocklyProperties properties, String mode, String sensorport) {
        super(properties);
        this.mode = mode;
        this.sensorport = sensorport;
        setReadOnly();
    }
}
