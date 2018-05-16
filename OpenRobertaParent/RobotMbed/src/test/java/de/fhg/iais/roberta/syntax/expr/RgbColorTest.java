package de.fhg.iais.roberta.syntax.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class RgbColorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstancesOfRgbColorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=163, y=37], MainTask [], "
                + "DisplayTextAction [TEXT, RgbColor [NumConst [20], NumConst [25], NumConst [30], NumConst [30]]]]]]";
        String result = this.h.generateTransformerString("/expr/create_color.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/expr/create_color.xml");
    }

}
