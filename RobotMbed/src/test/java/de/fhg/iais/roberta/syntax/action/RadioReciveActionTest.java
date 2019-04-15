package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class RadioReciveActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfRadioReciveActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "DisplayTextAction [TEXT, ActionExpr [BluetoothReceiveAction [STRING]]]]]]";

        String result = this.h.generateTransformerString("/action/radio_receive_message.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/radio_receive_message.xml");
    }

}
