package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class HumiditySensorTest extends AstTest {
    @Test
    public void make_ByDefault_ReturnInstanceOfTemperatureSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [HumiditySensor [H, HUMIDITY, EMPTY_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [HumiditySensor [H, TEMPERATURE, EMPTY_SLOT]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/humidity_sensor.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/humidity_sensor.xml");
    }
}
