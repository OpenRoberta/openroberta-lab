package de.fhg.iais.roberta.syntax.functions;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class ImageShiftFunctionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfImageShiftFunctionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [PredefinedImage [SILLY], NumConst [1], UP]]], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [PredefinedImage [SILLY], NumConst [2], DOWN]]"
                + "]]]]";

        String result = this.h.generateTransformerString("/function/image_shift_up_down.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingImagePosition_InstanceOfImageShiftFunctionWithMissingImagePosition() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [EmptyExpr [defVal=PREDEFINED_IMAGE], EmptyExpr [defVal=NUMBER_INT], UP]]]"
                + "]]]";

        String result = this.h.generateTransformerString("/function/image_shift_missing_image_and_position.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/function/image_shift_up_down.xml");
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingImagePosition_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/function/image_shift_missing_image_and_position.xml");
    }
}
