package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "RFID_SENSING", category = "SENSOR", blocklyNames = {"robSensors_rfid_getSample"},
    sampleValues = {@F2M(field = "RFID_IDONE", mode = "IDONE"), @F2M(field = "RFID_PRESENCE", mode = "PRESENCE")})
public final class RfidSensor extends ExternalSensor {

    public RfidSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
