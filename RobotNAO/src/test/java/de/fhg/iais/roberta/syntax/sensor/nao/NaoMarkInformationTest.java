package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class NaoMarkInformationTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoMarkInformationClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=349, y=50], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [NaoMarkInformation [NumConst [84]]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/markinformation.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/markinformation.xml");
    }
}