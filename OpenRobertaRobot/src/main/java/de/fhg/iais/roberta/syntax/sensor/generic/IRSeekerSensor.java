package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "IRSEEKER_SENSING", category = "SENSOR", blocklyNames = {"robSensors_irseeker_getSample"},
    sampleValues = {@F2M(field = "IRSEEKER_RCCODE", mode = "RCCODE"), @F2M(field = "IRSEEKER_MODULATED", mode = "MODULATED"), @F2M(field = "IRSEEKER_UNMODULATED", mode = "UNMODULATED")})
public final class IRSeekerSensor extends ExternalSensor {

    public IRSeekerSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
