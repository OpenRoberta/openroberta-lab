package de.fhg.iais.roberta.syntax.sensor.mbed;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "RADIO_RSSI", category = "SENSOR", blocklyNames = {"mbedSensors_getRssi", "robsensors_rssi_getsample"}, blocklyType = BlocklyType.NUMBER)
public final class RadioRssiSensor extends ExternalSensor {

    public RadioRssiSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
