package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ServoSetActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfServoSetActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "ServoSetAction [C04, NumConst[90]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/servo_set.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/servo_set.xml");
    }

}
