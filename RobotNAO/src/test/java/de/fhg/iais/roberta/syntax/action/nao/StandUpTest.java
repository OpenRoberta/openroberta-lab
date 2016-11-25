package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class StandUpTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfStandUpActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=38, y=88], " + "MainTask [], " + "StandUp []]]]";

        String result = Helper.generateTransformerString("/action/standUp.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/standUp.xml");
    }
}