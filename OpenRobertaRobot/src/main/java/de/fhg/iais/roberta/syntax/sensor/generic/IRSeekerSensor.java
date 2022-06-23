package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "IRSEEKER_SENSING", category = "SENSOR", blocklyNames = {"robSensors_irseeker_getSample"},
    sampleValues = {@F2M(field = "IRSEEKER_RCCODE", mode = "RCCODE"), @F2M(field = "IRSEEKER_MODULATED", mode = "MODULATED"), @F2M(field = "IRSEEKER_UNMODULATED", mode = "UNMODULATED")})
@NepoExternalSensor()
public final class IRSeekerSensor<V> extends ExternalSensor<V> {

    public IRSeekerSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}
