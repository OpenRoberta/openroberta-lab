package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TimerSensorTest extends BotnrollAstTest {

    @Test
    public void getTimerValue() throws Exception {
        String a = "\n(int)(millis()-__time_1)";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_getSampleTimer.xml", makeConfiguration(), false);
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\n__time_1 = millis();";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_resetTimer.xml", makeConfiguration(), false);
    }
}
