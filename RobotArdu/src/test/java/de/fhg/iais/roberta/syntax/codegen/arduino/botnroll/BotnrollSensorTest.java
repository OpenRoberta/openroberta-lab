package de.fhg.iais.roberta.syntax.codegen.arduino.botnroll;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BotnrollSensorTest extends BotnrollAstTest {

    @Test
    public void botnrollSensorTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/botnroll_sensors_test.ino",
                "/ast/sensors/botnroll_sensors_test.xml",
                makeConfiguration());
    }
}
