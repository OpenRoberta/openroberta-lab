package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DisplayTextActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayTextActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=63], MainTask [], DisplayTextAction [TEXT, StringConst [Hallo]], DisplayTextAction [CHARACTER, StringConst [H]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/display_text_show_hello.xml");

    }

    @Test
    public void make_MissingMessage_InstanceOfDisplayTextActionClassWithMissingMessage() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], MainTask [], DisplayTextAction [TEXT, EmptyExpr [defVal=STRING]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/display_text_missing_message.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/display_text_show_hello.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/display_text_missing_message.xml");
    }
}
