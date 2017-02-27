package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.components.CalliopeConfiguration;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.testutil.Helper;

public class CppCodeGeneratorVisitorTest {

    private static final String IMPORTS = //
        "#include \"MicroBit.h\"" //
            + "#include <array>\n"
            + "#include <stdlib.h>\n"
            + "MicroBituBit;"
            + "int initTime=uBit.systemTime();";

    private static final String MAIN = "int main() { uBit.init();";

    private static final String END = "release_fiber();}";

    private static CalliopeConfiguration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        @SuppressWarnings("rawtypes")
        Configuration.Builder configuration = new CalliopeConfiguration.Builder();
        brickConfiguration = (CalliopeConfiguration) configuration.build();
    }

    @Test
    public void visitMainTask_ByDefault_ReturnsEmptyCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + END;

        assertCodeIsOk(expectedResult, "/task/main_task_no_variables_empty.xml");
    }

    @Test
    public void visitDisplayText_ShowHelloScript_ReturnsCppProgramWithShowTextCall() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(\"Hallo\");"
            + "uBit.display.print(\"H\");"
            + END;

        assertCodeIsOk(expectedResult, "/action/display_text_show_hello.xml");
    }

    @Test
    public void visitPredefinedImage_ScriptWithToImageVariables_ReturnsCppProgramWithTwoImageVariables() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "MicroBitImage Element = MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\");"
            + "MicroBitImage Element2 = MicroBitImage(\"255,255,255,255,255\\n255,255,0,255,255\\n0,0,0,0,0\\n0,255,0,255,0\\n0,255,255,255,0\\n\");"
            + MAIN
            + END;

        assertCodeIsOk(expectedResult, "/expr/image_get_image_defined_as_global_variables.xml");
    }

    @Test
    public void visitDisplayImageAction_ScriptWithDisplayImageAndAnimation_ReturnsCppProgramWithDisplayImageAndAnimation() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.print(MicroBitImage(\"0,255,0,255,0\\n"
            + "255,255,255,255,255\\n"
            + "255,255,255,255,255\\n"
            + "0,255,255,255,0\\n"
            + "0,0,255,0,0\\n\"));"
            + "uBit.display.animateImages({MicroBitImage(\"0,0,0,0,0\\n"
            + "0,255,0,255,0\\n"
            + "0,255,255,255,0\\n"
            + "0,0,255,0,0\\n"
            + "0,0,0,0,0\\n\"), MicroBitImage(\"0,0,0,0,0\\n"
            + "255,255,0,255,255\\n"
            + "0,0,0,0,0\\n"
            + "0,255,255,255,0\\n"
            + "0,0,0,0,0\\n\")}, 200);"
            + END;

        assertCodeIsOk(expectedResult, "/action/display_image_show_imag_and_animation.xml");
    }

    @Test
    public void visitDisplayImageAction_ScriptWithMissinImageToDisplay_ReturnsCppProgramWithMissingImageToDisplay() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.print(\"\");"
            + END;
        //

        assertCodeIsOk(expectedResult, "/action/display_image_missing_image_name.xml");
    }

    @Test
    public void visitClearDisplayAction_ScriptWithClearDisplay_ReturnsCppProgramClearDisplay() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "\n"
            + "uBit.display.clear();"
            + END;

        assertCodeIsOk(expectedResult, "/action/display_clear.xml");
    }

    @Ignore
    public void visitImageShiftFunction_ScriptWithShiftTwoImages_ReturnsCppProgramShiftTwoImages() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + END;
        // + "\n"
        // + "display.show(Image.SILLY.shift_up(1))\n"
        // + "display.show(Image.SILLY.shift_down(2))";

        assertCodeIsOk(expectedResult, "/function/image_shift_up_down.xml");
    }

    @Ignore
    public void visitImageShiftFunction_ScriptWithMissingPositionImage_ReturnsCppProgramMissingPositionImage() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + END;
        // + "\n"
        // + "display.show(Image.SILLY.shift_up(0))";

        assertCodeIsOk(expectedResult, "/function/image_shift_missing_image_and_position.xml");
    }

    @Ignore
    public void visitImageInvertFunction_ScriptWithInvertHeartImage_ReturnsCppProgramInvertHeartImage() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + END;
        // + "\n"
        // + "display.show(Image.HEART.invert())";

        assertCodeIsOk(expectedResult, "/function/image_invert_heart_image.xml");
    }

    @Ignore
    public void visitImageInvertFunction_ScriptWithMissingImage_ReturnsCppProgramInvertDefaultImage() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + END;
        // + "\n"
        //+ "display.show(Image.SILLY.invert())";

        assertCodeIsOk(expectedResult, "/function/image_invert_missing_image.xml");
    }

    @Test
    public void visitBrickSensor_ScriptChecksKeyAStatus_ReturnsCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString(uBit.buttonA.isPressed()));"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/check_if_key_A_is_pressed.xml");
    }

    @Test
    public void visitCompassSensor_ScriptDisplayCompassHeading_ReturnsCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.accelerometer.updateSample();\n"
            + "uBit.display.scroll(ManagedString(uBit.compass.heading()));"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/get_compass_orientation_value.xml");
    }

    @Test
    public void visitImage_ScriptCreatingImage_ReturnsCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);"
            + "uBit.display.print(MicroBitImage(\"255,255,0,0,0\\n0,0,0,0,255\\n0,85,0,0,0\\n0,0,0,255,0\\n0,56,0,0,0\\n\"));"
            + END;

        assertCodeIsOk(expectedResult, "/expr/image_create.xml");
    }

    @Test
    public void visitGestureSensor_ScriptGetCurrentGestureAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.accelerometer.updateSample();\n"
            + "\n"
            + "uBit.display.scroll(ManagedString(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FACE_DOWN));"
            + "uBit.display.scroll(ManagedString(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_TILT_LEFT));"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/check_gesture.xml");
    }

    @Test
    public void visitTemperatureSensor_ScriptGetCurrentTemperatureAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString(uBit.thermometer.getTemperature()));"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/get_temperature.xml");
    }

    @Test
    public void visitLedOnAction_TurnOnLedInThreeDifferentColors_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.rgb.setColour(255, 0, 0, 255);\n"
            + "uBit.rgb.setColour(0, 153, 0, 255);"
            + "uBit.rgb.setColour(153, 153, 255, 255);\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/led_on_three_colors.xml");
    }

    @Test
    public void visitLedOnAction_TurnOnLedMissingColor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.rgb.setColour(0, 0, 0, 255);\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/led_on_missing_color.xml");
    }

    @Test
    public void visitLightStatusAction_TurnOffLed_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.rgb.off();\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/led_off.xml");
    }

    @Ignore
    @Test
    public void visitMotorOnAction_TurnOnMotorsA_B_AB_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.soundmotor.motorAOn(30);\n"
            + "uBit.soundmotor.motorBOn(30);\n"
            + "uBit.soundmotor.motorOn(30);\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/motor_on.xml");
    }

    @Test
    public void visitToneAction_PlayTone50Hz500ms_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.soundmotor.soundOn(50);\n"
            + "uBit.sleep(500);\n"
            + "uBit.soundmotor.soundOff();\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/play_tone.xml");
    }

    @Test
    public void visitTAmbientLightSensor_GetAmbientLigthAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString(uBit.display.readLightLevel()));\n"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/get_ambient_light.xml");
    }

    @Test
    public void visitRadioSendAction_SendHelloMessage_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.radio.enable();\n"
            + "uBit.radio.datagram.send(\"Hallo\");\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/radio_send_message.xml");
    }

    @Test
    public void visitRadioSendAction_SendMissingMessage_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.radio.enable();\n"
            + "uBit.radio.datagram.send(\"\");\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/radio_send_missing_message.xml");
    }

    @Test
    public void visitRadioReceiveAction_ReceiveMessage_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.radio.enable();\n"
            + "uBit.display.scroll(ManagedString(ManagedString(uBit.radio.datagram.recv())));\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/radio_receive_message.xml");
    }

    @Test
    public void visitMotorStopAction_StopMotorFloatNonFloat_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.soundmotor.motorAOff();\n"
            + "uBit.soundmotor.motorBOff();\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/motor_stop.xml");
    }

    @Test
    public void visitMathRandomIntFunct_ShowRandInt1to200_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString((uBit.random(200 - 1 + 1) + 1)));\n"
            + END;

        assertCodeIsOk(expectedResult, "/function/random_int_generator.xml");
    }

    @Test
    public void visitMathRandomIntFunct_ShowRandIntMissingParam_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString((uBit.random(0 - 0 + 1) + 0)));\n"
            + END;

        assertCodeIsOk(expectedResult, "/function/random_int_generator_missing_param.xml");
    }

    @Test
    public void visitWaitStmt_TestAllTheSensorsInTheWaitStmt_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.accelerometer.updateSample();"
            + "while(1){"
            + "if(uBit.buttonA.isPressed()==true){"
            + "break;"
            + "}"
            + "uBit.sleep(100);"
            + "}"
            + "while(1){"
            + "if(uBit.buttonB.isPressed()==true){"
            + "break;"
            + "}"
            + "uBit.sleep(100);"
            + "}"
            + "while(1){"
            + "if(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_TILT_UP==true){break;}uBit.sleep(100);}"
            + "while(1){if(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_TILT_DOWN==true){break;}uBit.sleep(100);}"
            + "while(1){if(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FACE_UP==true){break;}uBit.sleep(100);}"
            + "while(1){if(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FACE_DOWN==true){break;}uBit.sleep(100);}"
            + "while(1){if(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_SHAKE==true){break;}uBit.sleep(100);}"
            + "while(1){if(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FREEFALL==true){break;}uBit.sleep(100);}"
            + "while(1){if(uBit.compass.heading()>180){break;}uBit.sleep(100);}while(1){if(uBit.systemTime()-initTime>500){break;}uBit.sleep(100);}"
            + "while(1){if(uBit.thermometer.getTemperature()>20){break;}uBit.sleep(100);}while(1){if(uBit.display.readLightLevel()>50){break;}uBit.sleep(100);}"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/get_sample_sensor.xml");
    }

    @Test
    public void visitWaitStmt_TestTwoCases_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "while(1){"
            + "if(uBit.buttonA.isPressed()==true){"
            + "uBit.display.scroll(\"Hallo\");"
            + "break;"
            + "}"
            + "if(uBit.thermometer.getTemperature()>20){"
            + "uBit.display.scroll(\"Hallo\");"
            + "break;"
            + "}"
            + "uBit.sleep(100);"
            + "}"

            + END;

        assertCodeIsOk(expectedResult, "/sensor/wait_stmt_two_cases.xml");
    }

    @Test
    public void visitRgbColor_CreateColorAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString(20, 25, 30, 255));"
            + END;

        assertCodeIsOk(expectedResult, "/expr/create_color.xml");
    }

    @Test
    public void visitPinTouchSensor_DisplayIfPin0Pin2andPin3areTouched_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString(uBit.io.P12.isTouched()));\n"
            + "uBit.display.scroll(ManagedString(uBit.io.P1.isTouched()));"
            + "uBit.display.scroll(ManagedString(uBit.io.P16.isTouched()));"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/pin3_is_touched.xml");
    }

    @Test
    public void visitPinGetValueSensor_DisplayAnalogReadPin0andDigitalReadPin2_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.display.scroll(ManagedString(uBit.io.P12.getAnalogValue()));\n"
            + "uBit.display.scroll(ManagedString(uBit.io.P1.getDigitalValue()));"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/read_value_from_pin.xml");
    }

    @Test
    public void visitPinWriteValueSensor_SetAnalogPin0andDigitalPin2To0_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + MAIN
            + "uBit.io.P12.setAnalogValue(0);\n"
            + "uBit.io.P1.setDigitalValue(0);"
            + END;

        assertCodeIsOk(expectedResult, "/action/write_value_to_pin.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateString(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
