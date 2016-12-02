package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class SetEarIntensityTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSetEarIntensityClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "SetEarIntensity [NumConst [50]]]]]";

        String result = Helper.generateTransformerString("/action/setEarIntensity.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/setEarIntensity.xml");
    }
}
