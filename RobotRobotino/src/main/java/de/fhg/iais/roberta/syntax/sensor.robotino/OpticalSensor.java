package de.fhg.iais.roberta.syntax.sensor.robotino;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(category = "SENSOR", blocklyNames = {"robSensors_optical_getSample"}, name = "OPTICAL_SENSING",
    sampleValues = {@F2M(field = "OPTICAL_OPENING", mode = "OPENING"), @F2M(field = "OPTICAL_CLOSING", mode = "CLOSING")})
public final class OpticalSensor extends ExternalSensor {
    public OpticalSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
