package de.fhg.iais.roberta.sound;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SetLanguageTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSetLanguageAction() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=88], " + "MainTask [], " + "SetLanguage [GERMAN]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/ast/actions/action_SetLanguage.xml");
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_SetLanguage.xml");
    }
}