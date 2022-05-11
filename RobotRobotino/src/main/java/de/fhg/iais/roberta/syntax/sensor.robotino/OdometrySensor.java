package de.fhg.iais.roberta.syntax.sensor.robotino;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(category = "SENSOR", blocklyNames = {"robSensors_odometry_getSample"}, name = "ODOMETRY_SENSING",
    sampleValues = {@F2M(field = "ODOMETRY_VALUE", mode = "VALUE")})
public final class OdometrySensor extends ExternalSensor {
    public OdometrySensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
