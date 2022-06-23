package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "RFID_SENSING", category = "SENSOR", blocklyNames = {"robSensors_rfid_getSample"},
    sampleValues = {@F2M(field = "RFID_IDONE", mode = "IDONE"), @F2M(field = "RFID_PRESENCE", mode = "PRESENCE")})
@NepoExternalSensor()
public final class RfidSensor<V> extends ExternalSensor<V> {

    public RfidSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}
