package de.fhg.iais.roberta.syntax.sensors.arduino.sensebox;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName="GPS_LATITUDE",sensor="GPS",mode="LATITUDE"),@NepoSampleValue(blocklyFieldName="GPS_SPEED",sensor="GPS",mode="SPEED"),@NepoSampleValue(blocklyFieldName="GPS_DATE",sensor="GPS",mode="DATE"),@NepoSampleValue(blocklyFieldName="GPS_TIME",sensor="GPS",mode="TIME"),@NepoSampleValue(blocklyFieldName="GPS_ALTITUDE",sensor="GPS",mode="ALTITUDE"),@NepoSampleValue(blocklyFieldName="GPS_LONGITUDE",sensor="GPS",mode="LONGITUDE")}, containerType = "GPS", category = "SENSOR", blocklyNames = {"robSensors_gps_getSample"})
@NepoExternalSensor
public final class GpsSensor<V> extends ExternalSensor<V> {

    public GpsSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}
