package de.fhg.iais.roberta.syntax.sensor.mbed;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "CALLIBOT_KEYS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_keyc_getSample"}, sampleValues = {@F2M(field = "KEYC_PRESSED", mode = "PRESSED")})
public final class CallibotKeysSensor extends ExternalSensor {

    public CallibotKeysSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}