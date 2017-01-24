package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class RadioReciveActionTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRadioReciveActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "DisplayTextAction [TEXT, ActionExpr [BluetoothReceiveAction []]]]]]";

        String result = Helper.generateTransformerString("/action/radio_receive_message.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/radio_receive_message.xml");
    }

}
