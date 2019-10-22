package de.fhg.iais.roberta.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PredefinedImageTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnTwoInstancesOfPredefinedImageClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=13], MainTask [\n"
                + "exprStmt VarDeclaration [IMAGE, Element, PredefinedImage [HEART], true, true]\n"
                + "exprStmt VarDeclaration [IMAGE, Element2, PredefinedImage [FABULOUS], false, true]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/expr/image_get_image_defined_as_global_variables.xml");

    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/expr/image_get_image_defined_as_global_variables.xml");
    }

}
