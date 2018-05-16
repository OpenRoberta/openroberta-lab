package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class RadioRssiSensorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfRadioRssiSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], " + "MainTask [], " + "DisplayTextAction [TEXT, SensorExpr [RadioRssiSensor []]]]]]";

        String result = this.h.generateTransformerString("/sensor/radio_rssi.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/radio_rssi.xml");
    }

}
