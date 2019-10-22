package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ModeActiveTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfSetModeClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=63], " + "MainTask [], " + "Mode [ACTIVE]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/setModeActive.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/setModeActive.xml");
    }
}