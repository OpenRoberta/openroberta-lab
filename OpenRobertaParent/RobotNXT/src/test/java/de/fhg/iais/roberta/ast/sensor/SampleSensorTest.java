package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class SampleSensorTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void sensorGetSample1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=16, y=-1], GetSampleSensor [sensor=TouchSensor [S1, TOUCH, EMPTY_SLOT]]], "
                + "[Location [x=36, y=39], GetSampleSensor [sensor=UltrasonicSensor [S4, DISTANCE, EMPTY_SLOT]]], "
                + "[Location [x=56, y=79], GetSampleSensor [sensor=UltrasonicSensor [S4, PRESENCE, EMPTY_SLOT]]], "
                + "[Location [x=76, y=119], GetSampleSensor [sensor=ColorSensor [S3, COLOUR, EMPTY_SLOT]]], "
                + "[Location [x=96, y=159], GetSampleSensor [sensor=ColorSensor [S3, LIGHT, EMPTY_SLOT]]], "
                + "[Location [x=116, y=199], GetSampleSensor [sensor=ColorSensor [S3, AMBIENTLIGHT, EMPTY_SLOT]]], "
                + "[Location [x=136, y=239], GetSampleSensor [sensor=InfraredSensor [S4, DISTANCE, EMPTY_SLOT]]], "
                + "[Location [x=156, y=279], GetSampleSensor [sensor=EncoderSensor [A, ROTATION, EMPTY_SLOT]]], "
                + "[Location [x=176, y=319], GetSampleSensor [sensor=EncoderSensor [A, DEGREE, EMPTY_SLOT]]], "
                + "[Location [x=196, y=359], GetSampleSensor [sensor=BrickSensor [ENTER, PRESSED, EMPTY_SLOT]]], "
                + "[Location [x=216, y=399], GetSampleSensor [sensor=GyroSensor [S2, ANGLE, EMPTY_SLOT]]], "
                + "[Location [x=236, y=439], GetSampleSensor [sensor=GyroSensor [S2, RATE, EMPTY_SLOT]]], "
                + "[Location [x=256, y=479], GetSampleSensor [sensor=TimerSensor [S1, VALUE, EMPTY_SLOT]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_getSampleSensor.xml"));
    }
}
