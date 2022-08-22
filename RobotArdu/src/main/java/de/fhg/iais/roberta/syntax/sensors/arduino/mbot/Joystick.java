package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "ARDU_JOYSTICK_GETSAMPLE", category = "SENSOR", blocklyNames = {"robSensors_joystick_getSample"})
@NepoExternalSensor
public final class Joystick extends ExternalSensor {
    public Joystick(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
