package de.fhg.iais.roberta.syntax.sensor.rcj;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(category = "SENSOR", blocklyNames = {"robSensors_inductive_getSample"}, name = "INDUCTIVE_SENSING",
    sampleValues = {@F2M(field = "INDUCTIVE_PRESENCE", mode = "PRESENCE")})
public final class InductiveSensor extends ExternalSensor {
    public InductiveSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}