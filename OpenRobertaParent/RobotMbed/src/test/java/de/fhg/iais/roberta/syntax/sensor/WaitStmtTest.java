package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class WaitStmtTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfGetSampleSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=80, y=92], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [BrickSensor [button_a, PRESSED, EMPTY_SLOT]]], BoolConst [true]]]\n"
                + ")], DisplayTextAction [TEXT, StringConst [Hallo]], WaitStmt [\n"
                + "(repeat [WAIT, Binary [GT, SensorExpr [GetSampleSensor [GyroSensor [X, ANGLE, EMPTY_SLOT]]], NumConst [90]]]\n"
                + ")], DisplayTextAction [TEXT, StringConst [Hallo]]]]]";

        String result = this.h.generateTransformerString("/sensor/wait_stmt_two_cases.xml");

        Assert.assertEquals(expectedResult, result);
    }

    // TODO: add generation of empty slot in xml
    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/wait_stmt_two_cases.xml");
    }

}
