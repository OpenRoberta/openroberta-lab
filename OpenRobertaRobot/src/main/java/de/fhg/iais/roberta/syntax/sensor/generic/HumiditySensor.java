package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "HUMIDITY_SENSING", category = "SENSOR", blocklyNames = {"robSensors_humidity_getSample"},
    sampleValues = {@F2M(field = "HUMIDITY_TEMPERATURE", mode = "TEMPERATURE"), @F2M(field = "HUMIDITY_HUMIDITY", mode = "HUMIDITY")})
public final class HumiditySensor extends ExternalSensor {

    public HumiditySensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
