package de.fhg.iais.roberta.syntax.functions;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ImageShiftFunctionTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfImageShiftFunctionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [PredefinedImage [SILLY], NumConst [1], UP]]], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [PredefinedImage [SILLY], NumConst [2], DOWN]]"
                + "]]]]";

        String result = Helper.generateTransformerString("/function/image_shift_up_down.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingImagePosition_InstanceOfImageShiftFunctionWithMissingImagePosition() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [EmptyExpr [defVal=class de.fhg.iais.roberta.syntax.expr.PredefinedImage], EmptyExpr [defVal=class java.lang.Integer], UP]]]"
                + "]]]";

        String result = Helper.generateTransformerString("/function/image_shift_missing_image_and_position.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/function/image_shift_up_down.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingImagePosition_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/function/image_shift_missing_image_and_position.xml");
    }
}
