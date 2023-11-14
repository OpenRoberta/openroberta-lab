package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "GYRO_SENSING", category = "SENSOR", blocklyNames = {"robSensors_gyro_getSample"},
    sampleValues = {@F2M(field = "GYRO_TILTED", mode = "TILTED"), @F2M(field = "GYRO_Y", mode = "Y"), @F2M(field = "GYRO_RATE", mode = "RATE"),
        @F2M(field = "GYRO_Z", mode = "Z"), @F2M(field = "GYRO_X", mode = "X"), @F2M(field = "GYRO_ANGLE", mode = "ANGLE"), @F2M(field = "IMU_GYRO", mode = "GYRO"), @F2M(field = "IMU_MAGNETICFLUX", mode = "MAGNETICFLUX"), @F2M(field = "IMU_ACCELERATION", mode = "ACCELERATION")})
public final class GyroSensor extends ExternalSensor {

    public GyroSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
