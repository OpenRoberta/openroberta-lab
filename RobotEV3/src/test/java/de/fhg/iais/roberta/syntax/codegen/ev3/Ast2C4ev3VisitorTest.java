package de.fhg.iais.roberta.syntax.codegen.ev3;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Ast2C4ev3VisitorTest {


    private static final String CONSTANTS_AND_IMPORTS =
        "" //
            + "#definePROGRAM_NAME\"Test\"\n"
            + "#define WHEEL_DIAMETER 5.6\n"
            + "#define TRACK_WIDTH 18.0\n"
            + "#include <ev3.h>\n"
            + "#include <math.h>\n"
            + "#include <list>\n"
            + "#include \"NEPODefs.h\"\n";

    private static final String CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH =
        "" //
            + "#definePROGRAM_NAME\"Test\"\n"
            + "#define WHEEL_DIAMETER 5.6\n"
            + "#define TRACK_WIDTH 17.0\n"
            + "#include <ev3.h>\n"
            + "#include <math.h>\n"
            + "#include <list>\n"
            + "#include \"NEPODefs.h\"\n";

    private static final String MAIN_INIT_EV3 =
        "" //
            + "int main() {\n"
            + "    NEPOInitEV3();\n";

    private static final String BEGIN_MAIN__TOUCH_NULL_INFRARED_ULTRASONIC =
        "" //
        + MAIN_INIT_EV3
        + "    NEPOSetAllSensors(EV3Touch, NULL, EV3Ir, EV3Ultrasonic);\n";

    private static final String BEGIN_MAIN__TOUCH_NULL_NULL_ULTRASONIC =
        "" //
        + MAIN_INIT_EV3
        + "    NEPOSetAllSensors(EV3Touch, NULL, NULL, EV3Ultrasonic);\n\n";

    private static final String BEGIN_MAIN__TOUCH_NULL_COLOR_NULL =
        "" //
        + MAIN_INIT_EV3
        + "    NEPOSetAllSensors(EV3Touch, NULL, EV3Color, NULL);\n\n";

    private static final String BEGIN_MAIN__NULL_NULL_COLOR_NULL =
        "" //
        + MAIN_INIT_EV3
        + "    NEPOSetAllSensors(NULL, NULL, EV3Color, NULL);\n\n";

    private static final String BEGIN_MAIN__NULL_NULL_HTCOLORV2_NULL =
        "" //
            + MAIN_INIT_EV3
            + "    NEPOSetAllSensors(NULL, NULL, HTColorV2, NULL);\n\n";

    private static final String BEGIN_MAIN__NULLSORS =
        "" //
            + MAIN_INIT_EV3
            + "    NEPOSetAllSensors(NULL, NULL, NULL, NULL);\n\n";


    private static final String END_MAIN =
        "" //
            + "    \n"
            + "    NEPOFreeEV3();\n"
            + "    return 0;\n"
            + "}\n";

    private static Configuration standardBrickConfiguration = HelperEv3ForXmlTest.makeStandardEv3DevConfiguration();

    private final HelperEv3ForXmlTest helper = new HelperEv3ForXmlTest();


    @Test
    public void testLcdText() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN__NULLSORS
                + "DrawString(ToString(\"Hallo\"), 0, 3);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator.xml", expectedCode);
    }

    @Test
    public void testLcdTextInForLoop() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN__NULLSORS
                + "for (float ___k0 = 0; ___k0 < 10; ___k0 += 1) {\n"
                + "    DrawString(ToString(\"Hallo\"), 0, 3);\n"
                + "}\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator1.xml", expectedCode);
    }

    @Test
    public void testIfPressedLedElseLcdPictureThenPlaySoundAndMove() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH
                + BEGIN_MAIN__TOUCH_NULL_COLOR_NULL
                + "if ( ReadEV3TouchSensor(IN_1) ) {\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "} else if ( Red == ReadEV3ColorSensor(IN_3) ) {\n"
                + "    while ( true ) {\n"
                + "        LcdPicture(LCD_COLOR_BLACK, 0, 0, EYESOPEN);\n"
                + "        OnFwdReg(OUT_B, Speed(30));\n"
                + "    }\n"
                + "}\n"
                + "PlaySystemSound(SOUND_DOUBLE_BEEP);\n"
                + "SetVolume(50);\n"
                + "for ( float ___i = 1; ___i < 10; ___i += 1 ) {\n"
                + "    RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + "}\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator2.xml", expectedCode, HelperEv3ForXmlTest.makeTouchUltrasonicColorConfiguration());
    }


    @Test
    public void testIfPressedLedElseIfPressedLedElseIfNearLcdPictureElseMove () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH
                + BEGIN_MAIN__TOUCH_NULL_NULL_ULTRASONIC
                + "if ( ReadEV3TouchSensor(IN_1) ) {\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "} else {\n"
                + "    if ( ReadEV3TouchSensor(IN_1) ) {\n"
                + "        SetLedPattern(LED_GREEN);\n"
                + "    } else if ( 0 == ReadEV3UltrasonicSensorDistance(IN_4, CM) ) {\n"
                + "        LcdPicture(LCD_COLOR_BLACK, 0, 0, FLOWERS);\n"
                + "    } else {\n"
                + "        while ( !ButtonIsDown(BTNUP) ) {\n"
                + "            OnFwdReg(OUT_B, Speed(30));\n"
                + "        }\n"
                + "    }\n"
                + "}\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator3.xml", expectedCode, HelperEv3ForXmlTest.makeTouchUltrasonicColorUltrasonicConfiguration());
    }

    @Test
    public void testIfNearRotateThenIfTachoThenLedElsePictureThenLed () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH
                + BEGIN_MAIN__TOUCH_NULL_INFRARED_ULTRASONIC
                + "if ( 5 < MotorPower(OUT_B) ) {\n"
                + "    OnFwdReg(OUT_B, Speed(30));\n"
                + "    RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + "    OnFwdSyncEx(OUT_AB, Speed(50), -200, RESET_NONE);"
                + "}\n"
                + "if ( ((MotorRotationCount(OUT_A) / 360.0) + ReadEV3IrSensorProximity(IN_3)) == ReadEV3UltrasonicSensorDistance(IN_4, CM) ) {\n"
                + "    SetLedPattern(LED_BLACK);\n"
                + "} else {\n"
                + "    NEPOResetEV3GyroSensor(IN_2);\n"
                + "    while ( ReadEV3TouchSensor(IN_1) ) {\n"
                + "        LcdPicture(LCD_COLOR_BLACK, 0, 0, OLDGLASSES);\n"
                + "        LcdClean();\n"
                + "    }\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "}\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator4.xml", expectedCode, HelperEv3ForXmlTest.makeTouchGyroInfraredUltrasonic());
    }


    @Test
    public void testMotorsAndSound () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN__NULLSORS
                + "OnFwdReg(OUT_B, Speed(0));\n"
                + "RotateMotorForAngle(OUT_B, Speed(30), 360 * 0);\n"
                + "OnFwdSyncEx(OUT_BC, Speed(0), -200, RESET_NONE);\n"
                + "SetVolume(50);\n"
                + "NEPOPlayTone(0, 0);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator5.xml", expectedCode);
    }

    @Test
    public void testLcdTextAndSound() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN__NULLSORS
                + "DrawString(ToString(\"Hallo\"), 0, 0);\n"
                + "NEPOPlayTone(300, 3000);"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator6.xml", expectedCode);
    }


    @Test
    public void testMotorAction() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN__NULLSORS
                + "OnFwdReg(OUT_B, Speed(30));\n"
                + "RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + END_MAIN;

        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator7.xml", expectedCode);
    }


    @Test
    public void testVariablesAndLcdText() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "double ___item = 10;\n"
                + "std::string ___item2 = \"TTTT\";\n"
                + "bool ___item3 = true;\n"
                + BEGIN_MAIN__NULLSORS
                + "DrawString(ToString(___item), 0, 0);\n"
                + "DrawString(ToString(___item2), 0, 0);\n"
                + "DrawString(ToString(___item3), 0, 0);\n"
                + "___item3 = false;\n"
                + END_MAIN;

        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator8.xml", expectedCode);
    }

    // TODO: Test drawColorName

    @Test
    public void testMoveAndLcdPicture() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "double ___variablenName = 0;\n"
                + BEGIN_MAIN__NULLSORS
                + "OnFwdSync(OUT_BC, Speed(50));\n"
                + "LcdPicture(LCD_COLOR_BLACK,0,0,OLDGLASSES);\n"
                + END_MAIN;

        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator9.xml", expectedCode);
    }


    @Test
    public void testVariables() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "double ___item = 0;\n"
                + "std::string ___item2 = \"ss\";\n"
                + "bool ___item3 = true;\n"
                + "std::list<double> ___item4 = ((std::list<double>){1, 2, 3});\n"
                + "std::list<std::string> ___item5 = ((std::list<std::string>){\"a\", \"b\"});\n"
                + "std::list<bool> ___item6 = ((std::list<bool>){true, false});\n"
                + "std::list<Color> ___item7 = ((std::list<Color>){Red, Black, None});\n"
                + "Color ___item8 = None;\n"
                + "BluetoothConnectionHandle ___item9 = NEPOWaitConnection();\n"
                + "std::list<BluetoothConnectionHandle> ___item10 = ((std::list<BluetoothConnectionHandle>){NEPOWaitConnection()});\n"
                + BEGIN_MAIN__NULLSORS
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/task/task_mainTask.xml", expectedCode);
    }

    @Test
    public void testMoveAndDefineAndCallFunction () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "void macheEtwas(double ___x, double ___x2) {\n"
                + "    LcdPicture(LCD_COLOR_BLACK, 0, 0, OLDGLASSES);\n"
                + "}\n"
                + BEGIN_MAIN__NULLSORS
                + "RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + "macheEtwas(10, 10);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/methods/method_void_1.xml", expectedCode);
    }


    @Test
    public void testVoidFunction () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "void test() {\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "}\n"
                + BEGIN_MAIN__NULLSORS
                + "test();\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/methods/method_void_2.xml", expectedCode);
    }


    @Test
    public void testVoidFunctionIfTrueReturn () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "void test(bool ___x) {\n"
                + "    if (___x) return;"
                + "    SetLedPattern(LED_GREEN);\n"
                + "}\n"
                + BEGIN_MAIN__NULLSORS
                + "test(true);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/methods/method_if_return_1.xml", expectedCode);
    }

    @Test
    public void testMultipleVoidFunctions () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "double ___variablenName = 0;\n"
                + "bool ___variablenName2 = true;\n"
                + "void test1(double ___x, double ___x2) {\n"
                + "    DrawString(ToString(\"Hallo\"), ___x, ___x2);\n"
                + "}\n"
                + "void test2 () {\n"
                + "    if (___variablenName2) return;\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "}\n"
                + BEGIN_MAIN__NULLSORS
                + "test1(0, 0);\n"
                + "test2();"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/methods/method_void_3.xml", expectedCode);
    }

    @Test
    public void testFunctionReturnsDouble () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "std::list<std::string> ___variablenName = ((std::list<std::string>){\"a\", \"b\", \"c\"});\n"
                + "double test(double ___x, std::list<std::string> ___x2) {\n"
                + "    DrawString(ToString(___x2), ___x, 0);"
                + "    return ___x;\n"
                + "}\n"
                + BEGIN_MAIN__NULLSORS
                + "DrawString(ToString(test(0, ___variablenName)), 0, 0);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/methods/method_return_1.xml", expectedCode);
    }

    @Test
    public void testFunctionReturnsColor () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "std::list<std::string> ___variablenName = ((std::list<std::string>){\"a\", \"b\", \"c\"});\n"
                + "Color test() {\n"
                + "    DrawString(ToString(___variablenName), 0, 0);"
                + "    return None;\n"
                + "}\n"
                + BEGIN_MAIN__NULLSORS
                + "DrawString(ToString(test()), 0, 0);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/methods/method_return_2.xml", expectedCode);
    }


    @Test
    public void testFunctionReturnsRedOrNullColor () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "std::list<std::string> ___variablenName = ((std::list<std::string>){\"a\", \"b\", \"c\"});\n"
                + "Color test() {\n"
                + "    if (true) return Red;\n"
                + "    DrawString(ToString(___variablenName), 0, 0);"
                + "    return None;\n"
                + "}\n"
                + BEGIN_MAIN__NULLSORS
                + "DrawString(ToString(test()), 0, 0);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/methods/method_if_return_2.xml", expectedCode);
    }


    @Test
    public void testCompareString () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "std::string ___message = \"exit\";\n"
                + BEGIN_MAIN__NULLSORS
                + "if (___message == \"exit\") {\n"
                + "    DrawString(ToString(\"done\"), 0, 0);\n"
                + "}\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/stmt/if_stmt4.xml", expectedCode);
    }

    @Test
    public void testDeclareTwoVariables () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "double ___item;\n"
                + "std::string ___item2 = \"cc\";\n"
                + BEGIN_MAIN__NULLSORS
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator11.xml", expectedCode);
    }

    @Test
    public void testReadColorSensorInDifferentModes () throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + "Color ___color = White;\n"
                + "double ___light = 0;\n"
                + "std::list<double> ___rgb = ((std::list<double>){0, 0, 0});\n"
                + BEGIN_MAIN__NULL_NULL_COLOR_NULL
                + "___color = ReadEV3ColorSensor(IN_3);\n"
                + "___light = ReadEV3ColorSensorLight(IN_3, ReflectedLight);\n"
                + "___light = ReadEV3ColorSensorLight(IN_3, AmbientLight);\n"
                + "___rgb = NEPOReadEV3ColorSensorRGB(IN_3);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/read_color_sensor_in_different_modes.xml", expectedCode);
    }

    @Test
    public void testReadHiTecColorSensorV2InDifferentModes () throws Exception {
        Configuration configuration = HelperEv3ForXmlTest.makeHiTecColorSensorConfiguration();
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH
                + "Color ___color = White;\n"
                + "double ___light = 0;\n"
                + "std::list<double> ___rgb = ((std::list<double>){0, 0, 0});\n"
                + BEGIN_MAIN__NULL_NULL_HTCOLORV2_NULL
                + "___color = NEPOReadHTColorSensorV2(IN_3);\n"
                + "___light = NEPOReadHTColorSensorV2Light(IN_3);\n"
                + "___light = NEPOReadHTColorSensorV2AmbientLight(IN_3);\n"
                + "___rgb = NEPOReadHTColorSensorV2RGB(IN_3);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/read_hitec_color_sensor_v2_in_different_modes.xml", expectedCode, configuration);
    }

    @Test
    public void testRotateRegulatedUnregulatedForwardBackwardMotors () throws Exception {
        Configuration configuration = HelperEv3ForXmlTest.makeRotateRegulatedUnregulatedForwardBackwardMotors();
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH
                + BEGIN_MAIN__NULLSORS
                + "OnFwdReg(OUT_A, Speed(30));\n"
                + "RotateMotorForAngle(OUT_A, Speed(30), 360 * 1);\n"
                + "OnFwdReg(OUT_B, Speed(30));\n"
                + "RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + "OnFwdEx(OUT_C, Speed(30), RESET_NONE);\n"
                + "RotateMotorForAngle(OUT_C, Speed(30), 360 * 1);\n"
                + "OnFwdEx(OUT_D, Speed(30), RESET_NONE);\n"
                + "RotateMotorForAngle(OUT_D, Speed(30), 360 * 1);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/rotate_regulated_unregulated_forward_backward_motors.xml", expectedCode, configuration);
    }

    private void checkCodeGeneratorForInput(String fileName, String expectedSourceCode) throws Exception {
        checkCodeGeneratorForInput(fileName, expectedSourceCode, standardBrickConfiguration);
    }

    private void checkCodeGeneratorForInput(String fileName, String expectedSourceCode, Configuration configuration) throws Exception {
        String generatedSourceCode = helper.generateC4ev3(fileName, configuration);
        assertEquals(removeSpaces(expectedSourceCode), removeSpaces(generatedSourceCode));
    }

    private static String removeSpaces (String string) {
        return string.replaceAll("\\s+", "");
    }

}
