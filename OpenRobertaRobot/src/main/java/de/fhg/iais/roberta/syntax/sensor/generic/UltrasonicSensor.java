package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "ULTRASONIC_SENSING", category = "SENSOR", blocklyNames = {"robSensors_ultrasonic_getSample", "sim_ultrasonic_getSample"},
    sampleValues = {@F2M(field = "ULTRASONIC_DISTANCE", mode = "DISTANCE"), @F2M(field = "ULTRASONIC_PRESENCE", mode = "PRESENCE")})
public final class UltrasonicSensor extends ExternalSensor {

    public UltrasonicSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
