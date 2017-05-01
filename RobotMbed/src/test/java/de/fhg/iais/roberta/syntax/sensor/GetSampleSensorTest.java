package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class GetSampleSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfGetSampleSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=13, y=87], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [BrickSensor [key=button_a, mode=IS_PRESSED]]], BoolConst [true]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [BrickSensor [key=button_b, mode=IS_PRESSED]]], BoolConst [true]]]\n"
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
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [CompassSensor []]], NumConst [180]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [TimerSensor [mode=GET_SAMPLE, timer=1]]], NumConst [500]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [TemperatureSensor []]], NumConst [20]]]\n"
                + ")], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [AmbientLightSensor []]], NumConst [50]]]\n"
                + ")]]]]";

        String result = Helper.generateTransformerString("/sensor/get_sample_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/sensor/get_sample_sensor.xml");
    }

}
