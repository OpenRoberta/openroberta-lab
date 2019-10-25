package de.fhg.iais.roberta.syntax.codegen.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PythonVisitorTest extends NaoAstTest {

    @Test
    public void rgbColorVisit_returnsCorrectPythonCodeConvertingRgb2Hex() throws Exception {
        String correct_code =
            "___item=int(\"{:02x}{:02x}{:02x}\".format(min(max(0,0),255),min(max(100,0),255),min(max(68,0),255),16))defrun():h.setAutonomousLife('ON')global___item";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/expr/create_rgb_variable.xml", false);
    }

    @Test
    public void rgbAccelerometerVisit_returnsCorrectPythonCodeGettingValueFromAccelerometerSensor() throws Exception {
        String correct_code = "defrun():h.setAutonomousLife('ON')h.walk(h.accelerometer('x'), 0, 0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/accelerometer.xml", false);
    }

    @Test
    public void rgbGyroSensorVisit_returnsCorrectPythonCodeGettingValueFromXaxis() throws Exception {
        String correct_code = "defrun():h.setAutonomousLife('ON')h.walk(h.gyrometer('x'), 0, 0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/gyrometer.xml", false);
    }

    @Test
    public void rgbUltrasonicSensorVisit_returnsCorrectPythonCodeGettingDistance() throws Exception {
        String correct_code = "defrun():h.setAutonomousLife('ON')h.walk(h.ultrasonic(), 0, 0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/ultrasonic.xml", false);
    }

    @Test
    public void touchSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n"
                + "    h.setAutonomousLife('ON')"
                + "    h.say(str(h.touchsensors('head', 'front')))\n"
                + "    h.say(str(h.touchsensors('head', 'middle')))\n"
                + "    h.say(str(h.touchsensors('head', 'rear')))\n"
                + "    h.say(str(h.touchsensors('hand', 'left')))\n"
                + "    h.say(str(h.touchsensors('hand', 'right')))\n"
                + "    h.say(str(h.touchsensors('bumper', 'left')))\n"
                + "    h.say(str(h.touchsensors('bumper', 'right')))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/touch.xml", false);
    }

    @Test
    public void fsrSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.setAutonomousLife('ON')"
                + "    h.say(str(h.fsr('left')))\n"
                + "    h.say(str(h.fsr('right')))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/fsr.xml", false);
    }

    @Test
    public void detectFaceSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.setAutonomousLife('OFF')"
                + "    h.say(str(faceRecognitionModule.detectFace()))\n"
                + "    h.say(str(faceRecognitionModule.detectFaces()))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/facedetection.xml", false);
    }

    @Test
    public void detectedFaceInformationVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.setAutonomousLife('OFF')"
                + "    h.say(str(faceRecognitionModule.getFaceInformation(\"Roberta\")))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/faceinformation.xml", false);
    }

    @Test
    public void detectedMarkInformationVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.setAutonomousLife('OFF')"
                + "    h.say(str(h.getNaoMarkInformation(84)))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/markinformation.xml", false);
    }

    @Test
    public void detectMarkSensorVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n" //
                + "    h.setAutonomousLife('OFF')"
                + "    h.say(str(h.getDetectedMark()))\n"
                + "    h.say(str(h.getDetectedMarks()))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/detectmark.xml", false);
    }

    @Test
    public void electricCurrentVisit_returnsCorrectPythonCode() throws Exception {
        String correct_code =
            "def run():\n"
                + "    h.setAutonomousLife('ON')"
                + "    h.say(str(h.getElectricCurrent('HeadYaw')))\n"
                + "    h.say(str(h.getElectricCurrent('HeadPitch')))\n"
                + "    h.say(str(h.getElectricCurrent('LShoulderPitch')))\n"
                + "    h.say(str(h.getElectricCurrent('LShoulderRoll')))\n"
                + "    h.say(str(h.getElectricCurrent('RShoulderPitch')))\n"
                + "    h.say(str(h.getElectricCurrent('RShoulderRoll')))\n"
                + "    h.say(str(h.getElectricCurrent('LElbowYaw')))\n"
                + "    h.say(str(h.getElectricCurrent('LElbowRoll')))\n"
                + "    h.say(str(h.getElectricCurrent('RElbowYaw')))\n"
                + "    h.say(str(h.getElectricCurrent('RElbowRoll')))\n"
                + "    h.say(str(h.getElectricCurrent('LWristYaw')))\n"
                + "    h.say(str(h.getElectricCurrent('RWristYaw')))\n"
                + "    h.say(str(h.getElectricCurrent('LHand')))\n"
                + "    h.say(str(h.getElectricCurrent('RHand')))\n"
                + "    h.say(str(h.getElectricCurrent('LHipYawPitch')))\n"
                + "    h.say(str(h.getElectricCurrent('LHipRoll')))\n"
                + "    h.say(str(h.getElectricCurrent('LHipPitch')))\n"
                + "    h.say(str(h.getElectricCurrent('RHipYawPitch')))\n"
                + "    h.say(str(h.getElectricCurrent('RHipRoll')))\n"
                + "    h.say(str(h.getElectricCurrent('RHipPitch')))\n"
                + "    h.say(str(h.getElectricCurrent('LKneePitch')))\n"
                + "    h.say(str(h.getElectricCurrent('RKneePitch')))\n"
                + "    h.say(str(h.getElectricCurrent('LAnklePitch')))\n"
                + "    h.say(str(h.getElectricCurrent('LAnkleRoll')))\n"
                + "    h.say(str(h.getElectricCurrent('RAnklePitch')))\n"
                + "    h.say(str(h.getElectricCurrent('RAnkleRoll')))";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, correct_code, "/sensor/electriccurrent.xml", false);
    }
}
