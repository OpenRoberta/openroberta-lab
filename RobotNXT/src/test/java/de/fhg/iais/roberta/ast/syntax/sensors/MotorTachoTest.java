package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorTachoTest extends NxtAstTest {

    @Test
    public void setMotorTacho() throws Exception {
        final String a = "\nMotorTachoCount(OUT_A)/360.0" + "MotorTachoCount(OUT_C)";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_setEncoder.xml", brickConfigurationAC, false);
    }

    @Test
    public void resetMotorTacho() throws Exception {
        final String a = "\nResetTachoCount(OUT_A);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_resetEncoder.xml", brickConfiguration, false);
    }
}
