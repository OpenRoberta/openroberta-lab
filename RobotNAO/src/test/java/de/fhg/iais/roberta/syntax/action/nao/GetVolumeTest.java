package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GetVolumeTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfGetVolumeClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=88, y=88], " + "MainTask [], " + "SetVolume [ActionExpr [Get Volume []]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/getVolume.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/getVolume.xml");
    }
}