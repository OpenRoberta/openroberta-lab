package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class UltrasonicSensorTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void setUltrasonic() throws Exception {
        String a = "\nhal.getUltraSonicSensorDistance(SensorPort.S4)" + "hal.getUltraSonicSensorPresence(SensorPort.S2)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_setUltrasonic.xml");
    }
}
