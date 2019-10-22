package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LookAtMissingNumbersTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtRobotClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [ROBOT, LOOK, EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/lookAt_robot_missingNumbers.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtWorldClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [WORLD, LOOK, EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/lookAt_world_missingNumbers.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtTorsoClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [TORSO, LOOK, EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/lookAt_torso_missingNumbers.xml");

    }

    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
    
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/pointAt_robot.xml");
    }*/
}