package de.fhg.iais.roberta.syntax.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ImageTest {

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
        String result = Helper.generateTransformerString("/expr/image_create.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/expr/image_create.xml");
    }

}
