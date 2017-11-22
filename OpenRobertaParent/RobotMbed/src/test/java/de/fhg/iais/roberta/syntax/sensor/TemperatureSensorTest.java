package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.Helper;

public class TemperatureSensorTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfTemperatureSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=187, y=38], " + "MainTask [], " + "DisplayTextAction [TEXT, SensorExpr [TemperatureSensor [DEFAULT, NO_PORT]]]]]]";

        String result = this.h.generateTransformerString("/sensor/get_temperature.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/get_temperature.xml");
    }

}
