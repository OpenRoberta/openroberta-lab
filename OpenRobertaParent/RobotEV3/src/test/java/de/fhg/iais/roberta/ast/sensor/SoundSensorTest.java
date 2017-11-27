package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class SoundSensorTest {
    Helper h = new Helper();

    @Test
    public void sensorSound() throws Exception {
        String a = "BlockAST [project=[[Location [x=460, y=156], SoundSensor [DEFAULT, S1]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_getSampleSound.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_getSampleSound.xml");

        SoundSensor<Void> cs = (SoundSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(SensorPort.S1, cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_getSampleSound.xml");
    }
}
