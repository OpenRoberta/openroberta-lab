package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BrickSensorTest extends NxtAstTest {

    @Test
    public void isPressed() throws Exception {
        String a = "\nButtonPressed(BTNCENTER,false)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_brick1.xml", false);
    }
}
