package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class MoveAbsoluteTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveHeadYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [HEADYAW, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveHeadYawAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveHeadPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [HEADPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveHeadPitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftShoulderPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LSHOULDERPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftShoulderPitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftShoulderRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LSHOULDERROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftShoulderRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftElbowYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LELBOWYAW, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftElbowYawAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftElbowRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LELBOWROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftElbowRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftWristYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LWRISTYAW, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftWristYawAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHAND, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftHandAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHipYawPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHIPYAWPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftHipYawPitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHipRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHIPROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftHipRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftHipPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LHIPPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftHipPitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftKneePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LKNEEPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftKneePitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftAnklePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LANKLEPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftAnklePitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightAnkleRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RANKLEROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightAnkleRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHipYawPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHIPYAWPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightHipYawPitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHipRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHIPROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightHipRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    /*
    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHipPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHIPPITCH, ABSOLUTE, NumConst [10]]]]]";
        
        String result = this.h.generateTransformerString("/action/moveRightHipPitchAbsoluteTen.xml");
    
        Assert.assertEquals(expectedResult, result);
    }*/

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightKneePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RKNEEPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightKneePitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightAnklePitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RANKLEPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightAnklePitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveLeftAnkleRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [LANKLEROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveLeftAnkleRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightShoulderPitchClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RSHOULDERPITCH, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightShoulderPitchAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightShoulderRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RSHOULDERROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightShoulderRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightElbowYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RELBOWYAW, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightElbowYawAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightElbowRollClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RELBOWROLL, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightElbowRollAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightWristYawClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RWRISTYAW, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightWristYawAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfMoveRightHandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "MoveJoint [RHAND, ABSOLUTE, NumConst [10]]]]]";

        String result = this.h.generateTransformerString("/action/moveRightHandAbsoluteTen.xml");

        Assert.assertEquals(expectedResult, result);
    }

    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
    
        this.h.assertTransformationIsOk("/action/moveHeadYawAbsoluteTen.xml");
    }*/
}