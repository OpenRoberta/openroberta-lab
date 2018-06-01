package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class GyroSensorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [GyroSensor [X, ANGLE, NO_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [GyroSensor [Y, ANGLE, NO_SLOT]]]]]]";

        String result = this.h.generateTransformerString("/sensor/gyro_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/gyro_sensor.xml");
    }

}
