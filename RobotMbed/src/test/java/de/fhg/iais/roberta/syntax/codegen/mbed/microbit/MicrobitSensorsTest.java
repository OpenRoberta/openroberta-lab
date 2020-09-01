package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.MicrobitAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitSensorsTest extends MicrobitAstTest {

    @Test
    public void waitTimeConditionTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(testFactory, "/sensor/microbit_timer_test.py", "/sensor/microbit_timer_test.xml", configuration);
    }

}
