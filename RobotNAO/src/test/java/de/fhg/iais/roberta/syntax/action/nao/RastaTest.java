package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class RastaTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRastaClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], " + "MainTask [], " + "RastaDuration [NumConst [5000]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/rasta.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/rasta.xml");
    }
}