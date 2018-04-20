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
        String correct_code = "defrun():h.walk(h.accelerometer('x'), 0, 0)";

        this.h.assertCodeIsOk(correct_code, "/sensor/accelerometer.xml", false);
    }

    @Test
    public void rgbGyroSensorVisit_returnsCorrectPythonCodeGettingValueFromXaxis() throws Exception {
        String correct_code = "defrun():h.walk(h.gyrometer('x'), 0, 0)";

        this.h.assertCodeIsOk(correct_code, "/sensor/gyrometer.xml", false);
    }

    @Test
    public void rgbUltrasonicSensorVisit_returnsCorrectPythonCodeGettingDistance() throws Exception {
        String correct_code = "defrun():h.walk(h.ultrasonic(), 0, 0)";

        this.h.assertCodeIsOk(correct_code, "/sensor/ultrasonic.xml", false);
    }

    @Test
    public void touchSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n"
                + "    h.say(str(h.touchsensors('hand', 'left')))\n"
                + "    h.say(str(h.touchsensors('hand', 'right')))\n"
                + "    h.say(str(h.touchsensors('head', 'front')))\n"
                + "    h.say(str(h.touchsensors('head', 'middle')))\n"
                + "    h.say(str(h.touchsensors('head', 'rear')))\n"
                + "    h.say(str(h.touchsensors('bumper', 'left')))\n"
                + "    h.say(str(h.touchsensors('bumper', 'right')))";

        this.h.assertCodeIsOk(correct_code, "/sensor/touch.xml", false);
    }

    @Test
    public void fsrSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.say(str(h.fsr('left')))\n"
                + "    h.say(str(h.fsr('right')))";

        this.h.assertCodeIsOk(correct_code, "/sensor/fsr.xml", false);
    }

    @Test
    public void detectFaceSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.say(str(faceRecognitionModule.detectFace()))";

        this.h.assertCodeIsOk(correct_code, "/sensor/facedetection.xml", false);
    }

    @Test
    public void detectMarkSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.say(str(h.getDetectedMarks()))";

        this.h.assertCodeIsOk(correct_code, "/sensor/detectmark.xml", false);
    }
}
