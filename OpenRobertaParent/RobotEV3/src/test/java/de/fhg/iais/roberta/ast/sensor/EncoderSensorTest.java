package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class EncoderSensorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void sensorSetEncoder() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-20, y=94], EncoderSensor [A, ROTATION, NO_SLOT]], "
                + "[Location [x=-15, y=129], EncoderSensor [D, DEGREE, NO_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setEncoder.xml"));
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) transformer.getTree().get(0).get(1);
        EncoderSensor<Void> cs1 = (EncoderSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(MotorTachoMode.ROTATION, cs.getMode());
        Assert.assertEquals(MotorTachoMode.DEGREE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) transformer.getTree().get(0).get(1);
        EncoderSensor<Void> cs1 = (EncoderSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(new ActorPort("A", "MA"), cs.getPort());
        Assert.assertEquals(new ActorPort("D", "MD"), cs1.getPort());
    }

    @Test
    public void sensorResetEncoder() throws Exception {
        String a = "BlockAST [project=[[Location [x=-40, y=105], EncoderSensor [A, RESET, NO_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_resetEncoder.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_resetEncoder.xml");
    }
}
