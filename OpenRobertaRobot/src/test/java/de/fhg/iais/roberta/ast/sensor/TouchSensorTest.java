package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.ev3.TouchSensor;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class TouchSensorTest {

    @Test
    public void sensorTouch() throws Exception {
        String a = "BlockAST [project=[[Location [x=-86, y=1], TouchSensor [port=S1]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_Touch.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_Touch.xml");

        TouchSensor<Void> cs = (TouchSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(SensorPort.S1, cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_Touch.xml");
    }
}
