package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.nxt.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.nxt.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class LightSensorTest {

    @Test
    public void sensorSetLight() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=162, y=238], LightSensor [mode=RED, port=IN_3]], "
                + "[Location [x=163, y=263], LightSensor [mode=AMBIENTLIGHT, port=IN_4]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setLight.xml"));
    }

    @Test
    public void getMode() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setLight.xml");

        final LightSensor<Void> cs = (LightSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(LightSensorMode.RED, cs.getMode());

    }

    @Test
    public void getPort() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setLight.xml");

        final LightSensor<Void> cs = (LightSensor<Void>) transformer.getTree().get(0).get(1);
        final LightSensor<Void> cs1 = (LightSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(SensorPort.IN_3, cs.getPort());
        Assert.assertEquals(SensorPort.IN_4, cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setLight.xml");
    }
}
