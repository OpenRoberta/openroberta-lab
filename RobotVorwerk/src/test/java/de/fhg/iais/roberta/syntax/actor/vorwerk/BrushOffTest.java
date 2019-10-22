package de.fhg.iais.roberta.syntax.actor.vorwerk;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.VorwerkAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BrushOffTest extends VorwerkAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=384, y=50], " + "MainTask [], BrushOff []]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/actors/brush_off.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/actors/brush_off.xml");
    }
}