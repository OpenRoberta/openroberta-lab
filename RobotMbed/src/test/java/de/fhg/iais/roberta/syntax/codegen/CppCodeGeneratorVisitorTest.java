package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.components.CalliopeConfiguration;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.testutil.Helper;

public class CppCodeGeneratorVisitorTest {

    private static final String IMPORTS =
        "#include \"MicroBit.h\""
            + "MicroBituBit;"
            + "int main() {"
            + "uBit.init();"
            + "uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);"
            + "int initTime=uBit.systemTime();";

    private static final String END = "release_fiber();}";

    private static CalliopeConfiguration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        Configuration.Builder configuration = new CalliopeConfiguration.Builder();
        brickConfiguration = (CalliopeConfiguration) configuration.build();
    }

    @Test
    public void visitMainTask_ByDefault_ReturnsEmptyCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + END;

        assertCodeIsOk(expectedResult, "/task/main_task_no_variables_empty.xml");
    }

    @Test
    public void visitDisplayText_ShowHelloScript_ReturnsCppProgramWithShowTextCall() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.display.scroll(\"Hallo\");"
            + END;

        assertCodeIsOk(expectedResult, "/action/display_text_show_hello.xml");
    }

    @Test
    public void visitPredefinedImage_ScriptWithToImageVariables_ReturnsCppProgramWithTwoImageVariables() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "MicroBitImage Element = MicroBitImage(\"0,255,0,255,0\\n255,255,255,255,255\\n255,255,255,255,255\\n0,255,255,255,0\\n0,0,255,0,0\\n\");"
            + "MicroBitImage Element2 = MicroBitImage(\"255,255,255,255,255\\n255,255,0,255,255\\n0,0,0,0,0\\n0,255,0,255,0\\n0,255,255,255,0\\n\");"
            + END;

        assertCodeIsOk(expectedResult, "/expr/image_get_image_defined_as_global_variables.xml");
    }

    @Ignore
    public void visitDisplayImageAction_ScriptWithDisplayImageAndAnimation_ReturnsCppProgramWithDisplayImageAndAnimation() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.display.print(MicroBitImage(\"0,1,0,1,0\\n1,1,1,1,1\\n1,1,1,1,1\\n0,1,1,1,0\\n0,0,1,0,0\\n\"));"
            + END;
        //+ "\n"
        //+ "display.show([Image.HEART_SMALL, Image.ASLEEP])";

        assertCodeIsOk(expectedResult, "/action/display_image_show_imag_and_animation.xml");
    }

    @Test
    public void visitDisplayImageAction_ScriptWithMissinImageToDisplay_ReturnsCppProgramWithMissingImageToDisplay() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.display.print(\"\");"
            + END;
        //

        assertCodeIsOk(expectedResult, "/action/display_image_missing_image_name.xml");
    }

    @Test
    public void visitClearDisplayAction_ScriptWithClearDisplay_ReturnsCppProgramClearDisplay() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "\n"
            + "uBit.display.clear();"
            + END;

        assertCodeIsOk(expectedResult, "/action/display_clear.xml");
    }

    @Ignore
    public void visitImageShiftFunction_ScriptWithShiftTwoImages_ReturnsCppProgramShiftTwoImages() throws Exception {
        String expectedResult = "" //
            + IMPORTS
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
            + END;
        // + "\n"
        //+ "display.show(Image.SILLY.invert())";

        assertCodeIsOk(expectedResult, "/function/image_invert_missing_image.xml");
    }

    @Ignore
    public void visitBrickSensor_ScriptChecksKeyAStatus_ReturnsCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.display.scroll(uBit.buttonA.isPressed());"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/check_if_key_A_is_pressed.xml");
    }

    @Test
    public void visitCompassSensor_ScriptDisplayCompassHeading_ReturnsCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.display.scroll(uBit.compass.getFieldStrength());"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/get_compass_orientation_value.xml");
    }

    @Ignore
    public void visitImage_ScriptCreatingImage_ReturnsCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + END;
        // + "\n"
        // + "display.show(Image(\"99000:00009:03000:00090:02000\"))";

        assertCodeIsOk(expectedResult, "/expr/image_create.xml");
    }

    @Test
    public void visitGestureSensor_ScriptGetCurrentGestureAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "\n"
            + "uBit.display.scroll(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_FACE_DOWN);uBit.display.scroll(uBit.accelerometer.getGesture()==MICROBIT_ACCELEROMETER_EVT_TILT_LEFT);"
            + END;
        // + "display.scroll(str(\"left\" == accelerometer.current_gesture()))";

        assertCodeIsOk(expectedResult, "/sensor/check_gesture.xml");
    }

    @Test
    public void visitTemperatureSensor_ScriptGetCurrentTemperatureAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.display.scroll(uBit.thermometer.getTemperature());"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/get_temperature.xml");
    }

    @Test
    public void visitLedOnAction_TurnOnLedInThreeDifferentColors_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
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
            + "uBit.rgb.setColour(0, 0, 0, 255);\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/led_on_missing_color.xml");
    }

    @Test
    public void visitLightStatusAction_TurnOffLed_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.rgb.off();\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/led_off.xml");
    }

    @Test
    public void visitMotorOnAction_TurnOnMotorsA_B_AB_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.soundmotor.MotorA_On(30);\n"
            + "uBit.soundmotor.MotorB_On(30);\n"
            + "uBit.soundmotor.MotorOn(30);\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/motor_on.xml");
    }

    @Test
    public void visitToneAction_PlayTone50Hz500ms_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.soundmotor.Sound_On(50);\n"
            + "uBit.sleep(500);\n"
            + "uBit.soundmotor.Sound_Off();\n"
            + END;

        assertCodeIsOk(expectedResult, "/action/play_tone.xml");
    }

    @Test
    public void visitTAmbientLightSensor_GetAmbientLigthAndDisplay_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS
            + "uBit.display.scroll(uBit.display.readLightLevel());\n"
            + END;

        assertCodeIsOk(expectedResult, "/sensor/get_ambient_light.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateString(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
