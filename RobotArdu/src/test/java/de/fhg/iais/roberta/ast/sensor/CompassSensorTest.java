package de.fhg.iais.roberta.ast.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CompassSensorTest extends AstTest {

    @Test
    public void jaxb2astTransformation() throws Exception {
        String a = "BlockAST [project=[[Location [x=137, y=263], CompassSensor [NO_PORT, DEFAULT, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_Compass.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_Compass.xml");
    }

}
