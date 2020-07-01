package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CppVisitorTest extends CalliopeAstTest {

    private static final String IMPORTS = //
        "#define_GNU_SOURCE\n\n"
            + "#include \"MicroBit.h\"" //
            + "#include \"NEPODefs.h\""
            + "#include <list>\n"
            + "#include <array>\n"
            + "#include <stdlib.h>\n"
            + "MicroBit_uBit;";

    private static final String MAIN = "int main() { _uBit.init();";

    private static final String END = "release_fiber();}";

    @Test
    public void visitMainTask_ByDefault_ReturnsEmptyCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/task/main_task_no_variables_empty.xml",
                configuration,
                true);
        ;
    }

    @Test
    public void visitMainTask_timerUsed() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "int _initTime = _uBit.systemTime();"
                + "double ___item;"
                + MAIN
                + "___item = 0;\n"
                + "___item = ( _uBit.systemTime() - _initTime );"
                + END;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/timer_used.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/display_text_show_hello.xml",
                configuration,
                true);
        ;
    }

    @Test
    public void visitPredefinedImage_ScriptWithToImageVariables_ReturnsCppProgramWithTwoImageVariables() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "MicroBitImage ___Element;"
                + "MicroBitImage ___Element2;"
                + MAIN
                + "___Element = MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\");"
                + "___Element2 = MicroBitImage(\"255,255,255,255,255\\n255,255,0,255,255\\n0,0,0,0,0\\n0,255,0,255,0\\n0,255,255,255,0\\n\");"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/expr/image_get_image_defined_as_global_variables.xml",
                configuration,
                true);
        ;
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
                + "std::array<MicroBitImage,2>_animation=_convertToArray<MicroBitImage,2>"
                + "({MicroBitImage(\"0,0,0,0,0\\n0,255,0,255,0\\n0,255,255,255,0\\n0,0,255,0,0\\n0,0,0,0,0\\n\"),"
                + "MicroBitImage(\"0,0,0,0,0\\n255,255,0,255,255\\n0,0,0,0,0\\n0,255,255,255,0\\n0,0,0,0,0\\n\")});"
                + "_uBit.display.animateImages(_animation, 200);"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/display_image_show_imag_and_animation.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/display_image_missing_image_name.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/display_clear.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/function/image_shift_up_down.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/image_shift_missing_image_and_position.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/image_invert_heart_image.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/image_invert_missing_image.xml",
                configuration,
                true);
        ;
    }

    @Test
    public void visitBrickSensor_ScriptChecksKeyAStatus_ReturnsCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(_uBit.buttonA.isPressed()));"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/sensor/check_if_key_A_is_pressed.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/sensor/get_compass_orientation_value.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/expr/image_create.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/check_gesture.xml", configuration, true);
        ;
    }

    @Test
    public void visitTemperatureSensor_ScriptGetCurrentTemperatureAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(_uBit.thermometer.getTemperature()));"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/get_temperature.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/led_on_three_colors.xml", configuration, true);
        ;
    }

    @Test
    public void visitLedOnAction_TurnOnLedMissingColor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.rgb.setColour(MicroBitColor());\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/led_on_missing_color.xml", configuration, true);
        ;
    }

    @Test
    public void visitFourDigitDisplayShowAction_TurnOnLedMissingColor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + "#define_GNU_SOURCE\n\n"
                + "#include \"MicroBit.h\""
                + "#include \"NEPODefs.h\""
                + "#include \"FourDigitDisplay.h\n\""
                + "#include <list>\n"
                + "#include <array>\n"
                + "#include <stdlib.h>\n"
                + "MicroBit_uBit;"
                + "FourDigitDisplay _fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);"
                + MAIN
                + "_fdd.show(1234,0,true);\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/fourdigitdisplay_show.xml", configuration, true);
        ;
    }

    @Test
    public void visitFourDigitDisplayClearAction_TurnOnLedMissingColor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + "#define_GNU_SOURCE\n\n"
                + "#include \"MicroBit.h\""
                + "#include \"NEPODefs.h\""
                + "#include \"FourDigitDisplay.h\n\""
                + "#include <list>\n"
                + "#include <array>\n"
                + "#include <stdlib.h>\n"
                + "MicroBit_uBit;"
                + "FourDigitDisplay _fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);"
                + MAIN
                + "_fdd.clear();\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/fourdigitdisplay_clear.xml",
                configuration,
                true);
        ;
    }

    @Test
    public void visitLedBarSetAction_TurnOnLedMissingColor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + "#define_GNU_SOURCE\n\n"
                + "#include \"MicroBit.h\""
                + "#include \"NEPODefs.h\""
                + "#include \"Grove_LED_Bar.h\n\""
                + "#include <list>\n"
                + "#include <array>\n"
                + "#include <stdlib.h>\n"
                + "MicroBit_uBit;"
                + "Grove_LED_Bar _ledBar(MICROBIT_PIN_P8, MICROBIT_PIN_P2);"
                + MAIN
                + "_ledBar.setLed(0,5);\n"
                + END;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/ledbar_set.xml", configuration, true);
        ;
    }

    @Test
    public void visitLightStatusAction_TurnOffLed_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.rgb.off();\n"
                + END;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/led_off.xml", configuration, true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/motor_on.xml", configuration, true);
        ;
    }

    @Test(expected = DbcException.class)
    public void visitSingleMotorOnAction_TurnOnMotor_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.motorOn(0);\n"
                + "_uBit.soundmotor.motorOn(14);\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/single_motor_on.xml", configuration, true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/play_tone.xml", configuration, true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/play_note.xml", configuration, true);
        ;
    }

    @Test
    public void visitTAmbientLightSensor_GetAmbientLigthAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString(round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)));\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/get_ambient_light.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/radio_send_message.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/radio_send_missing_message.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/radio_receive_message.xml", configuration, true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/radio_rssi.xml", configuration, true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/motor_stop.xml", configuration, true);
        ;
    }

    @Test(expected = DbcException.class)
    public void visitSingleMotorStopAction_StopMotorFloatNonFloatSleep_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.soundmotor.motorCoast();\n"
                + "_uBit.soundmotor.motorBreak();\n"
                + "_uBit.soundmotor.motorSleep();\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/single_motor_stop.xml", configuration, true);
        ;
    }

    @Test
    public void visitMathRandomIntFunct_ShowRandInt1to200_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString((_uBit.random(200 - 1 + 1) + 1)));\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/random_int_generator.xml",
                configuration,
                true);
        ;
    }

    @Test
    public void visitMathPropertyFunct_ShowIsWholeNumber_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString((2==floor(2))));\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/function/is_whole_number.xml", configuration, true);
        ;
    }

    @Test
    public void visitMathPropertyFunct_ShowIsPrimeNumber_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "inline bool _isPrime(doubled);"
                + MAIN
                + "_uBit.display.scroll(ManagedString(_isPrime(2)));\n"
                + END
                + "inline bool _isPrime(double d) {\n"
                + "    if (!(d == floor(d))) {\n"
                + "        return false;\n"
                + "    }\n"
                + "    int n = (int)d;\n"
                + "    if (n < 2) {\n"
                + "        return false;\n"
                + "    }\n"
                + "    if (n == 2) {\n"
                + "        return true;\n"
                + "    }\n"
                + "    if (n % 2 == 0) {\n"
                + "        return false;\n"
                + "    }\n"
                + "    for (int i = 3, s = (int)(sqrt(d) + 1); i <= s; i += 2) {\n"
                + "        if (n % i == 0) {\n"
                + "            return false;\n"
                + "        }\n"
                + "    }\n"
                + "    return true;\n"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/function/is_prime_number.xml", configuration, true);
        ;
    }

    @Test
    public void visitMathRandomIntFunct_ShowRandIntMissingParam_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.scroll(ManagedString((_uBit.random(0 - 0 + 1) + 0)));\n"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/random_int_generator_missing_param.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/get_sample_sensor.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/wait_stmt_two_cases.xml", configuration, true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/expr/create_color.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/pin3_is_touched.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/acceleration_sensor.xml", configuration, true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/gyro_sensor.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/read_value_from_pin.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/write_value_to_pin.xml", configuration, true);
        ;
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/pin_set_pull.xml", configuration, true);
        ;
    }

    @Test
    public void visitHumiditySensor_DisplayTemperatureAndHumidity_ReturnsCorrectCPP_Program() throws Exception {
        String expectedResult =
            "" //
                + "#define_GNU_SOURCE\n\n"
                + "#include \"MicroBit.h\""
                + "#include \"NEPODefs.h\""
                + "#include \"Sht31.h\n\""
                + "#include <list>\n"
                + "#include <array>\n"
                + "#include <stdlib.h>\n"
                + "MicroBit_uBit;"
                + "Sht31 _sht31 = Sht31(MICROBIT_PIN_P8, MICROBIT_PIN_P2);"
                + MAIN
                + "_uBit.display.scroll(ManagedString(_sht31.readHumidity()));"
                + "_uBit.display.scroll(ManagedString(_sht31.readTemperature()));"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/humidity_sensor.xml", configuration, true);
        ;
    }

    @Test
    public void visitSwitchLedMatrix_SwitchLedMatrixOffAndOn_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.display.disable();\n"
                + "_uBit.display.enable();\n"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/switch_led_matrix.xml", configuration, true);
        ;
    }

    @Test
    public void visitMotionKitSingleSet_SetMotionKitSingle_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.io.P8.setServoValue(180);\n"
                + "_uBit.io.P2.setServoValue(180);\n"
                + "_uBit.io.P2.setAnalogValue(0);\n"
                + "_uBit.io.P8.setAnalogValue(0);\n"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/motionkit_single_set.xml", configuration, true);
        ;
    }

    @Test
    public void visitMotionKitDualSet_SetMotionKitDual_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.io.P2.setServoValue(180);\n"
                + "_uBit.io.P8.setServoValue(180);\n"
                + "_uBit.io.P2.setAnalogValue(0);\n"
                + "_uBit.io.P8.setServoValue(0);\n"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/motionkit_dual_set.xml", configuration, true);
        ;
    }

    @Test
    public void visitServoSet_SetServoToValues_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + MAIN
                + "_uBit.io.P3.setServoValue(90);\n"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/servo_set.xml", configuration, true);
        ;
    }

    @Test
    public void visitMathOnListFunct_DisplayAllMathOnListFunctsResults_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "std::list<double> ___item;"
                + MAIN
                + "___item = {1, 2, 3, 4};"
                + "_uBit.display.scroll(ManagedString(_getListSum(___item)));\n"
                + "_uBit.display.scroll(ManagedString(_getListMin(___item)));\n"
                + "_uBit.display.scroll(ManagedString(_getListMax(___item)));\n"
                + "_uBit.display.scroll(ManagedString(_getListAverage(___item)));\n"
                + "_uBit.display.scroll(ManagedString(_getListMedian(___item)));\n"
                + "_uBit.display.scroll(ManagedString(_getListStandardDeviation(___item)));\n"
                + "_uBit.display.scroll(ManagedString(_getListElementByIndex(___item,0)));"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/math_on_list.xml", configuration, true);
        ;
    }

    @Test
    public void visitUserDefinedMethod__ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "void doSomething(std::list<MicroBitImage> & ___x);"
                + "std::list<MicroBitImage> ___item;\n"
                + MAIN
                + "___item={MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\"),MicroBitImage(\"0,0,0,0,0\\n0,255,0,255,0\\n0,255,255,255,0\\n0,0,255,0,0\\n0,0,0,0,0\\n\"),MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\")};"
                + "doSomething(___item);"
                + END
                + "void doSomething(std::list<MicroBitImage> & ___x) {"
                + "for(MicroBitImage&image:___x){_uBit.display.print(image,0,0,255,200);_uBit.display.clear();}"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/user_defined_function.xml",
                configuration,
                true);
        ;
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

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/stmts/no_loops.xml", configuration, true);
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
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/stmts/nested_loops.xml", configuration, true);
    }

    @Test
    public void check_loopsWithBreakAndContinue_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "std::list<double> ___item2;"
                + MAIN
                + "___item2 = {0, 0, 0};"
                + "while (true) {"
                + "if (30 == 20) {"
                + "     break;"
                + "} else if (30 == 12) {"
                + "     continue;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "if (30 == 20) {"
                + "     continue;"
                + "} else if (30 == 12) {"
                + "     break;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "for (double ___item : ___item2) {"
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
                + "for (int ___k0 = 0; ___k0 < 10; ___k0 += 1) {"
                + "if (30 == 20) {"
                + "     break;"
                + "} else if (30 == 20) {"
                + "     continue;"
                + "}"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/stmts/loops_with_break_and_continue.xml", configuration, true);
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
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/loop_with_break_and_continue_inside_wait.xml",
                configuration,
                true);
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
                + "for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "     if (___i < 10) {"
                + "         continue;"
                + "     }"
                + "     _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + END;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/two_loop_with_break_and_continue_one_inside_wait_another_not.xml",
                configuration,
                true);
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
                + "for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "     if (___i < 10) {"
                + "        continue;"
                + "     }"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "continue_loop1:"
                + "_uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "}"
                + "break_loop1:"
                + END;
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/two_nested_loops_first_with_break_in_wait_second_not.xml",
                configuration,
                true);
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
                + "         for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "             if (___i < 10) {"
                + "                 continue;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int ___j = 1; ___j < 10; ___j += 1) {"
                + "             if (___j < 10) {"
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

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/loop_with_nested_two_loops_inside_wait.xml",
                configuration,
                true);
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
                + "         for (int ___j = 1; ___j < 10; ___j += 1) {"
                + "             if (___j < 10) {"
                + "                 continue;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int ___i = 1; ___i < 10; ___i += 1) {"
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
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml",
                configuration,
                true);
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
                + "         for (int ___j = 1; ___j < 10; ___j += 1) {"
                + "             if (___j < 10) {"
                + "                 continue;"
                + "             }"
                + "             _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     if (_uBit.buttonA.isPressed() == true) {"
                + "         for (int ___i = 1; ___i < 10; ___i += 1) {"
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
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml",
                configuration,
                true);
    }
}
