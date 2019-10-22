package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TouchSensorTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLearnFaceClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [TouchSensor [HEAD, PRESSED, FRONT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [TouchSensor [HEAD, PRESSED, MIDDLE]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [TouchSensor [HEAD, PRESSED, REAR]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [TouchSensor [HAND, PRESSED, LEFT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [TouchSensor [HAND, PRESSED, RIGHT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [TouchSensor [BUMPER, PRESSED, LEFT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [TouchSensor [BUMPER, PRESSED, RIGHT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/touch.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/touch.xml");
    }
}