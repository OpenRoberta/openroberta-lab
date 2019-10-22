package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MoveAbsoluteTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveHeadYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [HEADYAW, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveHeadYawAbsoluteTen.xml");
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveHeadPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [HEADPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveHeadPitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftShoulderPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LSHOULDERPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftShoulderPitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftShoulderRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LSHOULDERROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftShoulderRollAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftElbowYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LELBOWYAW, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftElbowYawAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftElbowRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LELBOWROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftElbowRollAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftWristYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LWRISTYAW, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftWristYawAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHAND, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftHandAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHipYawPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHIPYAWPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftHipYawPitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHipRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHIPROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftHipRollAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHipPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHIPPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftHipPitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftKneePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LKNEEPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftKneePitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftAnklePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LANKLEPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftAnklePitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightAnkleRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RANKLEROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightAnkleRollAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHipYawPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHIPYAWPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightHipYawPitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHipRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHIPROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightHipRollAbsoluteTen.xml");

    }

    /*
    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHipPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHIPPITCH, ABSOLUTE, NumConst [10]]]]]";
    
        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightHipPitchAbsoluteTen.xml");
    
    
    }*/

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightKneePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RKNEEPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightKneePitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightAnklePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RANKLEPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightAnklePitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftAnkleRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LANKLEROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveLeftAnkleRollAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightShoulderPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RSHOULDERPITCH, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightShoulderPitchAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightShoulderRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RSHOULDERROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightShoulderRollAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightElbowYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RELBOWYAW, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightElbowYawAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightElbowRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RELBOWROLL, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightElbowRollAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightWristYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RWRISTYAW, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightWristYawAbsoluteTen.xml");

    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHAND, ABSOLUTE, NumConst [10]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/moveRightHandAbsoluteTen.xml");
    }

    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
    
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/moveHeadYawAbsoluteTen.xml");
    }*/
}