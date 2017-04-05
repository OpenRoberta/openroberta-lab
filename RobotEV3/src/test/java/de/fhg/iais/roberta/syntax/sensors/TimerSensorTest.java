package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TimerSensorTest {
    @Test
    public void getTimerValue() throws Exception {
        String a = "\nhal.getTimerValue(1)}";

        Helper.assertCodeIsOk(a, "/syntax/sensors/sensor_getSampleTimer.xml");
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\nhal.resetTimer(1);}";

        Helper.assertCodeIsOk(a, "/syntax/sensors/sensor_resetTimer.xml");
    }
}
