package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PointAtTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtRobotClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [ROBOT, POINT, NumConst [0], NumConst [0], NumConst [0], NumConst [0]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/pointAt_robot.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtWorldClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [WORLD, POINT, NumConst [0], NumConst [0], NumConst [0], NumConst [0]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/pointAt_world.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtTorsoClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [TORSO, POINT, NumConst [0], NumConst [0], NumConst [0], NumConst [0]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/pointAt_torso.xml");

    }

    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
    
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/pointAt_robot.xml");
    }*/
}