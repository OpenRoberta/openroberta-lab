package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.mbed.CalliopeConfiguration;
import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class CppVisitorTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    private static final String IMPORTS = //
        "#define_GNU_SOURCE\n\n"
            + "#include \"MicroBit.h\"" //
            + "#include \"NEPODefs.h\""
            + "#include <array>\n"
            + "#include <stdlib.h>\n"
            + "MicroBit_uBit;";

    private static final String MAIN = "int main() { _uBit.init();";

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
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + END;

        assertCodeIsOk(expectedResult, "/task/main_task_no_variables_empty.xml");
    }

    @Test
    public void visitMainTask_timerUsed() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "int _initTime = _uBit.systemTime();"
                + "double item;"
                + MAIN
                + "item = 0;\n"
                + "item = ( _uBit.systemTime() - _initTime );"
                + END;

        assertCodeIsOk(expectedResult, "/action/timer_used.xml");
    }

    @Test
    public void visitDisplayText_ShowHelloScript_ReturnsCppProgramWithShowTextCall() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(\"Hallo\"));"
                + "_uBit.display.print(ManagedString(\"H\"));"
                + END;

        assertCodeIsOk(expectedResult, "/action/display_text_show_hello.xml");
    }

    @Test
    public void visitPredefinedImage_ScriptWithToImageVariables_ReturnsCppProgramWithTwoImageVariables() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "MicroBitImage Element;"
                + "MicroBitImage Element2;"
                + MAIN
                + "Element = MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\");"
                + "Element2 = MicroBitImage(\"255,255,255,255,255\\n255,255,0,255,255\\n0,0,0,0,0\\n0,255,0,255,0\\n0,255,255,255,0\\n\");"
                + END;

        assertCodeIsOk(expectedResult, "/expr/image_get_image_defined_as_global_variables.xml");
    }

    @Test
    public void visitDisplayImageAction_ScriptWithDisplayImageAndAnimation_ReturnsCppProgramWithDisplayImageAndAnimation() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.print(MicroBitImage(\"0,255,0,255,0\\n"
                + "255,255,255,255,255\\n"
                + "255,255,255,255,255\\n"
                + "0,255,255,255,0\\n"
                + "0,0,255,0,0\\n\"));"
                + "_uBit.display.animateImages({MicroBitImage(\"0,0,0,0,0\\n"
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
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.print(\"\");"
                + END;
        //

        assertCodeIsOk(expectedResult, "/action/display_image_missing_image_name.xml");
    }

    @Test
    public void visitClearDisplayAction_ScriptWithClearDisplay_ReturnsCppProgramClearDisplay() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "\n"
                + "_uBit.display.clear();"
                + END;

        assertCodeIsOk(expectedResult, "/action/display_clear.xml");
    }

    @Test
    public void visitImageShiftFunction_ScriptWithShiftTwoImages_ReturnsCppProgramShiftTwoImages() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "\n"
                + "_uBit.display.print(MicroBitImage(\"255,0,0,0,255\\n0,0,0,0,0\\n255,255,255,255,255\\n0,0,255,0,255\\n0,0,255,255,255\\n\").shiftImageUp(1));\n"
                + "_uBit.display.print(MicroBitImage(\"255,0,0,0,255\\n0,0,0,0,0\\n255,255,255,255,255\\n0,0,255,0,255\\n0,0,255,255,255\\n\").shiftImageDown(2));"
                + END;

        assertCodeIsOk(expectedResult, "/function/image_shift_up_down.xml");
    }

    @Test
    public void visitImageShiftFunction_ScriptWithMissingPositionImage_ReturnsCppProgramMissingPositionImage() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "\n"
                + "_uBit.display.print(MicroBitImage().shiftImageUp(0));"
                + END;

        assertCodeIsOk(expectedResult, "/function/image_shift_missing_image_and_position.xml");
    }

    @Test
    public void visitImageInvertFunction_ScriptWithInvertHeartImage_ReturnsCppProgramInvertHeartImage() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "\n"
                + "_uBit.display.print(MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\").invert());"
                + END;

        assertCodeIsOk(expectedResult, "/function/image_invert_heart_image.xml");
    }

    @Test
    public void visitImageInvertFunction_ScriptWithMissingImage_ReturnsCppProgramInvertDefaultImage() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "\n"
                + "_uBit.display.print(MicroBitImage().invert());"
                + END;

        assertCodeIsOk(expectedResult, "/function/image_invert_missing_image.xml");
    }

    @Test
    public void visitBrickSensor_ScriptChecksKeyAStatus_ReturnsCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(_uBit.buttonA.isPressed()));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/check_if_key_A_is_pressed.xml");
    }

    @Test
    public void visitCompassSensor_ScriptDisplayCompassHeading_ReturnsCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.accelerometer.updateSample();\n"
                + "_uBit.display.scroll(ManagedString(_uBit.compass.heading()));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/get_compass_orientation_value.xml");
    }

    @Test
    public void visitImage_ScriptCreatingImage_ReturnsCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);"
                + "_uBit.display.print(MicroBitImage(\"255,255,0,0,0\\n0,0,0,0,255\\n0,85,0,0,0\\n0,0,0,255,0\\n0,56,0,0,0\\n\"));"
                + END;

        assertCodeIsOk(expectedResult, "/expr/image_create.xml");
    }

    @Ignore("Test is ignored until next commit")
    @Test
    public void visitGestureSensor_ScriptGetCurrentGestureAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.accelerometer.updateSample();\n"
                + "\n"
                + "_uBit.display.scroll(ManagedString((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FACE_DOWN)));"
                + "_uBit.display.scroll(ManagedString((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_TILT_LEFT)));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/check_gesture.xml");
    }

    @Test
    public void visitTemperatureSensor_ScriptGetCurrentTemperatureAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(_uBit.thermometer.getTemperature()));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/get_temperature.xml");
    }

    @Test
    public void visitLedOnAction_TurnOnLedInThreeDifferentColors_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.rgb.setColour(MicroBitColor(255, 0, 0, 255));\n"
                + "_uBit.rgb.setColour(MicroBitColor(0, 153, 0, 255));\n"
                + "_uBit.rgb.setColour(MicroBitColor(153, 153, 255, 255));\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/led_on_three_colors.xml");
    }

    @Test
    public void visitLedOnAction_TurnOnLedMissingColor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.rgb.setColour(MicroBitColor());\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/led_on_missing_color.xml");
    }

    @Test
    public void visitLightStatusAction_TurnOffLed_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.rgb.off();\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/led_off.xml");
    }

    @Test
    public void visitMotorOnAction_TurnOnMotorsA_B_AB_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.motorAOn(30);\n"
                + "_uBit.soundmotor.motorBOn(30);\n"
                + "_uBit.soundmotor.motorAOn(30);\n"
                + "_uBit.soundmotor.motorBOn(30);\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/motor_on.xml");
    }

    @Test
    public void visitSingleMotorOnAction_TurnOnMotor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.motorOn(0);\n"
                + "_uBit.soundmotor.motorOn(14);\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/single_motor_on.xml");
    }

    @Test
    public void visitToneAction_PlayTone50Hz500ms_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.soundOn(50);\n"
                + "_uBit.sleep(500);\n"
                + "_uBit.soundmotor.soundOff();\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/play_tone.xml");
    }

    @Test
    public void visitPlayNoteAction_PlayNote261dot626Hz2000ms_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.soundOn(261.626);\n"
                + "_uBit.sleep(2000);\n"
                + "_uBit.soundmotor.soundOff();\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/play_note.xml");
    }

    @Test
    public void visitTAmbientLightSensor_GetAmbientLigthAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)));\n"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/get_ambient_light.xml");
    }

    @Test
    public void visitRadioSendAction_SendHelloMessage_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.radio.enable();\n"
                + "_uBit.radio.setTransmitPower(0); _uBit.radio.datagram.send(ManagedString((ManagedString(\"Hallo\"))));\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/radio_send_message.xml");
    }

    @Test
    public void visitRadioSendAction_SendMissingMessage_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.radio.enable();\n"
                + "_uBit.radio.setTransmitPower(0); _uBit.radio.datagram.send(ManagedString((\"\")));\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/radio_send_missing_message.xml");
    }

    @Test
    public void visitRadioReceiveAction_ReceiveMessage_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.radio.enable();\n"
                + "_uBit.display.scroll(ManagedString(ManagedString(_uBit.radio.datagram.recv())));\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/radio_receive_message.xml");
    }

    @Test
    public void visitRadioRssiSensor_DisplayRssiValue_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.radio.enable();\n"
                + "_uBit.display.scroll(ManagedString(_uBit.radio.getRSSI()));\n"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/radio_rssi.xml");
    }

    @Test
    public void visitMotorStopAction_StopMotorFloatNonFloat_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.motorAOff();\n"
                + "_uBit.soundmotor.motorBOff();\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/motor_stop.xml");
    }

    @Test
    public void visitSingleMotorStopAction_StopMotorFloatNonFloatSleep_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.motorCoast();\n"
                + "_uBit.soundmotor.motorBreak();\n"
                + "_uBit.soundmotor.motorSleep();\n"
                + END;

        assertCodeIsOk(expectedResult, "/action/single_motor_stop.xml");
    }

    @Test
    public void visitMathRandomIntFunct_ShowRandInt1to200_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString((_uBit.random(200 - 1 + 1) + 1)));\n"
                + END;

        assertCodeIsOk(expectedResult, "/function/random_int_generator.xml");
    }

    @Test
    public void visitMathPropertyFunct_ShowIsWholeNumber_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(isWhole(2)));\n"
                + END;

        assertCodeIsOk(expectedResult, "/function/is_whole_number.xml");
    }

    @Test
    public void visitMathPropertyFunct_ShowIsPrimeNumber_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(isPrime(2)));\n"
                + END;

        assertCodeIsOk(expectedResult, "/function/is_prime_number.xml");
    }

    @Test
    public void visitMathRandomIntFunct_ShowRandIntMissingParam_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString((_uBit.random(0 - 0 + 1) + 0)));\n"
                + END;

        assertCodeIsOk(expectedResult, "/function/random_int_generator_missing_param.xml");
    }

    @Ignore("Test is ignored until we have a solution for the wait statement")
    @Test
    public void visitWaitStmt_TestAllTheSensorsInTheWaitStmt_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.accelerometer.updateSample();"
                + "while(1){"
                + "if(_uBit.buttonA.isPressed()==true){"
                + "break;"
                + "}"
                + "_uBit.sleep(1);"
                + "}"
                + "while(1){"
                + "if(_uBit.buttonB.isPressed()==true){"
                + "break;"
                + "}"
                + "_uBit.sleep(1);"
                + "}"
                + "while(1){"
                + "if((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_TILT_UP)==true){break;}_uBit.sleep(1);}"
                + "while(1){if((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_TILT_DOWN)==true){break;}_uBit.sleep(1);}"
                + "while(1){if((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FACE_UP)==true){break;}_uBit.sleep(1);}"
                + "while(1){if((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FACE_DOWN)==true){break;}_uBit.sleep(1);}"
                + "while(1){if((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_SHAKE)==true){break;}_uBit.sleep(1);}"
                + "while(1){if((_uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FREEFALL)==true){break;}_uBit.sleep(1);}"
                + "while(1){if(_uBit.compass.heading()>180){break;}_uBit.sleep(1);}while(1){if((_uBit.systemTime()-_initTime)>500){break;}_uBit.sleep(1);}"
                + "while(1){if(_uBit.thermometer.getTemperature()>20){break;}_uBit.sleep(1);}while(1){if(_uBit.display.readLightLevel()>50){break;}_uBit.sleep(1);}"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/get_sample_sensor.xml");
    }

    @Test
    public void visitWaitStmt_TestTwoCases_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.accelerometer.updateSample();"
                + "while(true){"
                + "if(_uBit.buttonA.isPressed()==true){"
                + "break;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "_uBit.display.scroll(ManagedString(\"Hallo\"));"
                + "while(true){"
                + "if(_uBit.accelerometer.getPitch()>90){"
                + "break;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "_uBit.display.scroll(ManagedString(\"Hallo\"));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/wait_stmt_two_cases.xml");
    }

    @Ignore // display color is not allowed anymore
    @Test
    public void visitRgbColor_CreateColorAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(20, 25, 30, 30));"
                + END;

        assertCodeIsOk(expectedResult, "/expr/create_color.xml");
    }

    @Ignore("Test is ignored until we have a solution for the pin mapping")
    @Test
    public void visitPinTouchSensor_DisplayIfPin0Pin2andPin3areTouched_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(_uBit.io.P12.isTouched()));\n"
                + "_uBit.display.scroll(ManagedString(_uBit.io.P1.isTouched()));"
                + "_uBit.display.scroll(ManagedString(_uBit.io.P16.isTouched()));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/pin3_is_touched.xml");
    }

    @Test
    public void visitAccelerationSensor_DisplayTheAccelerationInEachDirection_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.accelerometer.updateSample();\n"
                + "_uBit.display.scroll(ManagedString(_uBit.accelerometer.getX()));\n"
                + "_uBit.display.scroll(ManagedString(_uBit.accelerometer.getY()));\n"
                + "_uBit.display.scroll(ManagedString(_uBit.accelerometer.getZ()));\n"
                + "_uBit.display.scroll(ManagedString(_uBit.accelerometer.getStrength()));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/acceleration_sensor.xml");
    }

    @Test
    public void visitAccelerationOrientationSensor_DisplayTheTileAndRotation_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.accelerometer.updateSample();\n"
                + "_uBit.display.scroll(ManagedString(_uBit.accelerometer.getPitch()));\n"
                + "_uBit.display.scroll(ManagedString(_uBit.accelerometer.getRoll()));"

                + END;

        assertCodeIsOk(expectedResult, "/sensor/gyro_sensor.xml");
    }

    @Test
    public void visitPinGetValueSensor_DisplayAnalogReadPin0andDigitalReadPin2_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(_uBit.io.P0.getAnalogValue()));\n"
                + "_uBit.display.scroll(ManagedString(_uBit.io.P12.getDigitalValue()));"
                + END;

        assertCodeIsOk(expectedResult, "/sensor/read_value_from_pin.xml");
    }

    @Test
    public void visitPinWriteValueSensor_SetAnalogPin0andDigitalPin2To0_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.io.P1.setAnalogValue(1);\n"
                + "_uBit.io.P19.setDigitalValue(1);"
                + END;

        assertCodeIsOk(expectedResult, "/action/write_value_to_pin.xml");
    }

    @Test
    public void visitPinSetPull_SetPullToValues_ReturnsCorrectCPP_Program() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.io.P12.setPull(PullUp);\n"
                + "_uBit.io.P0.setPull(PullDown);\n"
                + "_uBit.io.P1.setPull(PullNone);"
                + END;

        assertCodeIsOk(expectedResult, "/action/pin_set_pull.xml");
    }

    @Test
    public void visitMathOnListFunct_DisplayAllMathOnListFunctsResults_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "array<double, 4> item;"
                + MAIN
                + "item = {1, 2, 3, 4};"
                + "_uBit.display.scroll(ManagedString(sum(item)));\n"
                + "_uBit.display.scroll(ManagedString(min(item)));\n"
                + "_uBit.display.scroll(ManagedString(max(item)));\n"
                + "_uBit.display.scroll(ManagedString(average(item)));\n"
                + "_uBit.display.scroll(ManagedString(median(item)));\n"
                + "_uBit.display.scroll(ManagedString(standardDeviation(item)));\n"
                + "_uBit.display.scroll(ManagedString(randomElement(item)));"
                + END;
        assertCodeIsOk(expectedResult, "/action/math_on_list.xml");
    }

    @Test
    public void visitUserDefinedMethod__ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "template<size_t N0>"
                + "void doSomething(array<MicroBitImage,N0> x);"
                + "array<MicroBitImage,3> item;\n"
                + MAIN
                + "item={MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\"),MicroBitImage(\"0,0,0,0,0\\n0,255,0,255,0\\n0,255,255,255,0\\n0,0,255,0,0\\n0,0,0,0,0\\n\"),MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\")};"
                + "doSomething(item);"
                + END
                + "template<size_t N0>"
                + "void doSomething(array<MicroBitImage, N0> x) {"
                + "_uBit.display.animateImages(x,200);"
                + "}";

        assertCodeIsOk(expectedResult, "/function/user_defined_function.xml");
    }

    @Ignore("Test is ignored currently")
    @Test
    public void check_noLoops_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "if (20 == 30) {"
                + "   while (1) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(1);"
                + "   }"
                + "}"
                + END;

        assertCodeIsOk(a, "/stmts/no_loops.xml");
    }

    @Ignore("Test is ignored until next commit")
    @Test
    public void check_nestedLoopsNoBreakorContinue_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "while (true) {"
                + "if (30 == 20) {"
                + "   while (1) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(1);"
                + "   }"
                + "}"
                + "for (int i = 1; i < 10; i += 1) {"
                + "_uBit.sleep(1);"
                + "}"
                + "_uBit.sleep(1);"
                + "}"
                + END;
        assertCodeIsOk(a, "/stmts/nested_loops.xml");
    }

    @Test
    public void check_loopsWithBreakAndContinue_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "array<double, 3> item2;"
                + MAIN
                + "item2 = {0, 0, 0};"
                + "while (true) {"
                + "if (30 == 20) {"
                + "     break;"
                + "} else if (30 == 12) {"
                + "     continue;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "for (int i = 1; i < 10; i += 1) {"
                + "if (30 == 20) {"
                + "     continue;"
                + "} else if (30 == 12) {"
                + "     break;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "for (double item : item2) {"
                + "if (30 == 20) {"
                + "     continue;"
                + "} else if (30 == 20) {"
                + "     break;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "while (true) {"
                + "if (30 == 20) {"
                + "     continue;"
                + "} else if (30 == 20) {"
                + "     break;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "for (int k0 = 0; k0 < 10; k0 += 1) {"
                + "if (30 == 20) {"
                + "     break;"
                + "} else if (30 == 20) {"
                + "     continue;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + END;
        assertCodeIsOk(a, "/stmts/loops_with_break_and_continue.xml");
    }

    @Ignore("Test is ignored currently")
    @Test
    public void check_loopWithBreakAndContinueInWait_returnsOneLabeledLoop() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "while (true) {"

                + "   while (1) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(1);"
                + "   }"
                + "continue_loop1:"
                + "_uBit.sleep(1);"
                + "}"
                + "break_loop1:"
                + END;
        assertCodeIsOk(a, "/stmts/loop_with_break_and_continue_inside_wait.xml");
    }

    @Test
    public void check_loopsWithBreakAndContinueFitstInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "while (true) {"
                + "   while (true) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "   }"
                + "continue_loop1:"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "break_loop1:"
                + "for (int i = 1; i < 10; i += 1) {"
                + "     if (i < 10) {"
                + "         continue;"
                + "     }"
                + "     _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + END;

        assertCodeIsOk(a, "/stmts/two_loop_with_break_and_continue_one_inside_wait_another_not.xml");
    }

    @Test
    public void check_twoNestedloopsFirstWithBreakAndContinueInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "while (true) {"
                + "   while (true) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "   }"
                + "for (int i = 1; i < 10; i += 1) {"
                + "     if (i < 10) {"
                + "        continue;"
                + "     }"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "continue_loop1:"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "break_loop1:"
                + END;
        assertCodeIsOk(a, "/stmts/two_nested_loops_first_with_break_in_wait_second_not.xml");
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWait_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "while (true) {"
                + "   while (true) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int i = 1; i < 10; i += 1) {"
                + "             if (i < 10) {"
                + "                 continue;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int j = 1; j < 10; j += 1) {"
                + "             if (j < 10) {"
                + "                 continue;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "   }"
                + "continue_loop1:"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "break_loop1:"
                + END;

        assertCodeIsOk(a, "/stmts/loop_with_nested_two_loops_inside_wait.xml");
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstAndThirdLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "while (true) {"
                + "   while (true) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int j = 1; j < 10; j += 1) {"
                + "             if (j < 10) {"
                + "                 continue;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int i = 1; i < 10; i += 1) {"
                + "         while (true) {"
                + "             if (_uBit.buttonA.isPressed() == true) {"
                + "                 goto continue_loop3;"
                + "                 break;"
                + "             }"
                + "             if (_uBit.buttonA.isPressed() == true) {"
                + "                 goto break_loop3;"
                + "                 break;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         continue_loop3:"
                + "         _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         break_loop3:"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "   }"
                + "continue_loop1:"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "break_loop1:"
                + END;
        assertCodeIsOk(a, "/stmts/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml");
    }

    @Test
    public void check_threeLoopsWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstThirdAndFourthLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + MAIN
                + "while (true) {"
                + "   while (true) {"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int j = 1; j < 10; j += 1) {"
                + "             if (j < 10) {"
                + "                 continue;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int i = 1; i < 10; i += 1) {"
                + "         while (true) {"
                + "             if (_uBit.buttonA.isPressed() == true) {"
                + "                 goto continue_loop3;"
                + "                 break;"
                + "             }"
                + "             if (_uBit.buttonA.isPressed() == true) {"
                + "                 goto break_loop3;"
                + "                 break;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         continue_loop3:"
                + "         _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         break_loop3:"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "   }"
                + "continue_loop1:"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "break_loop1:"
                + "while (true) {"
                + "     if (10 < 10) {"
                + "         continue;"
                + "     }"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "while (true) {"
                + "         while (true) {"
                + "             if (_uBit.buttonA.isPressed() == true) {"
                + "                 goto continue_loop5;"
                + "                 break;"
                + "             }"
                + "             if (_uBit.buttonA.isPressed() == true) {"
                + "                 goto break_loop5;"
                + "                 break;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "continue_loop5:"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "break_loop5:"
                + END;
        assertCodeIsOk(a, "/stmts/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a.replaceAll("\\s+", ""), this.h.generateCpp(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
