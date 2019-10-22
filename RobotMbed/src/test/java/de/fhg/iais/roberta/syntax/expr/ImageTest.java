package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ImageTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstancesOfImageClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=12], MainTask [], "
                + "DisplayImageAction [IMAGE, Image ["
                + "[#, #,   ,   ,   ]\n"
                + "[  ,   ,   ,   , #]\n"
                + "[  , 3,   ,   ,   ]\n"
                + "[  ,   ,   , #,   ]\n"
                + "[  , 2,   ,   ,   ]"
                + "]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/expr/image_create.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/expr/image_create.xml");
    }

}
