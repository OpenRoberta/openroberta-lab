package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "ENCODER_SENSING", category = "SENSOR", blocklyNames = {"robSensors_encoder_getSample"},
    sampleValues = {@F2M(field = "ENCODER_DISTANCE", mode = "DISTANCE"), @F2M(field = "ENCODER_DEGREE", mode = "DEGREE"),
        @F2M(field = "ENCODER_ROTATION", mode = "ROTATION")}, blocklyType = BlocklyType.NUMBER)
public final class EncoderSensor extends ExternalSensor {

    public EncoderSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
