package de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "LOGO_TOUCH_SENSING", category = "SENSOR", blocklyNames = {"robsensors_logotouch_getsample"},
    sampleValues = {@F2M(field = "LOGOTOUCH_TOUCHED", mode = "TOUCHED"), @F2M(field = "LOGOTOUCH_PRESSED", mode = "PRESSED")})
public final class LogoTouchSensor extends ExternalSensor {

    public LogoTouchSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}