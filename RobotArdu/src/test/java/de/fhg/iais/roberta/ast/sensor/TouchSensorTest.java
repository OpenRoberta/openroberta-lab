package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.ArduFactory;
import de.fhg.iais.roberta.mode.sensor.arduino.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.Helper;

public class TouchSensorTest {
    Helper h = new Helper();
    ArduFactory robotFactory = new ArduFactory();

    @Before
    public void setUp() throws Exception {
        this.h.setRobotFactory(this.robotFactory);
    }

    @Test
    public void sensorTouch() throws Exception {
        String a = "BlockAST [project=[[Location [x=-86, y=1], TouchSensor [port=S1]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_Touch.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_Touch.xml");

        TouchSensor<Void> cs = (TouchSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(SensorPort.S1, cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_Touch.xml");
    }
}
