package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BrickSensorTest extends BotnrollAstTest {

    @Test
    public void isPressed() throws Exception {
        String a = "\nbnr.buttonIsPressed(2)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_brick1.xml", false);
    }
}
