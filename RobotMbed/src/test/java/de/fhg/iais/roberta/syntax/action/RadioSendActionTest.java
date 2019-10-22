package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class RadioSendActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRadioSendActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=38], " + "MainTask [], " + "RadioSendAction [ StringConst [Hallo], STRING, 0 ]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/radio_send_message.xml");

    }

    @Test
    public void make_MissingTextMessage_InstanceOfRadioSendActionMissingMessage() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=38], MainTask [], RadioSendAction [ EmptyExpr [defVal=STRING], STRING, 0 ]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/radio_send_missing_message.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/radio_send_message.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/radio_send_missing_message.xml");
    }
}
