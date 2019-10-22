package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TurnRightTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfTurnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "TurnDegrees [RIGHT, NumConst [60]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/turnRightSixty.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/turnRightSixty.xml");
    }
}