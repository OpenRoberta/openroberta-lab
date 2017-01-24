package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class PinValueSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=213, y=113], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [PinValueSensor [0, ANALOG]]], DisplayTextAction [TEXT, SensorExpr [PinValueSensor [2, DIGITAL]]]]]]";

        String result = Helper.generateTransformerString("/sensor/read_value_from_pin.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/sensor/read_value_from_pin.xml");
    }

}
