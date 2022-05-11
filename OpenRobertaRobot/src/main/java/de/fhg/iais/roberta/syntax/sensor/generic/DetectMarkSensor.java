package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(sampleValues = {@F2M(field = "DETECTMARK_IDONE", mode = "IDONE")}, name = "DETECT_MARK", category = "SENSOR", blocklyNames = {"robSensors_detectmark_getSample"})
public final class DetectMarkSensor extends ExternalSensor {

    public DetectMarkSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
