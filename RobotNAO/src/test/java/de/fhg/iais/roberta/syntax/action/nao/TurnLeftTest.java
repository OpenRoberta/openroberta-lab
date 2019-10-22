package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TurnLeftTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfTurnClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "TurnDegrees [LEFT, NumConst [30]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/turnLeftThirty.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/turnLeftThirty.xml");
    }
}