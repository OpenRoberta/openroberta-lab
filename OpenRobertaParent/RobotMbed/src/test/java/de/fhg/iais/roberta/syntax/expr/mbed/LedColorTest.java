package de.fhg.iais.roberta.syntax.expr.mbed;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class LedColorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstancesOfLedColorClass() throws Exception {
        String expectedResult =

            "BlockAST [project=[[Location [x=138, y=37], MainTask [\n"
                + "exprStmt VarDeclaration [COLOR, Element, LedColor [#0057a6], true, true]\n"
                + "exprStmt VarDeclaration [COLOR, Element2, LedColor [#b30006], true, true]\n"
                + "exprStmt VarDeclaration [COLOR, Element3, LedColor [#f7d117], false, true]]]]]";
        String result = this.h.generateTransformerString("/expr/led_color.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/expr/led_color.xml");
    }

}
