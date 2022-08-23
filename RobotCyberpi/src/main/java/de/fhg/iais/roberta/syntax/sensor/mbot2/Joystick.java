package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "JOYSTICK_SENSING", category = "SENSOR", blocklyNames = {"robSensors_joystickKeys_getSample"},
    sampleValues = {@F2M(field = "JOYSTICK_PRESSED", mode = "PRESSED")})
public final class Joystick extends ExternalSensor {

    public Joystick(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
