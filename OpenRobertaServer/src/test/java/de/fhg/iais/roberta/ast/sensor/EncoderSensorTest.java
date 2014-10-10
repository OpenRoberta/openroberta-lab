package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.MotorTachoMode;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class EncoderSensorTest {

    @Test
    public void sensorSetEncoder() throws Exception {
        String a = "BlockAST [project=[[Location [x=-33, y=1], DrehSensor [mode=ROTATION, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_setEncoder.xml"));
    }

    @Test
    public void getMode() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(MotorTachoMode.ROTATION, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_setEncoder.xml");

        EncoderSensor<Void> cs = (EncoderSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(ActorPort.A, cs.getMotor());
    }

    @Test
    public void sensorGetModeEncoder() throws Exception {
        String a = "BlockAST [project=[[Location [x=-33, y=55], DrehSensor [mode=GET_MODE, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getModeEncoder.xml"));
    }

    @Test
    public void sensorGetSampleEncoder() throws Exception {
        String a = "BlockAST [project=[[Location [x=-40, y=159], DrehSensor [mode=GET_SAMPLE, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleEncoder.xml"));
    }

    @Test
    public void sensorResetEncoder() throws Exception {
        String a = "BlockAST [project=[[Location [x=-40, y=105], DrehSensor [mode=RESET, motor=A]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_resetEncoder.xml"));
    }
}
