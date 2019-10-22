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
                + "PinSetPullAction [UP, 0], "
                + "PinSetPullAction [DOWN, 1], "
                + "PinSetPullAction [NONE, 2]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/pin_set_pull.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/pin_set_pull.xml");
    }

}
