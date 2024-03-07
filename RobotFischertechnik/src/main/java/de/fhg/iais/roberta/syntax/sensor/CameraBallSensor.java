package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "CAMERA_BALL_SENSING", category = "SENSOR", blocklyNames = {"robSensors_ball_getSample"},
    sampleValues = {@F2M(field = "CAMERA_BALL", mode = "LINES")})
public final class CameraBallSensor extends ExternalSensor {

    public CameraBallSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
