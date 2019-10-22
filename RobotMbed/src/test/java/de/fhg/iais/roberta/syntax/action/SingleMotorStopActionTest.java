package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SingleMotorStopActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSingleMotorStopActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=113, y=63], "
                + "MainTask [], "
                + "SingleMotorStopAction [FLOAT], "
                + "SingleMotorStopAction [NONFLOAT], "
                + "SingleMotorStopAction [SLEEP]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/single_motor_stop.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/single_motor_stop.xml");
    }

}
