package de.fhg.iais.roberta.syntax.codegen.sensebox;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SenseboxSensorTest extends SenseboxAstTest {

    @Test
    public void builtinAndSimpleActorsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/sensebox_simple_and_builtin_sensor_test.ino",
                "/ast/sensors/sensebox_simple_and_builtin_sensor_test.xml",
                "/ast/sensors/sensebox_simple_and_builtin_sensor_test_config.xml");
    }
}
