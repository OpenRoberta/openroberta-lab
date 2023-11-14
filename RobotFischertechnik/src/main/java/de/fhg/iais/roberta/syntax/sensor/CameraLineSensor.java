package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "CAMERA_LINE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_cameraline_getSample"},
    sampleValues = {@F2M(field = "CAMERA_NUMBERLINES", mode = "NUMBERLINES")})
public final class CameraLineSensor extends ExternalSensor {

    public CameraLineSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
