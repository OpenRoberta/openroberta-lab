package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.Helper;

public class PinWriteValueSensorTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=62, y=162], "
                + "MainTask [], "
                + "PinWriteValueSensor [0, ANALOG, NumConst [0]], PinWriteValueSensor [2, DIGITAL, NumConst [0]]]]]";

        String result = this.h.generateTransformerString("/action/write_value_to_pin.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/write_value_to_pin.xml");
    }

}
