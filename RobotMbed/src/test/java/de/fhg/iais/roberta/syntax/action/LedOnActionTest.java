package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LedOnActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLedOnActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=138, y=37], "
                + "MainTask [], "
                + "LedOnAction [ _R, ColorConst [#ff0000] ], "
                + "LedOnAction [ _R, ColorConst [#009900] ], "
                + "LedOnAction [ _R, ColorConst [#9999ff] ]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/led_on_three_colors.xml");

    }

    @Test
    public void make_MissingColor_InstanceOfLedOnActionClassWithMissingLedClor() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=163, y=62], MainTask [], LedOnAction [ _R, EmptyExpr [defVal=COLOR] ]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/led_on_missing_color.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/led_on_three_colors.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXMLWithMissingMessage_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/led_on_missing_color.xml");
    }
}
