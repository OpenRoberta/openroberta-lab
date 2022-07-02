package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "ACCELEROMETER_SENSING", category = "SENSOR", blocklyNames = {"mbedSensors_acceleration_getSample", "robsensors_accelerometer_getsample"},
    sampleValues = {@F2M(field = "ACCELEROMETER_VALUE", mode = "DEFAULT"), @F2M(field = "ACCELEROMETER_Z", mode = "Z"), @F2M(field = "ACCELEROMETER_X", mode = "X"), @F2M(field = "ACCELEROMETER_Y", mode = "Y")})
@NepoExternalSensor()
public final class AccelerometerSensor<V> extends ExternalSensor<V> {

    public AccelerometerSensor(BlocklyBlockProperties properties, BlocklyComment comment, ExternalSensorBean externalSensorBean) {
        super(properties, comment, externalSensorBean);
        setReadOnly();
    }
}
