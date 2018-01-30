package de.fhg.iais.roberta.syntax.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.mbed.HelperMbedForTest;

public class PinWriteValueSensorTest {
    HelperMbedForTest h = new HelperMbedForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void make_ByDefault_ReturnInstanceOfPinValueSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=116, y=42], "
                + "MainTask [], "
                + "PinWriteValueSensor [ANALOG, S1, NumConst [1]], PinWriteValueSensor [DIGITAL, S0, NumConst [1]]]]]";

        String result = this.h.generateTransformerString("/action/write_value_to_pin.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/action/write_value_to_pin.xml");
    }

}
