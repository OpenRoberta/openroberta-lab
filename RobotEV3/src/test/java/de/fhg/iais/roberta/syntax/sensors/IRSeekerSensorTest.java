package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class IRSeekerSensorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void getIRSeeker() throws Exception {
        String a =
            "hal.drawText(String.valueOf(hal.getHiTecIRSeekerModulated(SensorPort.S1)), 0, 0);"
                + "hal.drawText(String.valueOf(hal.getHiTecIRSeekerUnmodulated(SensorPort.S1)), 0, 0);}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_getIRSeeker.xml");
    }
}
