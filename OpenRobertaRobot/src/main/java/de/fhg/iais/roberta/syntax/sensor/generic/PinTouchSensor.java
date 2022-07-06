package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "PIN_TOUCH_SENSING", category = "SENSOR", blocklyNames = {"robsensors_pintouch_getsample"},
    sampleValues = {@F2M(field = "PIN_TOUCHED", mode = "PIN_TOUCHED"), @F2M(field = "PINTOUCH_PRESSED", mode = "PRESSED")})
@NepoExternalSensor()
public final class PinTouchSensor<V> extends ExternalSensor<V> {

    public PinTouchSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}
