package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WalkForwardTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfWalkClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "WalkDistance [FOREWARD, NumConst [20]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/walkForwardsTwenty.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/walkForwardsTwenty.xml");
    }
}