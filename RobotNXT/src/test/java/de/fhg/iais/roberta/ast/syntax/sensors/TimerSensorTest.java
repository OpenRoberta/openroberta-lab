package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TimerSensorTest extends NxtAstTest {

    @Test
    public void getTimerValue() throws Exception {
        String a = "\nGetTimerValue(timer1)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_getSampleTimer.xml", false);
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\nResetTimerValue(timer1);";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_resetTimer.xml", false);
    }
}
