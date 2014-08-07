package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensorMode;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class TimerSensorTest {

    @Test
    public void getMode() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_resetTimer.xml");

        TimerSensor cs = (TimerSensor) transformer.getTree().get(0);

        Assert.assertEquals(TimerSensorMode.RESET, cs.getMode());
    }

    @Test
    public void getTimer() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/sensors/sensor_resetTimer.xml");

        TimerSensor cs = (TimerSensor) transformer.getTree().get(0);

        Assert.assertEquals(1, cs.getTimer());
    }

    @Test
    public void sensorResetTimer() throws Exception {
        String a = "BlockAST [project=[[TimerSensor [mode=RESET, timer=1]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_resetTimer.xml"));
    }

    @Test
    public void sensorGetSampleTimer() throws Exception {
        String a = "BlockAST [project=[[TimerSensor [mode=GET_SAMPLE, timer=1]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/sensors/sensor_getSampleTimer.xml"));
    }
}
