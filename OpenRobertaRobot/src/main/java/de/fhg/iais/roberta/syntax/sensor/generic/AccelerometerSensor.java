package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "ACCELEROMETER_VALUE", sensor = "ACCELEROMETER", mode = "DEFAULT"), @NepoSampleValue(blocklyFieldName = "ACCELEROMETER_Z", sensor = "ACCELEROMETER", mode = "Z"), @NepoSampleValue(blocklyFieldName = "ACCELEROMETER_X", sensor = "ACCELEROMETER", mode = "X"), @NepoSampleValue(blocklyFieldName = "ACCELEROMETER_Y", sensor = "ACCELEROMETER", mode = "Y")}, containerType = "ACCELEROMETER_SENSING", category = "SENSOR", blocklyNames = {"mbedSensors_acceleration_getSample", "robsensors_accelerometer_getsample"})
@NepoExternalSensor()
public final class AccelerometerSensor<V> extends ExternalSensor<V> {

    public AccelerometerSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}
