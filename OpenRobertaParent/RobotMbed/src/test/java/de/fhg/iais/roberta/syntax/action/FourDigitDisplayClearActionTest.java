package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class FourDigitDisplayClearActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfFourDigitDisplayClearActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=409, y=23], MainTask [], FourDigitDisplayClearAction []]]]";

        String result = this.h.generateTransformerString("/action/fourdigitdisplay_clear.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/fourdigitdisplay_clear.xml");
    }
}
