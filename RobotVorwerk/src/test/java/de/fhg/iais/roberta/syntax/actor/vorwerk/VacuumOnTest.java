package de.fhg.iais.roberta.syntax.actor.vorwerk;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.VorwerkAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class VacuumOnTest extends VorwerkAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAnimationClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=384, y=50], " + "MainTask [], VacuumOn [NumConst [60]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/actors/vacuum_on.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/actors/vacuum_on.xml");
    }
}