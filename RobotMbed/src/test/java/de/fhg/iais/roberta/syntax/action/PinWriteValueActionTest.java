package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PinWriteValueActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "PinWriteValueAction [ANALOG, P2, NumConst [1]], PinWriteValueAction [DIGITAL, A0, NumConst [1]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/write_value_to_pin.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/write_value_to_pin.xml");
    }

}
