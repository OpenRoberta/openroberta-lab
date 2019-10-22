package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WalkSpeedTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfWalkAsyncClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=88], " + "MainTask [], " + "WalkTo [NumConst [0], NumConst [0], NumConst [0]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/walk_speed.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/walk_speed.xml");
    }
}