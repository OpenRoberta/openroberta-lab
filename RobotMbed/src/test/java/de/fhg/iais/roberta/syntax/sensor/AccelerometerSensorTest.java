package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class AccelerometerSensorTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=63], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [_A, VALUE, X]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [_A, VALUE, Y]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [_A, VALUE, Z]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [_A, VALUE, STRENGTH]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/acceleration_sensor.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/acceleration_sensor.xml");
    }

}
