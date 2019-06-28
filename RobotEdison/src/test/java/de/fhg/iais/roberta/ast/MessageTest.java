package de.fhg.iais.roberta.ast;

import de.fhg.iais.roberta.util.test.edison.HelperEdisonForXmlTest;
import org.junit.Assert;
import org.junit.Test;

public class MessageTest {

    HelperEdisonForXmlTest h = new HelperEdisonForXmlTest();

    private String insertIntoResult(String s) {
        return "BlockAST [project=" + s + "]";
    }

    //Messages

    @Test
    public void TestSend() throws Exception {
        String expected = insertIntoResult("[[Location [x=408, y=162], BluetoothSendAction [EmptyExpr [defVal=NULL], EmptyExpr [defVal=STRING], -1]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/etc/message_send.xml"));
    }

    @Test
    public void TestReceive() throws Exception {
        String expected = insertIntoResult("[[Location [x=429, y=207], BluetoothReceiveAction [connection=EmptyExpr [defVal=NULL], -1, Number]]]");
        Assert.assertEquals(expected, this.h.generateTransformerString("/ast/etc/message_receive.xml"));
    }
}
