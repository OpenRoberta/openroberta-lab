package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "MOTION_SENSING", category = "SENSOR", blocklyNames = {"robSensors_motion_getSample"},
    sampleValues = {@F2M(field = "MOTION_PRESENCE", mode = "PRESENCE")})
public final class MotionSensor extends ExternalSensor {

    public MotionSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
