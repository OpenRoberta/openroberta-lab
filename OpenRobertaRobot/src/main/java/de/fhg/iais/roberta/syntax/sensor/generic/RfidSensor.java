package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "RFID_IDONE", sensor = "RFID", mode = "IDONE"), @NepoSampleValue(blocklyFieldName = "RFID_PRESENCE", sensor = "RFID", mode = "PRESENCE")}, containerType = "RFID_SENSING", category = "SENSOR", blocklyNames = {"robSensors_rfid_getSample"})
@NepoExternalSensor()
public final class RfidSensor<V> extends ExternalSensor<V> {

    public RfidSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
