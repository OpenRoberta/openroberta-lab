package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensorMode;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class TimerSensorTest {

    @Test
    public void getMode() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_resetTimer.xml");

        TimerSensor<Void> cs = (TimerSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(TimerSensorMode.RESET, cs.getMode());
    }

    @Test
    public void getTimer() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_resetTimer.xml");

        TimerSensor<Void> cs = (TimerSensor<Void>) transformer.getTree().get(1);

        Assert.assertEquals(1, cs.getTimer());
    }

    @Test
    public void sensorResetTimer() throws Exception {
        String a = "BlockAST [project=[[Location [x=-96, y=73], TimerSensor [mode=RESET, timer=1]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_resetTimer.xml"));
    }

    @Test
    public void sensorGetSampleTimer() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=1], TimerSensor [mode=GET_SAMPLE, timer=1]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleTimer.xml"));
    }
}
