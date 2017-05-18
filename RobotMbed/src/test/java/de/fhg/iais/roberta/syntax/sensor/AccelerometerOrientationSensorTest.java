package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.Helper;

public class AccelerometerOrientationSensorTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerOrientationSensor [PITCH]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerOrientationSensor [ROLL]]]]]]";

        String result = this.h.generateTransformerString("/sensor/acceleration_orientation_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/acceleration_orientation_sensor.xml");
    }

}
