package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MessageTest extends EdisonAstTest {

    private String insertIntoResult(String s) {
        return "BlockAST [project=" + s + "]";
    }

    //Messages

    @Test
    public void TestSend() throws Exception {
        String expected = insertIntoResult("[[Location [x=408, y=162], BluetoothSendAction [EmptyExpr [defVal=NULL], EmptyExpr [defVal=STRING], -1]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/etc/message_send.xml");
    }

    @Test
    public void TestReceive() throws Exception {
        String expected = insertIntoResult("[[Location [x=429, y=207], BluetoothReceiveAction [connection=EmptyExpr [defVal=NULL], -1, Number]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/etc/message_receive.xml");
    }
}
