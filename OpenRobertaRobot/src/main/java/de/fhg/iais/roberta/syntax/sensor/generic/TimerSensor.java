package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "TIMER_SENSING", category = "SENSOR", blocklyNames = {"robSensors_timer_getSample"}, sampleValues = {@F2M(field = "TIMER_VALUE", mode = "VALUE"),
    @F2M(field = "TIME", mode = "VALUE")}, blocklyType = BlocklyType.NUMBER)
public final class TimerSensor extends ExternalSensor {

    public TimerSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
