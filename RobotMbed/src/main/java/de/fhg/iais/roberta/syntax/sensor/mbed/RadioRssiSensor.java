package de.fhg.iais.roberta.syntax.sensor.mbed;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "RADIO_RSSI", category = "SENSOR", blocklyNames = {"robSensors_rssi_getSample"}, sampleValues = {@F2M(field = "RSSI_VALUE", mode = "VALUE")}, blocklyType = BlocklyType.NUMBER)
public final class RadioRssiSensor extends ExternalSensor {

    public RadioRssiSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
