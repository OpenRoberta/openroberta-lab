package de.fhg.iais.roberta.syntax.functions;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ImageInvertFunctionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfImageInvertFunctionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageInvertFunction [PredefinedImage [HEART]]]"
                + "]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/function/image_invert_heart_image.xml");

    }

    @Test
    public void make_MissingImage_InstanceOfImageShiftFunctionWithMissingImage() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageInvertFunction [EmptyExpr [defVal=PREDEFINED_IMAGE]]]"
                + "]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/function/image_invert_missing_image.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/function/image_invert_heart_image.xml");
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingImage_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/function/image_invert_missing_image.xml");
    }
}
