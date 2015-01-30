package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class SampleSensorTest {

    @Test
    public void sensorGetSample1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=16, y=-1], GetSampleSensor [sensor=TouchSensor [port=S1]]], "
                + "[Location [x=36, y=39], GetSampleSensor [sensor=UltraSSensor [mode=DISTANCE, port=S4]]], "
                + "[Location [x=56, y=79], GetSampleSensor [sensor=UltraSSensor [mode=PRESENCE, port=S4]]], "
                + "[Location [x=76, y=119], GetSampleSensor [sensor=ColorSensor [mode=COLOUR, port=S3]]], "
                + "[Location [x=96, y=159], GetSampleSensor [sensor=ColorSensor [mode=RED, port=S3]]], "
                + "[Location [x=116, y=199], GetSampleSensor [sensor=ColorSensor [mode=AMBIENTLIGHT, port=S3]]], "
                + "[Location [x=136, y=239], GetSampleSensor [sensor=InfraredSensor [mode=DISTANCE, port=S4]]], "
                + "[Location [x=156, y=279], GetSampleSensor [sensor=DrehSensor [mode=ROTATION, motor=A]]], "
                + "[Location [x=176, y=319], GetSampleSensor [sensor=DrehSensor [mode=DEGREE, motor=A]]], "
                + "[Location [x=196, y=359], GetSampleSensor [sensor=BrickSensor [key=ENTER, mode=IS_PRESSED]]], "
                + "[Location [x=216, y=399], GetSampleSensor [sensor=GyroSensor [mode=ANGLE, port=S2]]], "
                + "[Location [x=236, y=439], GetSampleSensor [sensor=GyroSensor [mode=RATE, port=S2]]], "
                + "[Location [x=256, y=479], GetSampleSensor [sensor=TimerSensor [mode=GET_SAMPLE, timer=1]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSampleSensor.xml"));
    }
}
