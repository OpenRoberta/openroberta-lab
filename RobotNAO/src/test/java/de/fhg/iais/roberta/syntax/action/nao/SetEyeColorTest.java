package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class SetEyeColorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSetEyeColorClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=88], " + "MainTask [], " + "SetEyeColor [GREEN]]]]";

        String result = Helper.generateTransformerString("/action/setEyeColor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/setEyeColor.xml");
    }

}
