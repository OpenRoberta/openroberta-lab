package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "ACCELEROMETER_SENSING", category = "SENSOR", blocklyNames = {"mbedSensors_acceleration_getSample", "robsensors_accelerometer_getsample"},
    sampleValues = {@F2M(field = "ACCELEROMETER_VALUE", mode = "DEFAULT"), @F2M(field = "ACCELEROMETER_Z", mode = "Z"), @F2M(field = "ACCELEROMETER_X", mode = "X"), @F2M(field = "ACCELEROMETER_Y", mode = "Y")})
public final class AccelerometerSensor extends ExternalSensor {

    public AccelerometerSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
