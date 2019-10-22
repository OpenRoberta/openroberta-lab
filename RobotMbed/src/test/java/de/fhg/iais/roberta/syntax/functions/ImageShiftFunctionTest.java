package de.fhg.iais.roberta.syntax.functions;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ImageShiftFunctionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfImageShiftFunctionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [PredefinedImage [SILLY], NumConst [1], UP]]], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [PredefinedImage [SILLY], NumConst [2], DOWN]]"
                + "]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/function/image_shift_up_down.xml");

    }

    @Test
    public void make_MissingImagePosition_InstanceOfImageShiftFunctionWithMissingImagePosition() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [], "
                + "DisplayImageAction [IMAGE, FunctionExpr [ImageShiftFunction [EmptyExpr [defVal=PREDEFINED_IMAGE], EmptyExpr [defVal=NUMBER_INT], UP]]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/function/image_shift_missing_image_and_position.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/function/image_shift_up_down.xml");
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingImagePosition_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/function/image_shift_missing_image_and_position.xml");
    }
}
