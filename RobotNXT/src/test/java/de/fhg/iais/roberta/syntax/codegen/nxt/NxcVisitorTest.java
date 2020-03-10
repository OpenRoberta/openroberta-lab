package de.fhg.iais.roberta.syntax.codegen.nxt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class NxcVisitorTest extends NxtAstTest {

    private static final String SUFFIX = "";

    @Test
    public void test() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"

                + "        TextOut(0,(MAXLINES - 3) * MAXLINES,\"Hallo\");\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void test1() throws Exception {

        final String a =
            ""
                + DEFINES_INCLUDES
                + "task main() {"

                + "        for ( int ___k0 = 0; ___k0 < 10; ___k0+=1 ) {\n"
                + "           TextOut(0,(MAXLINES - 3) * MAXLINES,\"Hallo\");"
                + "        }\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator1.xml",
                brickConfiguration,
                true);
        ;
    }

    //ignore
    public void test2() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; task main() {"

                + "        if (SENSOR_1) {\n"
                + "          SENSOR_TYPE_LIGHT_ACTIVE;"
                + "          SetSensorLight(S3,STYPE_COLORGREEN);\n"
                + "        } else if ( Pickcolor.RED == Sensor(S)STYPE_COLORCOLOUR);) {\n"
                + "        \n"
                + "            while ( true ) {\n"
                + "               GraphicOut( 0, 0,\"EYESOPEN\");\n\n"
                + "                  RotateMotor(OUT_B,30);"
                + "            \n"
                + "        }\n"
                + "        }\n"
                + "        playFile(1);\n"
                + "        setVolume(50);\n"
                + "        for ( float ___i = 1; ___i < 10; ___i += 1 ) {\n\n"
                + "        RotateMotor(OUT_B,30,360.0*1);"
                + "        }\n"
                + SUFFIX
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator2.xml",
                brickConfiguration,
                true);
        ;
    }

    //ignore
    public void test3() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; float__speed; task main() {"
                + "    SetSensor(S3,STYPE_COLORGREEN);\n"

                + "        if (SENSOR_1) {\n"
                + "           SENSOR_TYPE_LIGHT_ACTIVE;SetSensorLight(S3,STYPE_COLORGREEN);\n"
                + "        } else {\n"
                + "            if (,\"1pressed\") {\n"
                + "                SENSOR_TYPE_LIGHT_ACTIVE;SetSensorLight(S3,STYPE_COLORGREEN);\n"
                + "            } else if (0==SetSensorLowspeed(S4);) {\n"
                + "               GraphicOut( 15, 15,\"FLOWERS\");\n"
                + "            } else {\n"
                + "            \n"
                + "                while ( !hal.isPressed(BrickKey.UP) ) {\n\n"
                + "                    RotateMotor(OUT_B,30);"
                + "               \n"
                + "            }\n"
                + "            }\n"
                + "        }\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator3.xml",
                brickConfiguration,
                true);
        ;
    }

    // ignore
    public void test4() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; float__speed; task main() {"

                + "        if ( 5 < MotorPower(OUT_B); ) {\n\n\n"
                + "             RotateMotor(OUT_B,30);\n"
                + "          RotateMotor(OUT_B,30,360.0*1);\n"
                + "            turn_right(50);\n"
                + "        }\n"
                + "        if ((MotorTachoCount(OUT_A); + SetSensorInfrared(SSensorPort.S4,DISTANCE); )== SetSensorLowspeed(S4);) {\n"
                + "            SENSOR_TYPE_LIGHT_INACTIVE;\n"
                + "        } else {\n"
                + "           SetSensorGyro(SSensorPort.S2,RESET);\n"
                + "       \n"
                + "            while ( ,1pressed) {\n"
                + "                GraphicOut( 0, 0,\"OLDGLASSES\");\n"
                + "                ClearScreen();\n"
                + "           \n"
                + "         }\n"
                + "           SENSOR_TYPE_LIGHT_ACTIVE;SetSensorLight(S3,STYPE_COLORGREEN);\n"
                + "        }\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator4.xml",
                brickConfiguration,
                true);
        ;
    }

    public void test5() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; float__speed; task main() {"

                + "          OnFwdReg(OUT_B,0,100);"
                + "        RotateMotorEx(OUT_B,-30,360.0*0,-100,true,true);"
                + "       OnFwdSync(OUT_AB,0,100);"
                + "        SetVolume(50);"
                + "        PlayTone(0,0);"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator5.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void test6() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "byte volume = 0x02;\n"
                + "task main() {"
                + "        TextOut(0,(MAXLINES - 0) * MAXLINES,\"Hallo\");\n"
                + "        PlayToneEx(300, 3000, volume, false);Wait(3000);\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator6.xml",
                brickConfiguration,
                true);
        ;
    }

    //ignore
    public void test7() throws Exception {
        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; float__speed; task main() {"

                + "          OnFwdReg(OUT_B,30,100);\n"
                + "          RotateMotorEx(OUT_B,-30,360.0*1,-100,true,true);\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator7.xml",
                brickConfiguration,
                true);
        ;
    }

    public void test8() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; float__speed; task main() {"
                + "        float item = 10;\n"
                + "        string item2 = \"TTTT\";\n"
                + "        bool item3 = true;\n"
                + "        TextOut(0,LCD_LINE0,\"Hallo\");\n"
                + "        TextOut(0,LCD_LINE0,\"Hallo\");\n"
                + "        TextOut(0,LCD_LINE0,\"Hallo\");\n"
                + "        item3 = false;\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator8.xml",
                brickConfiguration,
                true);
        ;
    }

    //
    public void test19() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; float__speed; task main() {"
                + "        float variablenName = 0;\n"

                + "OnFwdReg(OUT_AB,50);"
                + "GraphicOut(0,0,\"OLDGLASSES\");"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator9.xml",
                brickConfiguration,
                true);
        ;
    }

    //Test accepts some types, those don't exist in NXC. Should be fixed later, when the
    //OpenRoberta for NXT is ready

    // @Test
    //public void test9() throws Exception {

    //String a = "" //
    //+ MASMETHOD
    //+ "        floatitem=0;"
    //+ "        stringitem2=\"ss\";"
    //+ "        booleanitem3=true;"
    //+ "        floatitem4={1,2,3};"
    //+ "        stringitem5={\"a\",\"b\"};"
    //+ "        booleanitem6={true,false};"

    //+ SUFFIX

    //+ "}\n";

    //UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a,  "/ast/task/task_mainTask.xml");;
    //}

    @Test
    public void test11() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "    void test();"
                + "task main() {"
                + "    SetSensor(S3, SENSOR_COLORFULL);\n"
                + "        test();"

                + "}\n"
                + "    void test() {\n"
                + "SetSensorColorRed(3);"
                + "    }";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/methods/method_void_2.xml", brickConfiguration, true);
        ;
    }

    @Test
    public void test12() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "     void test(bool ___x);"
                + "task main() {"
                + "    SetSensor(S3, SENSOR_COLORFULL);\n"

                + "        test(true);"

                + "}\n"
                + "     void test(bool ___x) {\n"
                + "        if (___x) return;"
                + "SetSensorColorGreen(3);"
                + "    }";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/methods/method_if_return_1.xml", brickConfiguration, true);
        ;
    }

    @Test
    public void test13() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "     void test1(float ___x, float ___x2);"
                + "    void test2();"
                + "float ___variablenName;\n"
                + "bool ___variablenName2;\n"
                + "task main() {"
                + "___variablenName=0;\n"
                + "___variablenName2=true;\n"
                + "    SetSensor(S3, SENSOR_COLORFULL);\n"
                + "        test1(0, 0);"
                + "        test2();"

                + "}\n"
                + "     void test1(float ___x, float ___x2) {\n"
                + "        TextOut(___x,(MAXLINES - ___x2) * MAXLINES,\"Hallo\");\n"
                + "    }\n\n"
                + "    void test2() {\n"
                + "        if (___variablenName2) return;"
                + "SetSensorColorNone(3);"
                + "    }";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/methods/method_void_3.xml", brickConfiguration, true);
        ;
    }

    //ignore
    public void test14() throws Exception {

        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; task main() {"
                + "    string variablenName[]={\"a\",\"b\",\"c\"};\n"

                + "        TextOut(0,LCD_LINE0,"

                + "     float test(float x, string x2[]) {\n"
                + "       TextOut(x,LCD_LINE0,string(test(0,variablenName);\n"
                + "        return x;\n"
                + "    }"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/methods/method_return_1.xml", brickConfiguration, true);
        ;
    }

    //Test accepts some types, those don't exist in NXC. Should be fixed later, when the
    //OpenRoberta for NXT is ready
    //@Test
    //public void test15() throws Exception {

    //String a = "" //
    //+ MASMETHOD
    //+ "    string variablenName=BlocklyMethods.createListWithString(\"a\", \"b\", \"c\");\n"

    //+ "        hal.drawText(String.valueOf(test()), 0, 0);"

    //+ "    private Pickcolor test() {\n"
    //+ "        hal.drawText(String.valueOf(variablenName), 0, 0);\n"
    //+ "        return Pickcolor.NONE;\n"
    //+ "    }"
    //+"}\n";

    //    UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a,  "/syntax/methods/method_return_2.xml");;
    //}

    //Test accepts some types, those don't exist in NXC. Should be fixed later, when the
    //OpenRoberta for NXT is ready
    //@Test
    //public void test16() throws Exception {

    //    String a = "" //

    //        + "    string variablenName=BlocklyMethods.createListWithString(\"a\", \"b\", \"c\");\n"

    //        + "        hal.drawText(String.valueOf(test()), 0, 0);"
    //        + "}\n";

    //    UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a,  "/syntax/methods/method_if_return_2.xml");;
    //}

    @Test
    public void test17() throws Exception {
        // regression test for https://mp-devel.iais.fraunhofer.de/jira/browse/ORA-610
        final String a =
            "" //
                + DEFINES_INCLUDES
                + "string ___message;\n"
                + " task main() {"
                + "___message=\"exit\";"
                + "        if (___message == \"exit\") {\n"
                + "           TextOut(0,(MAXLINES - 0) * MAXLINES,\"done\");"
                + "        }\n"

                + "}\n";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/if_stmt4.xml", brickConfiguration, true);
        ;
    }

    @Test
    public void test18() throws Exception {
        final String a =
            "" //
                + DEFINES_INCLUDES
                + "    float ___item;\n"
                + "    string ___item2;\n"
                + "task main() {"
                + "    ___item2=\"cc\";\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator11.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void testCurveBlocks() throws Exception {
        final String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S4, SENSOR_LIGHT);\n"
                + "  SteerDriveEx( OUT_A, OUT_B, MIN(MAX(30, -100), 100), MIN(MAX(-20, -100), 100), true, 20 );\n"
                + "  SteerDriveEx( OUT_A, OUT_B, MIN(MAX(50, -100), 100), MIN(MAX(-50, -100), 100), false, 20 );\n"
                + "  while ( true ) {\n"
                + "    while ( true ) {\n"
                + "      if ( _readLightSensor( S4, 1 ) < 50 ) {\n"
                + "        SteerDrive( OUT_A, OUT_B, MIN(MAX(30, -100), 100), MIN(MAX(10, -100), 100), true );\n"
                + "          break;\n"
                + "      }\n"
                + "      if ( _readLightSensor( S4, 1 ) >= 50 ) {\n"
                + "        SteerDrive( OUT_A, OUT_B, MIN(MAX(10, -100), 100), MIN(MAX(30, -100), 100), true );\n"
                + "        break;\n"
                + "      }\n"
                + "      Wait( 15 );\n"
                + "    }\n"
                + "  }\n"
                + SUFFIX

                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/java_code_generator12.xml",
                brickConfiguration,
                true);
        ;
    }

    @Ignore
    public void testStmtForEach() throws Exception {
        final String a =
            "" //
                + DEFINES_INCLUDES
                + "float__speed; task main() {"
                + "ArrayList<Pickcolor>variablenName=BlocklyMethods.createListWithColour(Pickcolor.NONE,Pickcolor.RED,Pickcolor.BLUE);\n"
                + "    public void run() throwsException {\n"
                + "        for (PickcolorvariablenName2 : variablenName) {\n"
                + "            TextOut(String(variablenName2),0,0);\n"
                + "        }\n"
                + "    }\n"
                + "}\n";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/forEach_stmt.xml", brickConfiguration, true);
        ;
    }

    @Test
    public void visitToneAction_PlayTone50Hz500ms_ReturnsCorrectNXCProgram() throws Exception {
        String expectedResult =
            "" //
                + DEFINES_INCLUDES
                + "PlayToneEx(50, 500, volume, false);"
                + "Wait(500);"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/ast/actions/action_PlayTone.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void visitPlayNoteAction_PlayNote261dot626Hz2000ms_ReturnsCorrectNXCProgram() throws Exception {
        String expectedResult =
            "" //
                + DEFINES_INCLUDES
                + "PlayToneEx(261.626, 2000, volume, false);"
                + "Wait(2000);"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/ast/actions/action_PlayNote.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void check_noLoops_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "if (30 == 20) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "   }"
                + "}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/no_loops.xml", brickConfiguration, true);
        ;
    }

    @Test
    public void check_nestedLoopsNoBreakorContinue_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "if (30 == 20) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "   }"
                + "}"
                + "for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "}"
                + "}"
                + "}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/stmt/nested_loops.xml", brickConfiguration, true);
        ;
    }

    @Test
    public void check_loopsWithBreakAndContinue_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "float ___item2[3];"
                + "task main() {"
                + "float _____item2[] = {0, 0, 0};"
                + "___item2=_____item2;"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "if (30 == 20) {"
                + "     break;"
                + "} else if (30 == 12) {"
                + "     continue;"
                + "}"
                + "}"
                + "for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "if (30 == 20) {"
                + "     continue;"
                + "} else if (30 == 12) {"
                + "     break;"
                + "}"
                + "}"
                + "float ___item;"
                + "for (int ___i=0; ___i<ArrayLen(___item2); ++___i) {"
                + "___item = ___item2[___i];"
                + "if (30 == 20) {"
                + "     continue;"
                + "} else if (30 == 20) {"
                + "     break;"
                + "}"
                + "}"
                + "while (true) {"
                + "if (30 == 20) {"
                + "     continue;"
                + "} else if (30 == 20) {"
                + "     break;"
                + "}"
                + "}"
                + "for (int ___k0 = 0; ___k0 < 10; ___k0 += 1) {"
                + "if (30 == 20) {"
                + "     break;"
                + "} else if (30 == 20) {"
                + "     continue;"
                + "}"
                + "}"
                + "while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         break;"
                + "     }"
                + "     if (Sensor(S1) == true) {"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "}"
                + "}";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/stmt/loops_with_break_and_continue.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void check_loopWithBreakAndContinueInWait_returnsOneLabeledLoop() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (Sensor(S1) == true) {"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "   }"
                + "continue_loop1:"
                + "}"
                + "break_loop1:"
                + "}";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/stmt/loop_with_break_and_continue_inside_wait.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void check_loopsWithBreakAndContinueFitstInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (Sensor(S1) == true) {"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "   }"
                + "continue_loop1:"
                + "}"
                + "break_loop1:"
                + "for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "     if (___i < 10) {"
                + "         continue;"
                + "     }"
                + "}"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/stmt/two_loop_with_break_and_continue_one_inside_wait_another_not.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void check_twoNestedloopsFirstWithBreakAndContinueInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (Sensor(S1) == true) {"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "   }"
                + "for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "     if (___i < 10) {"
                + "        continue;"
                + "     }"
                + "}"
                + "continue_loop1:"
                + "}"
                + "break_loop1:"
                + "}";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/stmt/two_nested_loops_first_with_break_in_wait_second_not.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWait_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "             if (___i < 10) {"
                + "                 continue;"
                + "             }"
                + "         }"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     if (Sensor(S1) == true) {"
                + "         for (int ___j = 1; ___j < 10; ___j += 1) {"
                + "             if (___j < 10) {"
                + "                 continue;"
                + "             }"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "    Wait(15);"
                + "   }"
                + "continue_loop1:"
                + "}"
                + "break_loop1:"
                + "}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/stmt/loop_with_nested_two_loops_inside_wait.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstAndThirdLoopLabeled() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         for (int ___j = 1; ___j < 10; ___j += 1) {"
                + "             if (___j < 10) {"
                + "                 continue;"
                + "             }"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     if (Sensor(S1) == true) {"
                + "         for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "         while (true) {"
                + "             if (Sensor(S1) == true) {"
                + "                 goto continue_loop3;"
                + "                 break;"
                + "             }"
                + "             if (Sensor(S1) == true) {"
                + "                 goto break_loop3;"
                + "                 break;"
                + "             }"
                + "              Wait(15);"
                + "         }"
                + "         continue_loop3:"
                + "         }"
                + "         break_loop3:"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "   }"
                + "continue_loop1:"
                + "}"
                + "break_loop1:"
                + "}";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/stmt/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml",
                brickConfiguration,
                true);
        ;
    }

    @Test
    public void check_threeLoopsWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstThirdAndFourthLoopLabeled() throws Exception {
        String a =
            "" //
                + DEFINES_INCLUDES
                + "task main() {"
                + "    SetSensor(S1, SENSOR_TOUCH);\n"
                + "while (true) {"
                + "   while (true) {"
                + "     if (Sensor(S1) == true) {"
                + "         for (int ___j = 1; ___j < 10; ___j += 1) {"
                + "             if (___j < 10) {"
                + "                 continue;"
                + "             }"
                + "         }"
                + "         goto continue_loop1;"
                + "         break;"
                + "     }"
                + "     if (Sensor(S1) == true) {"
                + "         for (int ___i = 1; ___i < 10; ___i += 1) {"
                + "         while (true) {"
                + "             if (Sensor(S1) == true) {"
                + "                 goto continue_loop3;"
                + "                 break;"
                + "             }"
                + "             if (Sensor(S1) == true) {"
                + "                 goto break_loop3;"
                + "                 break;"
                + "             }"
                + "             Wait(15);"
                + "         }"
                + "         continue_loop3:"
                + "         }"
                + "         break_loop3:"
                + "         goto break_loop1;"
                + "         break;"
                + "     }"
                + "     Wait(15);"
                + "   }"
                + "continue_loop1:"
                + "}"
                + "break_loop1:"
                + "while (true) {"
                + "     if (10 < 10) {"
                + "         continue;"
                + "     }"
                + "}"
                + "while (true) {"
                + "         while (true) {"
                + "             if (Sensor(S1) == true) {"
                + "                 goto continue_loop5;"
                + "                 break;"
                + "             }"
                + "             if (Sensor(S1) == true) {"
                + "                 goto break_loop5;"
                + "                 break;"
                + "             }"
                + "             Wait(15);"
                + "         }"
                + "continue_loop5:"
                + "}"
                + "break_loop5:"
                + "}";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/stmt/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml",
                brickConfiguration,
                true);
        ;
    }
}
