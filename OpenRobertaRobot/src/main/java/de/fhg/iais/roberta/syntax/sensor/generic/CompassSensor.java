package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "COMPASS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_compass_getSample", "mbedsensors_compass_getsample"},
    sampleValues = {@F2M(field = "COMPASS_COMPASS", mode = "COMPASS"), @F2M(field = "COMPASS_X", mode = "X"), @F2M(field = "COMPASS_Y", mode = "Y"),
        @F2M(field = "COMPASS_ANGLE", mode = "ANGLE"), @F2M(field = "COMPASS_Z", mode = "Z")}, blocklyType = BlocklyType.NUMBER)
public final class CompassSensor extends ExternalSensor {

    public CompassSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
