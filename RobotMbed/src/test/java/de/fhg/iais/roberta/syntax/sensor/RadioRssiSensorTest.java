package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class RadioRssiSensorTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRadioRssiSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], " + "MainTask [], " + "DisplayTextAction [TEXT, SensorExpr [RadioRssiSensor []]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/radio_rssi.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/radio_rssi.xml");
    }

}
