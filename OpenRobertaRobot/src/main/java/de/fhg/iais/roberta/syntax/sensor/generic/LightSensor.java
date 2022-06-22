package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "INFRARED_AMBIENTLIGHT", sensor = "LIGHT", mode = "AMBIENTLIGHT"), @NepoSampleValue(blocklyFieldName = "LIGHT_LEVEL", sensor = "LIGHT_LEVEL", mode = "LIGHT_LEVEL"), @NepoSampleValue(blocklyFieldName = "LIGHT_LIGHT", sensor = "LIGHT", mode = "LIGHT"), @NepoSampleValue(blocklyFieldName = "LIGHT_AMBIENTLIGHT", sensor = "LIGHT", mode = "AMBIENTLIGHT"), @NepoSampleValue(blocklyFieldName = "LIGHT_VALUE", sensor = "LIGHT_VALUE", mode = "LIGHT_VALUE"), @NepoSampleValue(blocklyFieldName = "LIGHT_LINE", sensor = "LINETRACKER", mode = "LINE")}, containerType = "LIGHT_SENSING", category = "SENSOR", blocklyNames = {"robSensors_light_getSample", "sim_light_getSample"})
@NepoExternalSensor()
public final class LightSensor<V> extends ExternalSensor<V> {

    public LightSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
