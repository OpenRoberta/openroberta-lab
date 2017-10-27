package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class TouchSensorTest {
    Helper h = new Helper();

    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(SensorPort.S1)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_Touch.xml");
    }
}
