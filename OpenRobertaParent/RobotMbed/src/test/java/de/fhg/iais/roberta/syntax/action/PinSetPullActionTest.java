package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMbedForXmlTest;

public class PinSetPullActionTest {
    private final HelperMbedForXmlTest h = new HelperMbedForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "PinSetPullAction [UP, S0], "
                + "PinSetPullAction [DOWN, S1], "
                + "PinSetPullAction [NONE, S2]]]]";

        String result = this.h.generateTransformerString("/action/pin_set_pull.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/pin_set_pull.xml");
    }

}
