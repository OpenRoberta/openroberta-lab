package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "HUMIDITY_TEMPERATURE", sensor = "HUMIDITY", mode = "TEMPERATURE"), @NepoSampleValue(blocklyFieldName = "HUMIDITY_HUMIDITY", sensor = "HUMIDITY", mode = "HUMIDITY")}, containerType = "HUMIDITY_SENSING", category = "SENSOR", blocklyNames = {"robSensors_humidity_getSample"})
@NepoExternalSensor()
public final class HumiditySensor<V> extends ExternalSensor<V> {

    public HumiditySensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
