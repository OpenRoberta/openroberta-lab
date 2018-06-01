package de.fhg.iais.roberta.syntax.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class ImageTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

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
        String result = this.h.generateTransformerString("/expr/image_create.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/expr/image_create.xml");
    }

}
