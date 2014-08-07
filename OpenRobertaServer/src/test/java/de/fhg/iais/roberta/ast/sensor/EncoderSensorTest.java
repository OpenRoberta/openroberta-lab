package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.MotorTachoMode;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class EncoderSensorTest {

    @Test
    public void sensorSetEncoder() throws Exception {
        String a = "BlockAST [project=[[DrehSensor [mode=ROTATION, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_setEncoder.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor cs = (EncoderSensor) transformer.getTree().get(0);

        Assert.assertEquals(MotorTachoMode.ROTATION, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor cs = (EncoderSensor) transformer.getTree().get(0);

        Assert.assertEquals(ActorPort.A, cs.getMotor());
    }

    @Test
    public void sensorGetModeEncoder() throws Exception {
        String a = "BlockAST [project=[[DrehSensor [mode=GET_MODE, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_getModeEncoder.xml"));
    }

    @Test
    public void sensorGetSampleEncoder() throws Exception {
        String a = "BlockAST [project=[[DrehSensor [mode=GET_SAMPLE, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_getSampleEncoder.xml"));
    }

    @Test
    public void sensorResetEncoder() throws Exception {
        String a = "BlockAST [project=[[DrehSensor [mode=RESET, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_resetEncoder.xml"));
    }
}
