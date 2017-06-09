package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class InfraredSensorTest {
    Helper h = new Helper();

    @Test
    public void setInfrared() throws Exception {
        String a = "\nhal.getInfraredSensorDistance(SensorPort.S4)" + "hal.getInfraredSensorSeek(SensorPort.S3)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_setInfrared.xml");
    }
}
