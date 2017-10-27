package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class UltrasonicSensorTest {
    Helper h = new Helper();

    @Test
    public void setUltrasonic() throws Exception {
        String a = "\nhal.getUltraSonicSensorDistance(SensorPort.S4)" + "hal.getUltraSonicSensorPresence(SensorPort.S2)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_setUltrasonic.xml");
    }
}
