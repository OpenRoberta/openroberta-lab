package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SingleMotorOnActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSingleMotorOnActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=138], "
                + "MainTask [], "
                + "SingleMotorOnAction [NumConst [0]], "
                + "SingleMotorOnAction [NumConst [14]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/single_motor_on.xml");

    }

    @Test
    public void make_MissingColor_InstanceOfSingleMotorOnActionClassWithMissingMotorPower() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=13, y=138], " + "MainTask [], " + "SingleMotorOnAction [EmptyExpr [defVal=NUMBER_INT]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/single_motor_on_missing_power.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/single_motor_on.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/single_motor_on_missing_power.xml");
    }
}
