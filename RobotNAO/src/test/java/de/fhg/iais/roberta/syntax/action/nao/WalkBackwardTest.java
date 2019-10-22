package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WalkBackwardTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfWalkClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "WalkDistance [BACKWARD, NumConst [70]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/walkBackwardsSeventy.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/walkBackwardsSeventy.xml");
    }
}