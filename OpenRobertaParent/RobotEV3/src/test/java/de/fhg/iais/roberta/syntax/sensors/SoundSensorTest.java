package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class SoundSensorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void getSampleSound() throws Exception {
        String a = "\nhal.getSoundLevel(SensorPort.S1)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_getSampleSound.xml");
    }
}
