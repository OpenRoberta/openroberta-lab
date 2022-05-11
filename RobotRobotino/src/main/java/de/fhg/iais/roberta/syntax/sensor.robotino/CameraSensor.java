package de.fhg.iais.roberta.syntax.sensor.robotino;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(category = "SENSOR", blocklyNames = {"robSensors_camera_getSample"}, name = "CAMERA_SENSING",
    sampleValues = {@F2M(field = "CAMERA_LINE", mode = "LINE")})
public final class CameraSensor extends ExternalSensor {
    public CameraSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
