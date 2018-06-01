package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class AccelerometerSensorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=63], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [X, VALUE, NO_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [Y, VALUE, NO_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [Z, VALUE, NO_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [STRENGTH, VALUE, NO_SLOT]]]]]]";

        String result = this.h.generateTransformerString("/sensor/acceleration_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/acceleration_sensor.xml");
    }

}
