package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class PredefinedImageTest {

    @Test
    public void make_ByDefault_ReturnTwoInstancesOfPredefinedImageClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [\n"
                + "exprStmt VarDeclaration [IMAGE, Element, PredefinedImage [HEART], true, true]\n"
                + "exprStmt VarDeclaration [IMAGE, Element2, PredefinedImage [FABULOUS], false, true]]]]]";

        String result = Helper.generateTransformerString("/expr/image_get_image_defined_as_global_variables.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/expr/image_get_image_defined_as_global_variables.xml");
    }

}
