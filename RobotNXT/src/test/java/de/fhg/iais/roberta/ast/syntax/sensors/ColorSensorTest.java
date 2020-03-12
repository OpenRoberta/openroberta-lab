package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ColorSensorTest extends NxtAstTest {

    @Test
    public void setColor() throws Exception {
        String a = "SensorColor(S3,\"COLOUR\")SensorColor(S1,\"LIGHT\")SensorColor(S4,\"AMBIENTLIGHT\")";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_setColor.xml", brickConfigurationC1C3C4, false);
    }
}
