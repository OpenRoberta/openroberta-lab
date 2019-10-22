package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LedOffTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLedOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], " + "MainTask [], " + "LedOff [led=ALL]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/ledOff.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/ledOff.xml");
    }
}