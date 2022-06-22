package de.fhg.iais.roberta.syntax.sensors.arduino.sensebox;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_TEMPERATURE",sensor="ENVIRONMENTAL",mode="TEMPERATURE"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_VOCEQUIVALENT",sensor="ENVIRONMENTAL",mode="VOCEQUIVALENT"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_CALIBRATION",sensor="ENVIRONMENTAL",mode="CALIBRATION"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_IAQ",sensor="ENVIRONMENTAL",mode="IAQ"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_PRESSURE",sensor="ENVIRONMENTAL",mode="PRESSURE"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_HUMIDITY",sensor="ENVIRONMENTAL",mode="HUMIDITY"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_CO2EQUIVALENT",sensor="ENVIRONMENTAL",mode="CO2EQUIVALENT")}, containerType = "ENVIRONMENTAL", category = "SENSOR", blocklyNames = {"robSensors_environmental_getSample"})
@NepoExternalSensor
public final class EnvironmentalSensor<V> extends ExternalSensor<V> {

    public EnvironmentalSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
