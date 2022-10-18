package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "GESTURE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_gesture_getSample"}, sampleValues = {@F2M(field = "GESTURE_UP", mode = "UP"),
    @F2M(field = "GESTURE_DOWN", mode = "DOWN"), @F2M(field = "GESTURE_FACE_DOWN", mode = "FACE_DOWN"), @F2M(field = "GESTURE_FREEFALL", mode = "FREEFALL"),
    @F2M(field = "GESTURE_ACTIVE", mode = "GESTURE_ACTIVE"), @F2M(field = "GESTURE_SHAKE", mode = "SHAKE"), @F2M(field = "GESTURE_FACE_UP", mode = "FACE_UP"),
    @F2M(field = "GESTURE_TAPPED", mode = "TAPPED"), @F2M(field = "GESTURE_RIGHT", mode = "RIGHT"),
    @F2M(field = "GESTURE_LEFT", mode = "LEFT"), @F2M(field = "GESTURE_FRONT", mode = "FRONT"), @F2M(field = "GESTURE_BACK", mode = "BACK")})
public final class GestureSensor extends ExternalSensor {

    public GestureSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
