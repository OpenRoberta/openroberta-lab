package de.fhg.iais.roberta.syntax.codegen.mbed;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class SimulationVisitorTest {

    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void visitLightStatusAction_TurnOffLed_ReturnsCorrectJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createStatusLight(CONST.OFF);\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(expectedResult, "/action/led_off.xml");
    }

    @Test
    public void visitClearDisplayAction_ScriptWithClearDisplay_ReturnsJavaScriptProgramClearDisplay() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createClearDisplayAction();\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(expectedResult, "/action/display_clear.xml");
    }

    @Test
    public void visitDisplayText_ShowHelloScript_ReturnsJavaScriptProgramWithShowTextCall() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createConstant(CONST.STRING_CONST, 'Hallo'));\n"
                + "var stmt1 = createDisplayTextAction(CONST.CHARACTER, createConstant(CONST.STRING_CONST, 'H'));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(expectedResult, "/action/display_text_show_hello.xml");
    }

    @Test
    public void visitBrickSensor_ScriptChecksKeyAStatus_ReturnsJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createGetSample(CONST.BUTTONS, CONST.BUTTON_A));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(expectedResult, "/sensor/check_if_key_A_is_pressed.xml");
    }

    @Test
    public void visitCompassSensor_ScriptDisplayCompassHeading_ReturnsJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createGetSample(CONST.COMPASS));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(expectedResult, "/sensor/get_compass_orientation_value.xml");
    }

    @Test
    public void visitDisplayImageAction_ScriptWithDisplayImageAndAnimation_ReturnsCppProgramWithDisplayImageAndAnimation() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayImageAction(CONST.IMAGE, createConstant(CONST.IMAGE, [[0,255,0,255,0],[255,255,255,255,255],[255,255,255,255,255],[0,255,255,255,0],[0,0,255,0,0],]));\n"
                + "var stmt1 = createDisplayImageAction(CONST.ANIMATION, createCreateListWith(CONST.ARRAY_IMAGE, [createConstant(CONST.IMAGE, [[0,0,0,0,0],[0,255,0,255,0],[0,255,255,255,0],[0,0,255,0,0],[0,0,0,0,0],]), createConstant(CONST.IMAGE, [[0,0,0,0,0],[255,255,0,255,255],[0,0,0,0,0],[0,255,255,255,0],[0,0,0,0,0],])]));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(expectedResult, "/action/display_image_show_imag_and_animation.xml");
    }

    @Test
    public void visitDisplayImageAction_ScriptWithMissinImageToDisplay_ReturnsJavaScriptProgramWithMissingImageToDisplay() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayImageAction(CONST.IMAGE, createConstant(CONST.STRING_CONST, ''));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(expectedResult, "/action/display_image_missing_image_name.xml");
    }

    @Test
    public void visitPlayNoteAction_ScriptPlayNote_ReturnsJavaScriptProgramWithPlayNote() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createToneAction(createConstant(CONST.NUM_CONST, 261.626), createConstant(CONST.NUM_CONST, 2000));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(expectedResult, "/action/play_note.xml");
    }

    @Test
    public void visitImage_ScriptCreatingImage_ReturnsJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayImageAction(CONST.IMAGE, createConstant(CONST.IMAGE, [[255,255,0,0,0],[0,0,0,0,255],[0,85,0,0,0],[0,0,0,255,0],[0,56,0,0,0]]));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(expectedResult, "/expr/image_create.xml");
    }

    @Ignore("Test is ignored until next commit")
    @Test
    public void visitGestureSensor_ScriptGetCurrentGestureAndDisplay_ReturnsCorrectJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createGetSample(CONST.GESTURE, CONST.FACE_DOWN));\n"
                + "var stmt1 = createDisplayTextAction(CONST.TEXT, createGetSample(CONST.GESTURE, CONST.LEFT));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(expectedResult, "/sensor/check_gesture.xml");
    }

    @Test
    public void visitTemperatureSensor_ScriptGetCurrentTemperatureAndDisplay_ReturnsCorrectJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createGetSample(CONST.TEMPERATURE));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(expectedResult, "/sensor/get_temperature.xml");
    }

    @Test
    public void visitLedOnAction_TurnOnLedInThreeDifferentColors_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createLedOnAction(createConstant(CONST.LED_COLOR_CONST, [255, 0, 0]));\n"
                + "var stmt1 = createLedOnAction(createConstant(CONST.LED_COLOR_CONST, [0, 153, 0]));\n"
                + "var stmt2 = createLedOnAction(createConstant(CONST.LED_COLOR_CONST, [153, 153, 255]));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2]};";

        assertCodeIsOk(expectedResult, "/action/led_on_three_colors.xml");
    }

    @Test
    public void visitRgbColor_DisplayIfPin0Pin2andPin3areTouched__ReturnsCorrectJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createRgbColor([createConstant(CONST.NUM_CONST, 20), createConstant(CONST.NUM_CONST, 25), createConstant(CONST.NUM_CONST, 30)]));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(expectedResult, "/expr/create_color.xml");
    }

    @Ignore("Test is ignored until next commit")
    @Test
    public void visitPinTouchSensor_CreateColorAndDisplay__ReturnsCorrectJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createPinTouchSensor(0));\n"
                + "var stmt1 = createDisplayTextAction(CONST.TEXT, createPinTouchSensor(2));\n"
                + "var stmt2 = createDisplayTextAction(CONST.TEXT, createPinTouchSensor(3));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2]};";
        assertCodeIsOk(expectedResult, "/sensor/pin3_is_touched.xml");
    }

    @Test
    public void visitPinGetValueSensor_DisplayAnalogReadPin0andDigitalReadPin2_ReturnsCorrectJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createDisplayTextAction(CONST.TEXT, createPinGetValueSensor(CONST.ANALOG, 1));\n"
                + "var stmt1 = createDisplayTextAction(CONST.TEXT, createPinGetValueSensor(CONST.DIGITAL, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(expectedResult, "/sensor/read_value_from_pin.xml");
    }

    @Test
    public void visitPinWriteValueSensor_SetAnalogPin0andDigitalPin2To0_ReturnsCorrectJavaScriptProgram() throws Exception {
        String expectedResult =
            "" //
                + "var stmt0 = createPinWriteValueSensor(CONST.ANALOG, 2, createConstant(CONST.NUM_CONST, 1));\n"
                + "var stmt1 = createPinWriteValueSensor(CONST.DIGITAL, 4, createConstant(CONST.NUM_CONST, 1));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(expectedResult, "/action/write_value_to_pin.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a, this.h.generateJavaScript(fileName));
    }
}
