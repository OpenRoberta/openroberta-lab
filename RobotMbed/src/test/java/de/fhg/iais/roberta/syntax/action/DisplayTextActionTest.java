package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class DisplayTextActionTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayTextActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=63], MainTask [], DisplayTextAction [TEXT, StringConst [Hallo]], DisplayTextAction [CHARACTER, StringConst [H]]]]]";

        String result = Helper.generateTransformerString("/action/display_text_show_hello.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingMessage_InstanceOfDisplayTextActionClassWithMissingMessage() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], MainTask [], DisplayTextAction [TEXT, EmptyExpr [defVal=STRING]]]]]";

        String result = Helper.generateTransformerString("/action/display_text_missing_message.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/display_text_show_hello.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/display_text_missing_message.xml");
    }
}
