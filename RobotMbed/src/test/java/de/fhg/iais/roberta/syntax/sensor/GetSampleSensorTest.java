package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GetSampleSensorTest extends AstTest {

    @Ignore("Test is ignored until next commit")
    @Test
    public void make_ByDefault_ReturnInstanceOfGetSampleSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=87], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [KeysSensor [BUTTON_A, PRESSED, EMPTY_SLOT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [KeysSensor [BUTTON_B, PRESSED, EMPTY_SLOT]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [GestureSensor [ UP ]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [GestureSensor [ DOWN ]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [GestureSensor [ FACE_UP ]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [GestureSensor [ FACE_DOWN ]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [GestureSensor [ SHAKE ]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [GestureSensor [ FREEFALL ]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [CompassSensor [NO_PORT, DEFAULT, EMPTY_SLOT]]], NumConst [180]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [TimerSensor [1, VALUE, EMPTY_SLOT]]], NumConst [500]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [TemperatureSensor [NO_PORT, DEFAULT, EMPTY_SLOT]]], NumConst [20]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [LightSensor [NO_PORT, DEFAULT, EMPTY_SLOT]]], NumConst [50]]]\n"
                + ")]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/get_sample_sensor.xml");

    }

    @Ignore("Test is ignored until next commit")
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/get_sample_sensor.xml");
    }

}
