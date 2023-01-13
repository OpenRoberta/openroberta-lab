package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "QUAD_COLOR_SENSING", category = "SENSOR", blocklyNames = {"robSensors_line_getSample", "robSensors_quadrgb_getSample"},
    sampleValues = {@F2M(field = "QUADRGB_COLOUR", mode = "COLOUR"), @F2M(field = "QUADRGB_LIGHT", mode = "LIGHT"), @F2M(field = "QUADRGB_AMBIENTLIGHT", mode = "AMBIENTLIGHT"), @F2M(field = "QUADRGB_LINE", mode = "LINE")})
public final class QuadRGBSensor extends ExternalSensor {

    public QuadRGBSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
