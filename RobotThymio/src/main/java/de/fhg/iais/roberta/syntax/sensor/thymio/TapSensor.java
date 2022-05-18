package de.fhg.iais.roberta.syntax.sensor.thymio;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "TAP_SENSING", category = "SENSOR", blocklyNames = {"robSensors_tap_getSample"},
    sampleValues = {@F2M(field = "TAP_VALUE", mode = "VALUE")})
public final class TapSensor extends ExternalSensor {

    public TapSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
