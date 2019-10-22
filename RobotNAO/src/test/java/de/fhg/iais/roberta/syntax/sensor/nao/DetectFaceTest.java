package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DetectFaceTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLearnFaceClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [DetectFaceSensor [NO_PORT, NAMEONE, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [DetectFaceSensor [NO_PORT, NAMEALL, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/facedetection.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/facedetection.xml");
    }
}