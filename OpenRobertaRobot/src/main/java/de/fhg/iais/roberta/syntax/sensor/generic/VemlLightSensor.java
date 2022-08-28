package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "VEMLLIGHT_SENSING", category = "SENSOR", blocklyNames = {"robSensors_lightveml_getSample"},
    sampleValues = {@F2M(field = "LIGHTVEML_UVLIGHT", mode = "UVLIGHT"), @F2M(field = "LIGHTVEML_LIGHT", mode = "LIGHT")})
public final class VemlLightSensor extends ExternalSensor {

    public VemlLightSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        this.setReadOnly();
    }

}
