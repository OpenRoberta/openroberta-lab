package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class LookAtTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtRobotClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [ROBOT, LOOK, NumConst [0], NumConst [0], NumConst [0], NumConst [0]]]]]";

        String result = this.h.generateTransformerString("/action/lookAt_robot.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtWorldClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [WORLD, LOOK, NumConst [0], NumConst [0], NumConst [0], NumConst [0]]]]]";

        String result = this.h.generateTransformerString("/action/lookAt_world.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfPointLookAtTorsoClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=88], "
                + "MainTask [], "
                + "PointLookAt [TORSO, LOOK, NumConst [0], NumConst [0], NumConst [0], NumConst [0]]]]]";

        String result = this.h.generateTransformerString("/action/lookAt_torso.xml");

        Assert.assertEquals(expectedResult, result);
    }

    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
    
        this.h.assertTransformationIsOk("/action/lookAt_robot.xml");
    }*/
}