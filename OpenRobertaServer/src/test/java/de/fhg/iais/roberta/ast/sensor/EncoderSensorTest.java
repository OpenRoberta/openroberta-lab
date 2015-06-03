package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.MotorTachoMode;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;
import de.fhg.iais.roberta.codegen.lejos.Helper;

public class EncoderSensorTest {

    @Test
    public void sensorSetEncoder() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-20, y=94], DrehSensor [mode=ROTATION, motor=A]], "
                + "[Location [x=-15, y=129], DrehSensor [mode=DEGREE, motor=D]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setEncoder.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) transformer.getTree().get(0).get(1);
        EncoderSensor<Void> cs1 = (EncoderSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(MotorTachoMode.ROTATION, cs.getMode());
        Assert.assertEquals(MotorTachoMode.DEGREE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) transformer.getTree().get(0).get(1);
        EncoderSensor<Void> cs1 = (EncoderSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(ActorPort.A, cs.getMotor());
        Assert.assertEquals(ActorPort.D, cs1.getMotor());
    }

    @Test
    public void sensorResetEncoder() throws Exception {
        String a = "BlockAST [project=[[Location [x=-40, y=105], DrehSensor [mode=RESET, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_resetEncoder.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void reverseTransformation3() throws Exception {
        Helper.assertTransformationIsOk("/ast/sensors/sensor_resetEncoder.xml");
    }
}
