package de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "LOGO_SET_TOUCH_MODE", category = "SENSOR", blocklyNames = {"robSensors_set_logo_mode"})
public final class LogoSetTouchMode extends Sensor implements WithUserDefinedPort {

    @NepoField(name = "MODE")
    public final String mode;

    @NepoHide
    public final Hide hide;

    public LogoSetTouchMode(BlocklyProperties properties, String mode, Hide hide) {
        super(properties);
        this.mode = mode;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.hide.getValue();
    }
}
