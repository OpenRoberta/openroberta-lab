package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class TemperatureSensorTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfTemperatureSensorClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=187, y=38], " + "MainTask [], " + "DisplayTextAction [SensorExpr [TemperatureSensor []]]]]]";

        String result = Helper.generateTransformerString("/sensor/get_temperature.xml");

        Assert.assertEquals(expectedResult, result);
    }

    @Ignore
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        Helper.assertTransformationIsOk("/sensor/get_temperature.xml");
    }

}
