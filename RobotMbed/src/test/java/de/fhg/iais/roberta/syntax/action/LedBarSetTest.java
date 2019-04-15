package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class LedBarSetTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfLedBarSetActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=382, y=50], MainTask [], LedBarSetAction [ NumConst [0], NumConst [5] ]]]]";

        String result = this.h.generateTransformerString("/action/ledbar_set.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/ledbar_set.xml");
    }
}
