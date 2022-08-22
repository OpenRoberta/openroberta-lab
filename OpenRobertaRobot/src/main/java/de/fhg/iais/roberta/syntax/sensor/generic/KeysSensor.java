package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "KEYS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_key_getSample"},
    sampleValues = {@F2M(field = "PLAYKEY_PRESSED", mode = "PRESSED"), @F2M(field = "KEY_PRESSED", mode = "PRESSED"), @F2M(field = "RECKEY_PRESSED", mode = "PRESSED")})
public final class KeysSensor extends ExternalSensor {

    public KeysSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
