package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightSensorTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLightSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=63], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [LightSensor [_L, VALUE, EMPTY_SLOT]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/get_light_level.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/get_light_level.xml");
    }

}
