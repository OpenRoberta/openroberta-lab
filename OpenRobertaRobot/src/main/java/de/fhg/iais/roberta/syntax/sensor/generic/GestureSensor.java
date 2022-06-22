package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "GESTURE_UP", sensor = "GESTURE", mode = "UP"), @NepoSampleValue(blocklyFieldName = "GESTURE_DOWN", sensor = "GESTURE", mode = "DOWN"), @NepoSampleValue(blocklyFieldName = "GESTURE_FACE_DOWN", sensor = "GESTURE", mode = "FACE_DOWN"), @NepoSampleValue(blocklyFieldName = "GESTURE_FREEFALL", sensor = "GESTURE", mode = "FREEFALL"), @NepoSampleValue(blocklyFieldName = "GESTURE_ACTIVE", sensor = "GESTURE", mode = "GESTURE_ACTIVE"), @NepoSampleValue(blocklyFieldName = "GESTURE_SHAKE", sensor = "GESTURE", mode = "SHAKE"), @NepoSampleValue(blocklyFieldName = "GESTURE_FACE_UP", sensor = "GESTURE", mode = "FACE_UP")}, containerType = "GESTURE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_gesture_getSample"})
@NepoExternalSensor()
public final class GestureSensor<V> extends ExternalSensor<V> {

    public GestureSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}
