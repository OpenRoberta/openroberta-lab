package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class SampleSensorTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();

    @Test
    public void sensorGetSample1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=16, y=-1], GetSampleSensor [TouchSensor [S1, TOUCH, EMPTY_SLOT]]], "
                + "[Location [x=36, y=39], GetSampleSensor [UltrasonicSensor [S4, DISTANCE, EMPTY_SLOT]]], "
                + "[Location [x=56, y=79], GetSampleSensor [UltrasonicSensor [S4, PRESENCE, EMPTY_SLOT]]], "
                + "[Location [x=76, y=119], GetSampleSensor [ColorSensor [S3, COLOUR, EMPTY_SLOT]]], "
                + "[Location [x=96, y=159], GetSampleSensor [ColorSensor [S3, LIGHT, EMPTY_SLOT]]], "
                + "[Location [x=116, y=199], GetSampleSensor [ColorSensor [S3, AMBIENTLIGHT, EMPTY_SLOT]]], "
                + "[Location [x=136, y=239], GetSampleSensor [InfraredSensor [S4, DISTANCE, EMPTY_SLOT]]], "
                + "[Location [x=156, y=279], GetSampleSensor [EncoderSensor [A, ROTATION, EMPTY_SLOT]]], "
                + "[Location [x=176, y=319], GetSampleSensor [EncoderSensor [A, DEGREE, EMPTY_SLOT]]], "
                + "[Location [x=196, y=359], GetSampleSensor [BrickSensor [ENTER, PRESSED, EMPTY_SLOT]]], "
                + "[Location [x=216, y=399], GetSampleSensor [GyroSensor [S2, ANGLE, EMPTY_SLOT]]], "
                + "[Location [x=236, y=439], GetSampleSensor [GyroSensor [S2, RATE, EMPTY_SLOT]]], "
                + "[Location [x=256, y=479], GetSampleSensor [TimerSensor [S1, VALUE, EMPTY_SLOT]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_getSampleSensor.xml"));
    }
}
