package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "VEMLLIGHT_SENSING", category = "SENSOR", blocklyNames = {"robSensors_lightveml_getSample"},
    sampleValues = {@F2M(field = "LIGHTVEML_UVLIGHT", mode = "UVLIGHT"), @F2M(field = "LIGHTVEML_LIGHT", mode = "LIGHT")})
@NepoExternalSensor()
public final class VemlLightSensor<V> extends ExternalSensor<V> {

    public VemlLightSensor(BlocklyBlockProperties properties, BlocklyComment comment, ExternalSensorBean externalSensorBean) {
        super(properties, comment, externalSensorBean);
        this.setReadOnly();
    }

}
