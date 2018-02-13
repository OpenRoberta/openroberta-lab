package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMbedForXmlTest;

public class AccelerometerSensorTest {
    private final HelperMbedForXmlTest h = new HelperMbedForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=63], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [X, VALUE, EMPTY_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [Y, VALUE, EMPTY_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [Z, VALUE, EMPTY_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [STRENGTH, VALUE, EMPTY_SLOT]]]]]]";

        String result = this.h.generateTransformerString("/sensor/acceleration_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/acceleration_sensor.xml");
    }

}
