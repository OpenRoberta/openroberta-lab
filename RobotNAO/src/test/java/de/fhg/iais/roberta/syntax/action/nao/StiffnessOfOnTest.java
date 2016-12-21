package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class StiffnessOfOnTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfStiffnessOffActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "PartialStiffnessOn [ARM]]]]";

        String result = Helper.generateTransformerString("/action/stiffnessOfOn.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/stiffnessOfOn.xml");
    }
}