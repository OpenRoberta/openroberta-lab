package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GetLanguageTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfGetVolumeClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=138], " + "MainTask []]," + " [Location [x=213, y=188], " + "Get Language []]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/getLanguage.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/getLanguage.xml");
    }
}