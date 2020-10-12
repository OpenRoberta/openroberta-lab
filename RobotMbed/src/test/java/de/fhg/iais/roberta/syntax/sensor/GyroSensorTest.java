package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GyroSensorTest extends AstTest {
    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [GyroSensor [_G, ANGLE, X]]], "
                + "DisplayTextAction [TEXT, SensorExpr [GyroSensor [_G, ANGLE, Y]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/gyro_sensor.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/gyro_sensor.xml");
    }

}
