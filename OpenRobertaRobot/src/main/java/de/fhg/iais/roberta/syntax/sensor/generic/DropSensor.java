package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "DROP_SENSING", category = "SENSOR", blocklyNames = {"robSensors_drop_getSample"},
    sampleValues = {@F2M(field = "DROP_OFF_DISTANCE", mode = "DISTANCE"), @F2M(field = "DROP_VALUE", mode = "VALUE")})
public final class DropSensor extends ExternalSensor {

    public DropSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
