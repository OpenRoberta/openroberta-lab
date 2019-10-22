package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ColorSensorTest extends NxtAstTest {

    @Test
    public void setColor() throws Exception {
        //        final String a = "\nSensorColor(S3,\"COLOR\")SensorColor(S1,\"LIGHT\")SensorColor(S4,\"AMBIENTLIGHT\")";
        //        h.assertCodeIsOk(a, "/ast/sensors/sensor_setColor.xml");
        //    }
        String a =
            "BlockAST [project=[[Location [x=-15, y=107], ColorSensor [3, COLOUR, NO_SLOT]], [Location [x=-13, y=147], ColorSensor [1, LIGHT, NO_SLOT]],"
                + " [Location [x=-11, y=224], ColorSensor [4, AMBIENTLIGHT, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setColor.xml");
    }
}
