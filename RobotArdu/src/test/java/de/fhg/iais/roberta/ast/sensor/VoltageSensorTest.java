package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.Helper;

public class VoltageSensorTest {
    Helper h = new Helper();

    @Test
    public void voltageSensorJaxbToAstTransformation() throws Exception {
        String a = "BlockAST [project=[[Location [x=38, y=238], VoltageSensor []]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_Voltage.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_Voltage.xml");
    }

}
