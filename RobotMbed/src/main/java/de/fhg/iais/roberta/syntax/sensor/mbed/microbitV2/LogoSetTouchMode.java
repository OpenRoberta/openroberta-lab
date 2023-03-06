package de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "LOGO_SET_TOUCH_MODE", category = "SENSOR", blocklyNames = {"robSensors_set_logo_mode"})
public final class LogoSetTouchMode extends Sensor {

    @NepoField(name = "MODE")
    public final String mode;

    @NepoField(name = "SENSORPORT")
    public final String port;

    public LogoSetTouchMode(BlocklyProperties properties, String mode, String port) {
        super(properties);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }
}
