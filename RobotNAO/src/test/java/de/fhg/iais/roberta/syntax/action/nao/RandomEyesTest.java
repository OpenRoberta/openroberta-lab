package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class RandomEyesTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRandomEyesClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], " + "MainTask [], " + "RandomEyesDuration [NumConst [2]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/randomEyes.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/randomEyes.xml");
    }
}