package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class VoltageSensorTest {

    @Test
    public void sensorSetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=38, y=238], VoltageSensor []]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_Voltage.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_Voltage.xml");
    }

}
