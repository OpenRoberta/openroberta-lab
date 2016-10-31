package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class LedOnActionTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLedOnActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=37], "
                + "MainTask [], "
                + "LedOnAction [ LedColor [#ff0000] ], "
                + "LedOnAction [ LedColor [#009900] ], "
                + "LedOnAction [ LedColor [#9999ff] ]]]]";

        String result = Helper.generateTransformerString("/action/led_on_three_colors.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void make_MissingColor_InstanceOfLedOnActionClassWithMissingLedClor() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=163, y=62], MainTask [], LedOnAction [ EmptyExpr [defVal=class de.fhg.iais.roberta.syntax.expr.mbed.LedColor] ]]]]";

        String result = Helper.generateTransformerString("/action/led_on_missing_color.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/led_on_three_colors.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/action/led_on_missing_color.xml");
    }
}
