package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class TimerSensorTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void getTimerValue() throws Exception {
        String a = "\nhal.getTimerValue(1)}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_getSampleTimer.xml");
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\nhal.resetTimer(1);}";

        this.h.assertCodeIsOk(a, "/syntax/sensors/sensor_resetTimer.xml");
    }
}
