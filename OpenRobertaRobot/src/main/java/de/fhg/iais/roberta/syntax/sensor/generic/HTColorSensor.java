package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "HTCOLOR_SENSING", category = "SENSOR", blocklyNames = {"robSensors_htcolour_getSample"},
    sampleValues = {@F2M(field = "HTCOLOUR_AMBIENTLIGHT", mode = "AMBIENTLIGHT"), @F2M(field = "HTCOLOUR_COLOUR", mode = "COLOUR"), @F2M(field = "HTCOLOUR_LIGHT", mode = "LIGHT")})
public final class HTColorSensor extends ExternalSensor {

    public HTColorSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
