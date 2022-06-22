package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "ULTRASONIC_DISTANCE", sensor = "ULTRASONIC", mode = "DISTANCE"), @NepoSampleValue(blocklyFieldName = "ULTRASONIC_PRESENCE", sensor = "ULTRASONIC", mode = "PRESENCE")}, containerType = "ULTRASONIC_SENSING", category = "SENSOR", blocklyNames = {"robSensors_ultrasonic_getSample", "sim_ultrasonic_getSample"})
@NepoExternalSensor()
public final class UltrasonicSensor<V> extends ExternalSensor<V> {

    public UltrasonicSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
