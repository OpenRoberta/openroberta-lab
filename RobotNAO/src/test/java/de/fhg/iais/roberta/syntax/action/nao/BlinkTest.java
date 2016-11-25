package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class BlinkTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfBlinkActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=163, y=63], " + "MainTask [], " + "Blink []]]]";

        String result = Helper.generateTransformerString("/action/blink.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/blink.xml");
    }
}