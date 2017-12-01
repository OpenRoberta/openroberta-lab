package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.nxt.Helper;

public class LightSensorTest {
    Helper h = new Helper();

    @Test
    public void sensorSetLight() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=162, y=238], LightSensor [RED, S3]], " + "[Location [x=163, y=263], LightSensor [AMBIENTLIGHT, S4]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setLight.xml"));
    }

    @Test
    public void getMode() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setLight.xml");

        final LightSensor<Void> cs = (LightSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(LightSensorMode.RED, cs.getMode());

    }

    @Test
    public void getPort() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setLight.xml");

        final LightSensor<Void> cs = (LightSensor<Void>) transformer.getTree().get(0).get(1);
        final LightSensor<Void> cs1 = (LightSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(SensorPort.S3, cs.getPort());
        Assert.assertEquals(SensorPort.S4, cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setLight.xml");
    }
}
