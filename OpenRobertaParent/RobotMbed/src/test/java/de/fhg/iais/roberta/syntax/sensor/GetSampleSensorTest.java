package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.Helper;

public class GetSampleSensorTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfGetSampleSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=87], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [BrickSensor [key=BUTTON_A, mode=IS_PRESSED]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [BrickSensor [key=BUTTON_B, mode=IS_PRESSED]]], BoolConst [true]]]\n"
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
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [CompassSensor [NO_PORT, DEFAULT, NO_SLOT]]], NumConst [180]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [TimerSensor [S1, VALUE, NO_SLOT]]], NumConst [500]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [TemperatureSensor [NO_PORT, DEFAULT, NO_SLOT]]], NumConst [20]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [LightSensor [NO_PORT, DEFAULT, NO_SLOT]]], NumConst [50]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/get_sample_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/get_sample_sensor.xml");
    }

}
