package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class VoltageSensorTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void voltageSensorJaxbToAstTransformation() throws Exception {
        String a = "BlockAST [project=[[Location [x=38, y=238], Voltage [DEFAULT, NO_PORT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_Voltage.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_Voltage.xml");
    }

}
