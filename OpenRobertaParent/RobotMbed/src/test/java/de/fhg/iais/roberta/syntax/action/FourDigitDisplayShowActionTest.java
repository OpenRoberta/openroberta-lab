package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMbedForXmlTest;

public class FourDigitDisplayShowActionTest {
    private final HelperMbedForXmlTest h = new HelperMbedForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfFourDigitDisplayShowActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=409, y=23], MainTask [], FourDigitDisplayShowAction [NumConst [1234], NumConst [0], BoolConst [true]]]]]";

        String result = this.h.generateTransformerString("/action/fourdigitdisplay_show.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/fourdigitdisplay_show.xml");
    }
}
