package de.fhg.iais.roberta.syntax.functions;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class ImageInvertFunctionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfImageInvertFunctionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageInvertFunction [PredefinedImage [HEART]]]"
                + "]]]]";

        String result = this.h.generateTransformerString("/function/image_invert_heart_image.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingImage_InstanceOfImageShiftFunctionWithMissingImage() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageInvertFunction [EmptyExpr [defVal=PREDEFINED_IMAGE]]]"
                + "]]]]";

        String result = this.h.generateTransformerString("/function/image_invert_missing_image.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/function/image_invert_heart_image.xml");
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingImage_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/function/image_invert_missing_image.xml");
    }
}
