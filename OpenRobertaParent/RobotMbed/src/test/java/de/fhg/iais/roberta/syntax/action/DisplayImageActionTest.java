package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class DisplayImageActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayImageActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], "
                + "MainTask [], "
                + "DisplayImageAction [IMAGE, PredefinedImage [HEART]], "
                + "DisplayImageAction [ANIMATION, ListCreate [IMAGE, PredefinedImage [HEART_SMALL], PredefinedImage [ASLEEP]]"
                + "]]]]";

        String result = this.h.generateTransformerString("/action/display_image_show_imag_and_animation.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingMessage_InstanceOfDisplayTextActionClassWithMissingMessage() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=13, y=13], MainTask [], DisplayImageAction [IMAGE, EmptyExpr [defVal=STRING]]]]]";

        String result = this.h.generateTransformerString("/action/display_image_missing_image_name.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/display_image_show_imag_and_animation.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/display_image_missing_image_name.xml");
    }
}
