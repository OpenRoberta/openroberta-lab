package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.nxt.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class SoundSensorTest {

    @Test
    public void sensorSound() throws Exception {
        String a = "BlockAST [project=[[Location [x=137, y=338], SoundSensor [port=IN_2]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_Sound.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_Sound.xml");

        SoundSensor<Void> cs = (SoundSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(SensorPort.IN_2, cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_Sound.xml");
    }
}
