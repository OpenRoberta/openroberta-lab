package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class StiffnessOffTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessBodyOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [BODY, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfBodyOff.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessArmsOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [ARMS, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfArmsOff.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftArmOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTARM, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfLeftArmOff.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessHeadOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [HEAD, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfHeadOff.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightArmOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIGHTARM, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfRightArmOff.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLegsOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEGS, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfLegsOff.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessLeftLegOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [LEFTLEG, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfLeftLegOff.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessRightLegOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetStiffness [RIHTLEG, OFF]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/stiffnessOfRightLegOff.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/stiffnessOfBodyOff.xml");
    }
}