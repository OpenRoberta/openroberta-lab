package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "MOTION_SENSING", category = "SENSOR", blocklyNames = {"robSensors_motion_getSample"},
    sampleValues = {@F2M(field = "MOTION_PRESENCE", mode = "PRESENCE")})
@NepoExternalSensor()
public final class MotionSensor<V> extends ExternalSensor<V> {

    public MotionSensor(BlocklyBlockProperties properties, BlocklyComment comment, ExternalSensorBean externalSensorBean) {
        super(properties, comment, externalSensorBean);
        setReadOnly();
    }

}
