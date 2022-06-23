package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "GESTURE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_gesture_getSample"},
    sampleValues = {@F2M(field = "GESTURE_UP", mode = "UP"), @F2M(field = "GESTURE_DOWN", mode = "DOWN"), @F2M(field = "GESTURE_FACE_DOWN", mode = "FACE_DOWN"),
        @F2M(field = "GESTURE_FREEFALL", mode = "FREEFALL"), @F2M(field = "GESTURE_ACTIVE", mode = "GESTURE_ACTIVE"),
        @F2M(field = "GESTURE_SHAKE", mode = "SHAKE"), @F2M(field = "GESTURE_FACE_UP", mode = "FACE_UP")})
@NepoExternalSensor()
public final class GestureSensor<V> extends ExternalSensor<V> {

    public GestureSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}
