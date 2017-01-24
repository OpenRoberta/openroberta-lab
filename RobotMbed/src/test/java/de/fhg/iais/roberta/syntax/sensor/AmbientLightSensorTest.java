package de.fhg.iais.roberta.syntax.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class AmbientLightSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfAmbientLightSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=163, y=62], " + "MainTask [], " + "DisplayTextAction [TEXT, SensorExpr [AmbientLightSensor []]]]]]";

        String result = Helper.generateTransformerString("/sensor/get_ambient_light.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/sensor/get_ambient_light.xml");
    }

}
