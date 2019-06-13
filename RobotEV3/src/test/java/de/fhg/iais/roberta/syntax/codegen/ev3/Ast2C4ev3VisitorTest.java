package de.fhg.iais.roberta.syntax.codegen.ev3;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Ast2C4ev3VisitorTest {


    private static final String CONSTANTS_AND_IMPORTS =
        "" //
            + "#define WHEEL_DIAMETER 5.6\n"
            + "#define TRACK_WIDTH 18.0\n"
            + "#include <ev3.h>\n"
            + "#include <math.h>\n"
            + "#include <list>\n"
            + "#include \"NEPODefs.h\"\n";

    private static final String CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH =
        "" //
            + "#define WHEEL_DIAMETER 5.6\n"
            + "#define TRACK_WIDTH 17.0\n"
            + "#include <ev3.h>\n"
            + "#include <math.h>\n"
            + "#include <list>\n"
            + "#include \"NEPODefs.h\"\n";

    private static final String MAIN_INIT_EV3 =
        "" //
            + "int main() {\n"
            + "    InitEV3();\n";

    private static final String BEGIN_MAIN_DEFAULT =
        "" //
            + MAIN_INIT_EV3
            + "    setAllSensorMode(DEFAULT_MODE_TOUCH, DEFAULT_MODE_GYRO, DEFAULT_MODE_COLOR, DEFAULT_MODE_ULTRASONIC);\n\n";

    private static final String BEGIN_MAIN__TOUCH_ULTRASONIC_COLOR =
        "" //
            + MAIN_INIT_EV3
            + "    setAllSensorMode(DEFAULT_MODE_TOUCH, DEFAULT_MODE_ULTRASONIC, DEFAULT_MODE_COLOR, NO_SEN);\n\n";

    private static final String BEGIN_MAIN__TOUCH_ULTRASONIC_COLOR_ULTRASONIC =
        "" //
            + MAIN_INIT_EV3
            + "    setAllSensorMode(DEFAULT_MODE_TOUCH, DEFAULT_MODE_ULTRASONIC, DEFAULT_MODE_COLOR, DEFAULT_MODE_ULTRASONIC);\n\n";

    private static final String BEGIN_MAIN__TOUCH_GYRO_INFRARED_ULTRASONIC =
        "" //
            + MAIN_INIT_EV3
            + "    setAllSensorMode(DEFAULT_MODE_TOUCH, DEFAULT_MODE_GYRO, DEFAULT_MODE_INFRARED, DEFAULT_MODE_ULTRASONIC);\n\n";


    private static final String END_MAIN =
        "" //
            + "    \n"
            + "    FreeEV3();\n"
            + "    return 0;\n"
            + "}\n";

    private static Configuration standardBrickConfiguration = HelperEv3ForXmlTest.makeStandardEv3DevConfiguration();

    private final HelperEv3ForXmlTest helper = new HelperEv3ForXmlTest();


    @Test
    public void testLcdText() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN_DEFAULT
                + "LcdTextString(\"Hallo\", 0, 3);\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator.xml", expectedCode);
    }

    @Test
    public void testLcdTextInForLoop() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN_DEFAULT
                + "for (float ___k0 = 0; ___k0 < 10; ___k0 += 1) {\n"
                + "    LcdTextString(\"Hallo\", 0, 3);\n"
                + "}\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator1.xml", expectedCode);
    }

    @Test
    public void testIfPressedLedElseLcdPictureThenPlaySoundAndMove() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS__WITH_SMALLER_TRACK_WIDTH
                + BEGIN_MAIN__TOUCH_ULTRASONIC_COLOR
                + "if ( readSensor(IN_1) ) {\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "} else if ( INPUT_REDCOLOR == ReadSensorInMode(IN_3, COL_COLOR) ) {\n"
                + "    while ( true ) {\n"
                + "        LcdBmpFile(TEXT_COLOR_BLACK, 0, 0, EYESOPEN);\n"
                + "        OnFwdReg(OUT_B, Speed(30));\n"
                + "    }\n"
                + "}\n"
                + "_PlaySound(SOUND_DOUBLE_BEEP);\n"
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
                + BEGIN_MAIN__TOUCH_ULTRASONIC_COLOR_ULTRASONIC
                + "if ( readSensor(IN_1) ) {\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "} else {\n"
                + "    if ( readSensor(IN_1) ) {\n"
                + "        SetLedPattern(LED_GREEN);\n"
                + "    } else if ( 0 == ReadSensorInMode(IN_4, US_DIST_CM) ) {\n"
                + "        LcdBmpFile(TEXT_COLOR_BLACK, 0, 0, FLOWERS);\n"
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
                + BEGIN_MAIN__TOUCH_GYRO_INFRARED_ULTRASONIC
                + "if ( 5 < MotorPower(OUT_B) ) {\n"
                + "    OnFwdReg(OUT_B, Speed(30));\n"
                + "    RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + "    OnFwdSyncEx(OUT_AB, Speed(50), 100, RESET_NONE);"
                + "}\n"
                + "if ( (((MotorRotationCount(OUT_A) / 360.0) + ReadSensorInMode(IN_3, IR_PROX))) == ReadSensorInMode(IN_4, US_DIST_CM) ) {\n"
                + "    SetLedPattern(LED_BLACK);\n"
                + "} else {\n"
                + "    ResetGyroSensor(IN_2);\n"
                + "    while ( readSensor(IN_1) ) {\n"
                + "        LcdBmpFile(TEXT_COLOR_BLACK, 0, 0, OLDGLASSES);\n"
                + "        LcdClean();\n"
                + "    }\n"
                + "    SetLedPattern(LED_GREEN);\n"
                + "}\n"
                + END_MAIN;
        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator4.xml", expectedCode, HelperEv3ForXmlTest.makeTouchGyroInfraredUltrasonic());
    }


    // TODO: test_5
    // TODO: test_6


    @Test
    public void testMotorAction() throws Exception {
        String expectedCode =
            "" //
                + CONSTANTS_AND_IMPORTS
                + BEGIN_MAIN_DEFAULT
                + "    OnFwdReg(OUT_B, Speed(30));\n"
                + "    RotateMotorForAngle(OUT_B, Speed(30), 360 * 1);\n"
                + END_MAIN;

        checkCodeGeneratorForInput("/syntax/code_generator/java/java_code_generator7.xml", expectedCode);
    }


    // TODO: test_8
    // TODO: test_9
    // TODO: test_10
    // TODO: test_11
    // TODO: test_12
    // TODO: test_13
    // TODO: test_14
    // TODO: test_15
    // TODO: test_16
    // TODO: test_17
    // TODO: test_18

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
