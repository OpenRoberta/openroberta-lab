package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class PinTouchSensorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Ignore("Test is ignored until next commit")
    @Test
    public void make_ByDefault_ReturnInstanceOfPinTouchedSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [PinTouchSensor [0]]], DisplayTextAction [TEXT, SensorExpr [PinTouchSensor [2]]]]]]";

        String result = this.h.generateTransformerString("/sensor/pin_is_touched.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/pin_is_touched.xml");
    }

}
