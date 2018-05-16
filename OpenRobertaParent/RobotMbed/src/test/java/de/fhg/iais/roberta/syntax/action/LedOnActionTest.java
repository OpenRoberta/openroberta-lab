package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class LedOnActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfLedOnActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=37], "
                + "MainTask [], "
                + "LedOnAction [ LedColor [#ff0000] ], "
                + "LedOnAction [ LedColor [#009900] ], "
                + "LedOnAction [ LedColor [#9999ff] ]]]]";

        String result = this.h.generateTransformerString("/action/led_on_three_colors.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingColor_InstanceOfLedOnActionClassWithMissingLedClor() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=163, y=62], MainTask [], LedOnAction [ EmptyExpr [defVal=COLOR] ]]]]";

        String result = this.h.generateTransformerString("/action/led_on_missing_color.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/led_on_three_colors.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/led_on_missing_color.xml");
    }
}
