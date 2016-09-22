package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TimerSensorTest {
    @Test
    public void getTimerValue() throws Exception {
        String a = "\nGetTimerValue(timer1)";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_getSampleTimer.xml");
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\nResetTimerValue(timer1);";

        Helper.assertCodeIsOk(a, "/ast/sensors/sensor_resetTimer.xml");
    }
}
