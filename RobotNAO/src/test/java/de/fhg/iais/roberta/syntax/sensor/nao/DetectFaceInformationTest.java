package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DetectFaceInformationTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfDetectFaceInformationClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [DetectedFaceInformation [StringConst [Roberta]]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/faceinformation.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/faceinformation.xml");
    }
}