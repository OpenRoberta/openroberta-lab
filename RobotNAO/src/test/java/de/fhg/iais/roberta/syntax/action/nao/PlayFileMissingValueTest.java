package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PlayFileMissingValueTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPlayFileClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=138], " + "MainTask [], " + "PlayFile [EmptyExpr [defVal=STRING]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/playFile_missingValue.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/playFile_missingValue.xml");
    }
}