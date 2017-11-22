package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.Helper;

public class WaitStmtTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfGetSampleSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=113, y=87], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [MbedGetSampleSensor [BrickSensor [key=BUTTON_A, mode=IS_PRESSED]]], BoolConst [true]]]\n"
                + "AktionStmt [DisplayTextAction [TEXT, StringConst [Hallo]]]\n"
                + ")\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [MbedGetSampleSensor [TemperatureSensor [DEFAULT, NO_PORT]]], NumConst [20]]]\n"
                + "AktionStmt [DisplayTextAction [TEXT, StringConst [Hallo]]]\n"
                + ")]]]]";

        String result = this.h.generateTransformerString("/sensor/wait_stmt_two_cases.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/wait_stmt_two_cases.xml");
    }

}
