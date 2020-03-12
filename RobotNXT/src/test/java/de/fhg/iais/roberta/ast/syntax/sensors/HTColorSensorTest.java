package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class HTColorSensorTest extends NxtAstTest {

    @Test
    public void setColor() throws Exception {
        String a = "SensorHtColor(S3,\"COLOUR\")SensorHtColor(S1,\"LIGHT\")SensorHtColor(S4,\"AMBIENTLIGHT\")SensorHtColor(S2,\"RGB\")";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_setHTColor.xml", brickConfigurationHTC1HTC2HTC3HTC4, false);
    }
}
