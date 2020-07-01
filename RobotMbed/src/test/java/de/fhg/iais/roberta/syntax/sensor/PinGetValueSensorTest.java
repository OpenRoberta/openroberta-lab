package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PinGetValueSensorTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=213, y=113], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [PinGetValueSensor [S_P1_A, ANALOG, NO_SLOT]]], DisplayTextAction [TEXT, SensorExpr [PinGetValueSensor [S_P0_D, DIGITAL, NO_SLOT]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/read_value_from_pin.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/read_value_from_pin.xml");
    }

}
