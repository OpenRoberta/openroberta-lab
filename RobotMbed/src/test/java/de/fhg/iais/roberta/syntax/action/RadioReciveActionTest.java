package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class RadioReciveActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRadioReciveActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=38, y=38], " + "MainTask [], " + "DisplayTextAction [TEXT, ActionExpr [BluetoothReceiveAction [STRING]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/radio_receive_message.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/radio_receive_message.xml");
    }

}
