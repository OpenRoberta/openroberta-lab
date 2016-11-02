package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class RadioSendActionTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRadioSendActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=38], " + "MainTask [], " + "RadioSendAction [ StringConst [Hallo] ]]]]";

        String result = Helper.generateTransformerString("/action/radio_send_message.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingTextMessage_InstanceOfRadioSendActionMissingMessage() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=38], MainTask [], RadioSendAction [ EmptyExpr [defVal=class java.lang.String] ]]]]";

        String result = Helper.generateTransformerString("/action/radio_send_missing_message.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/radio_send_message.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/radio_send_missing_message.xml");
    }
}
