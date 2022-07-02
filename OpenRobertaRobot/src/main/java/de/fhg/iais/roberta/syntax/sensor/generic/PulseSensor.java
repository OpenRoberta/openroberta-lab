package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "PULSE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_pulse_getSample"},
    sampleValues = {@F2M(field = "PULSE_VALUE", mode = "VALUE")})
@NepoExternalSensor()
public final class PulseSensor<V> extends ExternalSensor<V> {

    public PulseSensor(BlocklyBlockProperties properties, BlocklyComment comment, ExternalSensorBean externalSensorBean) {
        super(properties, comment, externalSensorBean);
        setReadOnly();
    }

}
