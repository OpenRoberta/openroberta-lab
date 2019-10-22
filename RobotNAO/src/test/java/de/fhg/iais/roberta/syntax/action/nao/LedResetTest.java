package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LedResetTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLedOffClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], " + "MainTask [], " + "LedReset [led=HEAD]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/ledReset.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/ledReset.xml");
    }
}