package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DetectMarkTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfNaoMarkClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=232, y=251], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [DetectMarkSensor [NO_PORT, IDONE, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [DetectMarkSensor [NO_PORT, IDALL, EMPTY_SLOT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/detectmark.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/detectmark.xml");
    }
}