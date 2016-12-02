package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class WalkToTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfWalkDistanceActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=88], " + "MainTask [], " + "WalkTo [NumConst [0], NumConst [0], NumConst [0]]]]]";

        String result = Helper.generateTransformerString("/action/walk_to.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/walk_to.xml");
    }
}
