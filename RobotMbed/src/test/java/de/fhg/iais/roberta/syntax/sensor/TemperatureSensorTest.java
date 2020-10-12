package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TemperatureSensorTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfTemperatureSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=187, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [TemperatureSensor [_T, VALUE, EMPTY_SLOT]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/get_temperature.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/get_temperature.xml");
    }

}
