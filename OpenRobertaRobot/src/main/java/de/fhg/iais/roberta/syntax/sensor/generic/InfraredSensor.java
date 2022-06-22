package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "INFRARED_LINE", sensor = "INFRARED", mode = "LINE"), @NepoSampleValue(blocklyFieldName = "INFRARED_REFLEXION", sensor = "INFRARED", mode = "REFLEXION"), @NepoSampleValue(blocklyFieldName = "INFRARED_SEEK", sensor = "INFRARED", mode = "SEEK"), @NepoSampleValue(blocklyFieldName = "INFRARED_DISTANCE", sensor = "INFRARED", mode = "DISTANCE"), @NepoSampleValue(blocklyFieldName = "INFRARED_VALUE", sensor = "INFRARED", mode = "VALUE"), @NepoSampleValue(blocklyFieldName = "INFRARED_OBSTACLE", sensor = "INFRARED", mode = "OBSTACLE"), @NepoSampleValue(blocklyFieldName = "INFRARED_PRESENCE", sensor = "INFRARED", mode = "PRESENCE")}, containerType = "INFRARED_SENSING", category = "SENSOR", blocklyNames = {"robSensors_infrared_getSample"})
@NepoExternalSensor()
public final class InfraredSensor<V> extends ExternalSensor<V> {

    public InfraredSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
