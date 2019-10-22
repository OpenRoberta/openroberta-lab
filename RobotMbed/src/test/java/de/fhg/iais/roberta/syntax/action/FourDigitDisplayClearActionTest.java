package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class FourDigitDisplayClearActionTest extends CalliopeAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfFourDigitDisplayClearActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=409, y=23], MainTask [], FourDigitDisplayClearAction []]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/fourdigitdisplay_clear.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/fourdigitdisplay_clear.xml");
    }
}
