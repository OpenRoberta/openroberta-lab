package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PinTouchSensorTest extends AstTest {

    @Ignore("Test is ignored until next commit")
    @Test
    public void make_ByDefault_ReturnInstanceOfPinTouchedSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [PinTouchSensor [0]]], DisplayTextAction [TEXT, SensorExpr [PinTouchSensor [2]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/pin_is_touched.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/pin_is_touched.xml");
    }

}
