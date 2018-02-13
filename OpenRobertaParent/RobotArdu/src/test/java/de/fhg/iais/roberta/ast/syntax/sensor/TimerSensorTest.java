package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class TimerSensorTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void getTimerValue() throws Exception {
        String a = "\n(int)(millis()-__time)";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_getSampleTimer.xml", false);
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\n__time = millis();";

        this.h.assertCodeIsOk(a, "/ast/sensors/sensor_resetTimer.xml", false);
    }
}
