package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotionKitSingleSetActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfMotionKitSingleSetActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "MotionKitSingleSetAction [C17, FOREWARD],"
                + "MotionKitSingleSetAction [C16, BACKWARD],"
                + "MotionKitSingleSetAction [BOTH, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/motionkit_single_set.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/motionkit_single_set.xml");
    }

}
