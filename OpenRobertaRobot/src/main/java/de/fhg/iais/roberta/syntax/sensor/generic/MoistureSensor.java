package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "MOISTURE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_moisture_getSample"},
    sampleValues = {@F2M(field = "MOISTURE_VALUE", mode = "VALUE")})
public final class MoistureSensor extends ExternalSensor {

    public MoistureSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
