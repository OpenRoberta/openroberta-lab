package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class StiffnessOnTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessBodyOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [BODY, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfBodyOn.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessArmsOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [ARMS, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfArmsOn.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftArmOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTARM, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfLeftArmOn.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessHeadOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [HEAD, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfHeadOn.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightArmOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIGHTARM, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfRightArmOn.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLegsOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEGS, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfLegsOn.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftLegOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTLEG, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfLeftLegOn.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightLegOnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIHTLEG, ON]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfRightLegOn.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/stiffnessOfBodyOn.xml");
    }
}