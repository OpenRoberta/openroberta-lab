package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class CompassSensorTest {

    @Test
    public void sensorSetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=137, y=263], CompassSensor []]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_Compass.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_Compass.xml");
    }

}
