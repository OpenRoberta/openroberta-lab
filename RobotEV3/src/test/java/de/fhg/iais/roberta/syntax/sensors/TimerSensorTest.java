package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class TimerSensorTest {
    Helper h = new Helper();

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
