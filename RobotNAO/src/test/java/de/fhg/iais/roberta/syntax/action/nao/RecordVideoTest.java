package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class RecordVideoTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfRecordVideoClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=63], " + "MainTask [], " + "RecordVideo [LOW, TOP, NumConst [5], StringConst [RobertaVideo]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/recordVideo.xml");

    }

    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
    
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/recordVideo.xml");
    }*/
}