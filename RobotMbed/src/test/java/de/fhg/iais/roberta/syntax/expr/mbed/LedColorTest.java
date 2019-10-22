package de.fhg.iais.roberta.syntax.expr.mbed;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LedColorTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstancesOfLedColorClass() throws Exception {
        String expectedResult =

            "BlockAST [project=[[Location [x=138, y=37], MainTask [\n"
                + "exprStmt VarDeclaration [COLOR, Element, ColorConst [#0057a6], true, true]\n"
                + "exprStmt VarDeclaration [COLOR, Element2, ColorConst [#b30006], true, true]\n"
                + "exprStmt VarDeclaration [COLOR, Element3, ColorConst [#f7d117], false, true]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/expr/led_color.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/expr/led_color.xml");
    }

}
