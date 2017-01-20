package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class PinTouchSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfPinTouchedSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=63, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [SensorExpr [PinTouchSensor [0]]], DisplayTextAction [SensorExpr [PinTouchSensor [2]]]]]]";

        String result = Helper.generateTransformerString("/sensor/pin_is_touched.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/sensor/pin_is_touched.xml");
    }

}
