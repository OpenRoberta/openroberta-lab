package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class TouchSensorTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(SensorPort.S1)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_Touch.xml");
    }
}
