package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class RandomEyesTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSetVolumeActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=163, y=63], " + "MainTask [], " + "RandomEyesDuration [NumConst [2]]]]]";

        String result = Helper.generateTransformerString("/action/randomEyes.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/randomEyes.xml");
    }
}