package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "KEYS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_touchkey_getSample"},
    sampleValues = {@F2M(field = "TOUCHKEY_PRESSED", mode = "PRESSED")})
public final class TouchKeySensor extends ExternalSensor {

    public TouchKeySensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
