package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BrickSensorTest extends Ev3LejosAstTest {

    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(BrickKey.ENTER)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/sensors/sensor_brick1.xml", false);
    }
}
