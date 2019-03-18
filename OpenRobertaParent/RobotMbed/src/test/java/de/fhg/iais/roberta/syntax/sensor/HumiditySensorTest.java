package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;
import org.junit.Assert;
import org.junit.Test;

public class HumiditySensorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfTemperatureSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [HumiditySensor [5, HUMIDITY, EMPTY_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [HumiditySensor [5, TEMPERATURE, EMPTY_SLOT]]]]]]";

        String result = this.h.generateTransformerString("/sensor/humidity_sensor.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/humidity_sensor.xml");
    }
}
