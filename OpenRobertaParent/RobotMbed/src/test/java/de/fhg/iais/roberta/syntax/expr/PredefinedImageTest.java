package de.fhg.iais.roberta.syntax.expr;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMbedForXmlTest;

public class PredefinedImageTest {
    private final HelperMbedForXmlTest h = new HelperMbedForXmlTest();

    @Test
    public void make_ByDefault_ReturnTwoInstancesOfPredefinedImageClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [\n"
                + "exprStmt VarDeclaration [IMAGE, Element, PredefinedImage [HEART], true, true]\n"
                + "exprStmt VarDeclaration [IMAGE, Element2, PredefinedImage [FABULOUS], false, true]]]]]";

        String result = this.h.generateTransformerString("/expr/image_get_image_defined_as_global_variables.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/expr/image_get_image_defined_as_global_variables.xml");
    }

}
