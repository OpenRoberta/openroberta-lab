package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class LookAtTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLookAtActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=38, y=88], " + "MainTask [], " + "LookAt [TORSO, NumConst [0], NumConst [0], NumConst [0], NumConst [0]]]]]";

        String result = Helper.generateTransformerString("/action/lookAt_torso.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/lookAt_torso.xml");
    }
}
