package de.fhg.iais.roberta.ast.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SampleSensorTest extends AstTest {

    @Test
    public void sensorGetSample1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=16, y=-1], GetSampleSensor [TouchSensor [1, PRESSED, EMPTY_SLOT]]], "
                + "[Location [x=36, y=39], GetSampleSensor [UltrasonicSensor [4, DISTANCE, EMPTY_SLOT]]], "
                + "[Location [x=56, y=79], GetSampleSensor [UltrasonicSensor [4, PRESENCE, EMPTY_SLOT]]], "
                + "[Location [x=76, y=119], GetSampleSensor [ColorSensor [3, COLOUR, EMPTY_SLOT]]], "
                + "[Location [x=96, y=159], GetSampleSensor [ColorSensor [3, LIGHT, EMPTY_SLOT]]], "
                + "[Location [x=116, y=199], GetSampleSensor [ColorSensor [3, AMBIENTLIGHT, EMPTY_SLOT]]], "
                + "[Location [x=136, y=239], GetSampleSensor [InfraredSensor [4, DISTANCE, EMPTY_SLOT]]], "
                + "[Location [x=156, y=279], GetSampleSensor [EncoderSensor [A, ROTATION, EMPTY_SLOT]]], "
                + "[Location [x=176, y=319], GetSampleSensor [EncoderSensor [A, DEGREE, EMPTY_SLOT]]], "
                + "[Location [x=196, y=359], GetSampleSensor [KeysSensor [ENTER, PRESSED, EMPTY_SLOT]]], "
                + "[Location [x=216, y=399], GetSampleSensor [GyroSensor [2, ANGLE, EMPTY_SLOT]]], "
                + "[Location [x=236, y=439], GetSampleSensor [GyroSensor [2, RATE, EMPTY_SLOT]]], "
                + "[Location [x=256, y=479], GetSampleSensor [TimerSensor [1, VALUE, EMPTY_SLOT]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_getSampleSensor.xml");
    }
}
