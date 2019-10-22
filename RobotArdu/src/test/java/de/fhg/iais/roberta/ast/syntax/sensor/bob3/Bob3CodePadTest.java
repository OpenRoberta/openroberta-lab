package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.bob3.Bob3AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3CodePadTest extends Bob3AstTest {

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "double ___item; void setup(){ ___item = 0; } void loop() { ___item = rob.getID();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_bob3CodePad.xml", false);
    }
}
