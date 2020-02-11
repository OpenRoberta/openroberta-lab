package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotionKitDualSetActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfMotionKitDualSetActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "MotionKitDualSetAction [FOREWARD, BACKWARD],"
                + "MotionKitDualSetAction [BACKWARD, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/motionkit_dual_set.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/motionkit_single_set.xml");
    }

}
