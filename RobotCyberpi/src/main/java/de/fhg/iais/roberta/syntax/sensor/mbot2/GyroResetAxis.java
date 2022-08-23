package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(category = "SENSOR", blocklyNames = {"robSensors_gyro_reset_axis"}, name = "GYRO_RESET_AXIS")
public final class GyroResetAxis extends ExternalSensor {

    public GyroResetAxis(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}
