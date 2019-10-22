package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class UltrasonicSensorTest extends NxtAstTest {

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nSensorUS(S4)SensorUS(S2)";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/ast/sensors/sensor_setUltrasonic.xml",
                brickConfigurationUS2US4,
                false);
    }
}
