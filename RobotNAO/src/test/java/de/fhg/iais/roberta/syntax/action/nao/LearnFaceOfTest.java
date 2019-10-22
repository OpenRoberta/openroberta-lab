package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LearnFaceOfTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLearnFaceClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=63], " + "MainTask []], " + "[Location [x=87, y=113], " + "LearnFace [StringConst [roberta]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/learnFace.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/learnFace.xml");
    }
}