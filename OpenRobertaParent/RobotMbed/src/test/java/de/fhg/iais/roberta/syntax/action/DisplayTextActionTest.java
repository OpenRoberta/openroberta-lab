package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class DisplayTextActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayTextActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=63], MainTask [], DisplayTextAction [TEXT, StringConst [Hallo]], DisplayTextAction [CHARACTER, StringConst [H]]]]]";

        String result = this.h.generateTransformerString("/action/display_text_show_hello.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingMessage_InstanceOfDisplayTextActionClassWithMissingMessage() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], MainTask [], DisplayTextAction [TEXT, EmptyExpr [defVal=STRING]]]]]";

        String result = this.h.generateTransformerString("/action/display_text_missing_message.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/display_text_show_hello.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/display_text_missing_message.xml");
    }
}
