package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PinSetPullActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "PinSetPullAction [UP, P0_D], "
                + "PinSetPullAction [DOWN, P1_D], "
                + "PinSetPullAction [NONE, P2_D]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/pin_set_pull.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/pin_set_pull.xml");
    }

}
