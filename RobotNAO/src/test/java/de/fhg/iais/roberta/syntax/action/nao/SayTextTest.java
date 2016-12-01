package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class SayTextTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfTurnDegreesActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=37, y=62], " + "MainTask [], " + "SayText [StringConst [Hello]]]]]";

        String result = Helper.generateTransformerString("/action/sayText.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/sayText.xml");
    }

}
