package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class InfraredSensorTest {
    @Test
    public void setInfrared() throws Exception {
        String a = "\nhal.getInfraredSensorDistance(SensorPort.S4)" + "hal.getInfraredSensorSeek(SensorPort.S3)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setInfrared.xml");
    }
}
