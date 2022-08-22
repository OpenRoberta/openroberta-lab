package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "LIGHT_SENSING", category = "SENSOR", blocklyNames = {"robSensors_light_getSample", "sim_light_getSample"},
    sampleValues = {@F2M(field = "LIGHT_LEVEL", mode = "LIGHT_LEVEL"),
        @F2M(field = "LIGHT_LIGHT", mode = "LIGHT"), @F2M(field = "LIGHT_AMBIENTLIGHT", mode = "AMBIENTLIGHT"),
        @F2M(field = "LIGHT_VALUE", mode = "LIGHT_VALUE"), @F2M(field = "LIGHT_LINE", mode = "LINE")})
public final class LightSensor extends ExternalSensor {

    public LightSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
