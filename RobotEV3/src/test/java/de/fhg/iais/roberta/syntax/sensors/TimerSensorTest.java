package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TimerSensorTest extends Ev3LejosAstTest {

    @Test
    public void getTimerValue() throws Exception {
        String a = "\nhal.getTimerValue(1)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/sensors/sensor_getSampleTimer.xml", false);
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\nhal.resetTimer(1);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/sensors/sensor_resetTimer.xml", false);
    }
}
