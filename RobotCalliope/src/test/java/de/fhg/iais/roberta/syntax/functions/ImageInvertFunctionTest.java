package de.fhg.iais.roberta.syntax.functions;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ImageInvertFunctionTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfImageInvertFunctionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageInvertFunction [PredefinedImage [HEART]]]"
                + "]]]]";

        String result = Helper.generateTransformerString("/function/image_invert_heart_image.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingImage_InstanceOfImageShiftFunctionWithMissingImage() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageInvertFunction [EmptyExpr [defVal=class de.fhg.iais.roberta.syntax.expr.PredefinedImage]]]"
                + "]]]]";

        String result = Helper.generateTransformerString("/function/image_invert_missing_image.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        //Helper.assertTransformationIsOk("/function/image_invert_heart_image.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingImage_ReturnsSameXML() throws Exception {
        //Helper.assertTransformationIsOk("/function/image_invert_missing_image.xml");
    }
}
