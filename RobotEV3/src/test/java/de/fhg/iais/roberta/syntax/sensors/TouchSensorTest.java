package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TouchSensorTest {
    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(SensorPort.S1)}";

        Helper.assertCodeIsOk(a, "/syntax/sensors/sensor_Touch.xml");
    }
}
