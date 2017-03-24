package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class AccelerometerSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=63], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [X]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [Y]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [Z]]], "
                + "DisplayTextAction [TEXT, SensorExpr [AccelerometerSensor [STRENGTH]]]]]]";

        String result = Helper.generateTransformerString("/sensor/acceleration_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/sensor/acceleration_sensor.xml");
    }

}
