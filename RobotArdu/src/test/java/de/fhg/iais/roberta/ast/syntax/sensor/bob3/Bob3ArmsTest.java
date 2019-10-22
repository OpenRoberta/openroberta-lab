package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.bob3.Bob3AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3ArmsTest extends Bob3AstTest {

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "void setup(){}void loop(){if((rob.getArm(1)>0)){}elseif((rob.getArm(2)==1)){}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/sensors/sensor_bob3Arms.xml", false);
    }
}
