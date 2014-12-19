package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class InfraredSensorTest {
    @Test
    public void setInfrared() throws Exception {
        String a =
            "\nhal.getInfraredSensorValue(SensorPort.S4, InfraredSensorMode.DISTANCE)" + "hal.getInfraredSensorValue(SensorPort.S3, InfraredSensorMode.SEEK)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setInfrared.xml");
    }
}
