package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class DisplayImageActionTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfDisplayImageActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], "
                + "MainTask [], "
                + "DisplayImageAction [IMAGE, PredefinedImage [HEART]], "
                + "DisplayImageAction [ANIMATION, ListCreate [IMAGE, PredefinedImage [HEART_SMALL], PredefinedImage [ASLEEP]]"
                + "]]]]";

        String result = Helper.generateTransformerString("/action/display_image_show_imag_and_animation.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingMessage_InstanceOfDisplayTextActionClassWithMissingMessage() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], DisplayImageAction [IMAGE, EmptyExpr [defVal=class java.lang.String]]]]]";

        String result = Helper.generateTransformerString("/action/display_image_missing_image_name.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        //Helper.assertTransformationIsOk("/action/display_image_show_imag_and_animation.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        //Helper.assertTransformationIsOk("/action/display_image_missing_image_name.xml");
    }
}
