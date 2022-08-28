package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "DETECT_FACE", category = "SENSOR", blocklyNames = {"robSensors_detectface_getSample"},
    sampleValues = {@F2M(field = "DETECTFACE_NAMEONE", mode = "NAMEONE")})
public final class DetectFaceSensor extends ExternalSensor {

    public DetectFaceSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
