package de.fhg.iais.roberta.syntax.codegen.nao;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class PythonVisitorTest {
    private final HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    @Test
    public void rgbColorVisit_returnsCorrectPythonCodeConvertingRgb2Hex() throws Exception {
        String correct_code = "item=BlocklyMethods.rgb2hex(0, 100, 68)defrun():globalitem";

        this.h.assertCodeIsOk(correct_code, "/expr/create_rgb_variable.xml", false);
    }

    @Test
    public void rgbAccelerometerVisit_returnsCorrectPythonCodeGettingValueFromAccelerometerSensor() throws Exception {
        String correct_code = "defrun():h.walk(h.accelerometer('X'), 0, 0)";

        this.h.assertCodeIsOk(correct_code, "/sensor/accelerometer.xml", false);
    }

    @Test
    public void rgbGyroSensorVisit_returnsCorrectPythonCodeGettingValueFromXaxis() throws Exception {
        String correct_code = "defrun():h.walk(h.gyrometer('X'), 0, 0)";

        this.h.assertCodeIsOk(correct_code, "/sensor/gyrometer.xml", false);
    }

    @Test
    public void rgbUltrasonicSensorVisit_returnsCorrectPythonCodeGettingDistance() throws Exception {
        String correct_code = "defrun():h.walk(h.ultrasonic(), 0, 0)";

        this.h.assertCodeIsOk(correct_code, "/sensor/ultrasonic.xml", false);
    }
}
