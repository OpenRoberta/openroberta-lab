package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "IRSEEKER_RCCODE", sensor = "IRLED", mode = "RCCODE"), @NepoSampleValue(blocklyFieldName = "IRSEEKER_MODULATED", sensor = "IRSEEKER", mode = "MODULATED"), @NepoSampleValue(blocklyFieldName = "IRSEEKER_UNMODULATED", sensor = "IRSEEKER", mode = "UNMODULATED")}, containerType = "IRSEEKER_SENSING", category = "SENSOR", blocklyNames = {"robSensors_irseeker_getSample"})
@NepoExternalSensor()
public final class IRSeekerSensor<V> extends ExternalSensor<V> {

    public IRSeekerSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}
