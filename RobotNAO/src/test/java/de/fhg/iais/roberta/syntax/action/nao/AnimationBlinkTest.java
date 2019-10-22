package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class AnimationBlinkTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=88], " + "MainTask [], " + "Animation [BLINK]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/animation_blink.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/animation_blink.xml");
    }
}