package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "ULTRASONIC_SENSING", category = "SENSOR", blocklyNames = {"robSensors_ultrasonic_getSample", "sim_ultrasonic_getSample"},
    sampleValues = {@F2M(field = "ULTRASONIC_DISTANCE", mode = "DISTANCE"), @F2M(field = "ULTRASONIC_PRESENCE", mode = "PRESENCE")})
@NepoExternalSensor()
public final class UltrasonicSensor<V> extends ExternalSensor<V> {

    public UltrasonicSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
