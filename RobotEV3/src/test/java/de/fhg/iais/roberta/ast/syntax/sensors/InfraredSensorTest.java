package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.ev3.Helper;

public class InfraredSensorTest {
    @Test
    public void setInfrared() throws Exception {
        String a = "\nhal.getInfraredSensorDistance(SensorPort.S4)" + "hal.getInfraredSensorSeek(SensorPort.S3)";

        Helper.assertCodeIsOk(a, "/syntax/sensors/sensor_setInfrared.xml");
    }
}
