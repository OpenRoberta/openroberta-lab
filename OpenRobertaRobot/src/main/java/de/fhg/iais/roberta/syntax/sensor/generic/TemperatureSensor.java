package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "TEMPERATURE_PRESSURE", sensor = "TEMPERATURE", mode = "PRESSURE"), @NepoSampleValue(blocklyFieldName = "TEMPERATURE", sensor = "TEMPERATURE", mode = "TEMPERATURE"), @NepoSampleValue(blocklyFieldName = "TEMPERATURE_TEMPERATURE", sensor = "TEMPERATURE", mode = "TEMPERATURE"), @NepoSampleValue(blocklyFieldName = "TEMPERATURE_VALUE", sensor = "TEMPERATURE", mode = "TEMPERATURE")}, containerType = "TEMPERATURE_SENSING", category = "SENSOR", blocklyNames = {"mbedsensors_temperature_getsample", "robSensors_temperature_getSample"})
@NepoExternalSensor()
public final class TemperatureSensor<V> extends ExternalSensor<V> {

    public TemperatureSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
