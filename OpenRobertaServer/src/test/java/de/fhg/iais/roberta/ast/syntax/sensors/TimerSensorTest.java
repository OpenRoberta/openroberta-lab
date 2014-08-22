package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TimerSensorTest {
    @Test
    public void getTimerValue() throws Exception {
        String a = "\nhal.getTimerValue(1)";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_getSampleTimer.xml"));
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\nhal.resetTimer(1);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/sensors/sensor_resetTimer.xml"));
    }
}
