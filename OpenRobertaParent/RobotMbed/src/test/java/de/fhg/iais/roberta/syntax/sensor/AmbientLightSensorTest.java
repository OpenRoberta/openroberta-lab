package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class AmbientLightSensorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void make_ByDefault_ReturnInstanceOfLightSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=163, y=62], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [LightSensor [NO_PORT, DEFAULT, NO_SLOT]]]]]]";

        String result = this.h.generateTransformerString("/sensor/get_ambient_light.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        this.h.assertTransformationIsOk("/sensor/get_ambient_light.xml");
    }

}
