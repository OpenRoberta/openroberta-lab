package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class WalkDistanceTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfWalkDistanceActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=37], " + "MainTask [], " + "WalkDistance [FOREWARD, NumConst [2]]]]]";

        String result = Helper.generateTransformerString("/action/walk_forwards.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingDistance_InstanceOfWalkDistanceClassWithMissingMessage() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=37], " + "MainTask [], " + "WalkDistance [BACKWARD, EmptyExpr [defVal=class java.lang.Integer]]]]]";

        String result = Helper.generateTransformerString("/action/walk_backward_missing_distance.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/walk_forwards.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/walk_backward_missing_distance.xml");
    }
}
