package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class TimerSensorTest {
    Helper h = new Helper();

    @Test
    public void getTimerValue() throws Exception {
        String a = "\nT.ShowSeconds()";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_getSampleTimer.xml", false);
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\nT.ResetTimer();";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_resetTimer.xml", false);
    }
}
