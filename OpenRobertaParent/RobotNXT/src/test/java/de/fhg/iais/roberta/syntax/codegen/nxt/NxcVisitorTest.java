package de.fhg.iais.roberta.syntax.codegen.nxt;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.nxt.NxtConfiguration;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class NxcVisitorTest {
    private final HelperNxtForXmlTest h = new HelperNxtForXmlTest();
    //TODO: change diameter and trackwidth to changeable
    // when sensors are added to nxt, fix the sensors description here

    private static final String IMPORTS_CONSTANTS = "" //
        + "#define WHEELDIAMETER 5.6\n"
        + "#define TRACKWIDTH 11.0\n"
        + "#define MAXLINES 8 \n"
        + "#include\"NEPODefs.h\" // contains NEPO declarations for the NXC NXT API resources";
    private static final String MASMETHOD = "" //
        + "    SetSensor(S1, SENSOR_TOUCH);\n"
        + "    SetSensor(S2, SENSOR_LOWSPEED);\n";
    //+ "    SetSensor(S3, SENSOR_LIGHT);\n"
    //+ "    SetSensor(S4, SENSOR_SOUND);\n";

    private static final String SUFFIX = "";
    private static NxtConfiguration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        final NxtConfiguration.Builder builder = new NxtConfiguration.Builder();
        builder.setTrackWidth(11).setWheelDiameter(5.6);
        builder.addActor(new ActorPort("A", "A"), new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            new ActorPort("B", "B"),
            new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(new SensorPort("1", "S1"), new Sensor(SensorType.TOUCH)).addSensor(new SensorPort("2", "S2"), new Sensor(SensorType.ULTRASONIC));
        brickConfiguration = (NxtConfiguration) builder.build();
    }

    @Test
    public void test() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
            + "        TextOut(0,(MAXLINES - 3) * MAXLINES,\"Hallo\");\n"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        final String a = ""
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD

            + "        for ( int k0 = 0; k0 < 10; k0+=1 ) {\n"
            + "           TextOut(0,(MAXLINES - 3) * MAXLINES,\"Hallo\");"
            + "        }\n"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator1.xml");
    }

    //ignore
    public void test2() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; task main() {"
            + MASMETHOD

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
            + "        for ( float i = 1; i < 10; i += 1 ) {\n\n"
            + "        RotateMotor(OUT_B,30,360.0*1);"
            + "        }\n"
            + SUFFIX
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator2.xml");
    }

    //ignore
    public void test3() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; float__speed; task main() {"
            + MASMETHOD

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

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator3.xml");
    }

    // ignore
    public void test4() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; float__speed; task main() {"
            + MASMETHOD

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

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator4.xml");
    }

    public void test5() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; float__speed; task main() {"
            + MASMETHOD

            + "          OnFwdReg(OUT_B,0,100);"
            + "        RotateMotorEx(OUT_B,-30,360.0*0,-100,true,true);"
            + "       OnFwdSync(OUT_AB,0,100);"
            + "        SetVolume(50);"
            + "        PlayTone(0,0);"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "byte volume = 0x02;\n"
            + "task main() {"
            + MASMETHOD
            + "        TextOut(0,(MAXLINES - 0) * MAXLINES,\"Hallo\");\n"
            + "        PlayToneEx(300, 3000, volume, false);Wait(3000);\n"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator6.xml");
    }

    //ignore
    public void test7() throws Exception {
        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; float__speed; task main() {"
            + MASMETHOD

            + "          OnFwdReg(OUT_B,30,100);\n"
            + "          RotateMotorEx(OUT_B,-30,360.0*1,-100,true,true);\n"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator7.xml");
    }

    public void test8() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; float__speed; task main() {"
            + MASMETHOD
            + "        float item = 10;\n"
            + "        string item2 = \"TTTT\";\n"
            + "        bool item3 = true;\n"
            + "        TextOut(0,LCD_LINE0,\"Hallo\");\n"
            + "        TextOut(0,LCD_LINE0,\"Hallo\");\n"
            + "        TextOut(0,LCD_LINE0,\"Hallo\");\n"
            + "        item3 = false;\n"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator8.xml");
    }

    //
    public void test19() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; float__speed; task main() {"
            + MASMETHOD
            + "        float variablenName = 0;\n"

            + "OnFwdReg(OUT_AB,50);"
            + "GraphicOut(0,0,\"OLDGLASSES\");"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator9.xml");
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

    //assertCodeIsOk(a, "/ast/task/task_mainTask.xml");
    //}

    @Test
    public void test11() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "    void test();"
            + "task main() {"
            + MASMETHOD

            + "        test();"

            + "}\n"
            + "    void test() {\n"
            + "SetSensorColorRed(3);"
            + "    }";

        assertCodeIsOk(a, "/syntax/methods/method_void_2.xml");
    }

    @Test
    public void test12() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "     void test(bool x);"
            + "task main() {"
            + MASMETHOD

            + "        test(true);"

            + "}\n"
            + "     void test(bool x) {\n"
            + "        if (x) return;"
            + "SetSensorColorGreen(3);"
            + "    }";

        assertCodeIsOk(a, "/syntax/methods/method_if_return_1.xml");
    }

    @Test
    public void test13() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "     void test1(float x, float x2);"
            + "    void test2();"
            + "float variablenName;\n"
            + "bool variablenName2;\n"
            + "task main() {"
            + "variablenName=0;\n"
            + "variablenName2=true;\n"
            + MASMETHOD
            + "        test1(0, 0);"
            + "        test2();"

            + "}\n"
            + "     void test1(float x, float x2) {\n"
            + "        TextOut(x,(MAXLINES - x2) * MAXLINES,\"Hallo\");\n"
            + "    }\n\n"
            + "    void test2() {\n"
            + "        if (variablenName2) return;"
            + "SetSensorColorNone(3);"
            + "    }";

        assertCodeIsOk(a, "/syntax/methods/method_void_3.xml");
    }

    //ignore
    public void test14() throws Exception {

        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; task main() {"
            + MASMETHOD
            + "    string variablenName[]={\"a\",\"b\",\"c\"};\n"

            + "        TextOut(0,LCD_LINE0,"

            + "     float test(float x, string x2[]) {\n"
            + "       TextOut(x,LCD_LINE0,string(test(0,variablenName);\n"
            + "        return x;\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_return_1.xml");
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

    //    assertCodeIsOk(a, "/syntax/methods/method_return_2.xml");
    //}

    //Test accepts some types, those don't exist in NXC. Should be fixed later, when the
    //OpenRoberta for NXT is ready
    //@Test
    //public void test16() throws Exception {

    //    String a = "" //

    //        + "    string variablenName=BlocklyMethods.createListWithString(\"a\", \"b\", \"c\");\n"

    //        + "        hal.drawText(String.valueOf(test()), 0, 0);"
    //        + "}\n";

    //    assertCodeIsOk(a, "/syntax/methods/method_if_return_2.xml");
    //}

    @Test
    public void test17() throws Exception {
        // regression test for https://mp-devel.iais.fraunhofer.de/jira/browse/ORA-610
        final String a = "" //
            + IMPORTS_CONSTANTS
            + "string message;\n"
            + " task main() {"
            + "message=\"exit\";"
            + MASMETHOD
            + "        if (message == \"exit\") {\n"
            + "           TextOut(0,(MAXLINES - 0) * MAXLINES,\"done\");"
            + "        }\n"

            + "}\n";

        assertCodeIsOk(a, "/syntax/stmt/if_stmt4.xml");
    }

    @Test
    public void test18() throws Exception {
        final String a = "" //
            + IMPORTS_CONSTANTS
            + "    float item;\n"
            + "    string item2;\n"
            + "task main() {"
            + "    item=0;\n"
            + "    item2=\"cc\";\n"
            + MASMETHOD
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator11.xml");
    }

    @Test
    public void testCurveBlocks() throws Exception {
        final String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
            + "  SteerDriveEx( OUT_A, OUT_B,SpeedTest(30),SpeedTest(-20), true, 20 );\n"
            + "  SteerDriveEx( OUT_A, OUT_B, SpeedTest(50),SpeedTest(-50), false, 20 );\n"
            + "  while ( true ) {\n"
            + "    while ( true ) {\n"
            + "      if ( SensorLight( S3, \"LIGHT\" ) < 50 ) {\n"
            + "        SteerDrive( OUT_A, OUT_B, SpeedTest(30),SpeedTest(10), true );\n"
            + "          break;\n"
            + "      }\n"
            + "      if ( SensorLight( S3, \"LIGHT\" ) >= 50 ) {\n"
            + "        SteerDrive( OUT_A, OUT_B, SpeedTest(10),SpeedTest(30), true );\n"
            + "        break;\n"
            + "      }\n"
            + "      Wait( 15 );\n"
            + "    }\n"
            + "  }\n"
            + SUFFIX

            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator12.xml");
    }

    @Ignore
    public void testStmtForEach() throws Exception {
        final String a = "" //
            + IMPORTS_CONSTANTS
            + "float__speed; task main() {"
            + MASMETHOD
            + "ArrayList<Pickcolor>variablenName=BlocklyMethods.createListWithColour(Pickcolor.NONE,Pickcolor.RED,Pickcolor.BLUE);\n"
            + "    public void run() throwsException {\n"
            + "        for (PickcolorvariablenName2 : variablenName) {\n"
            + "            TextOut(String(variablenName2),0,0);\n"
            + "        }\n"
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

    @Test
    public void visitToneAction_PlayTone50Hz500ms_ReturnsCorrectNXCProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS_CONSTANTS
            + "PlayToneEx(50, 500, volume, false);"
            + "Wait(500);"
            + "}";

        assertCodeIsOk(expectedResult, "/ast/actions/action_PlayTone.xml");
    }

    @Test
    public void visitPlayNoteAction_PlayNote261dot626Hz2000ms_ReturnsCorrectNXCProgram() throws Exception {
        String expectedResult = "" //
            + IMPORTS_CONSTANTS
            + "PlayToneEx(261.626, 2000, volume, false);"
            + "Wait(2000);"
            + "}";

        assertCodeIsOk(expectedResult, "/ast/actions/action_PlayNote.xml");
    }

    @Test
    public void check_noLoops_returnsNoLabeledLoops() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
            + "if (30 == 20) {"
            + "   while (true) {"
            + "     if (Sensor(S1) == true) {"
            + "         break;"
            + "     }"
            + "     Wait(15);"
            + "   }"
            + "}}";

        assertCodeIsOk(a, "/syntax/stmt/no_loops.xml");
    }

    @Test
    public void check_nestedLoopsNoBreakorContinue_returnsNoLabeledLoops() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
            + "while (true) {"
            + "if (30 == 20) {"
            + "   while (true) {"
            + "     if (Sensor(S1) == true) {"
            + "         break;"
            + "     }"
            + "     Wait(15);"
            + "   }"
            + "}"
            + "for (int i = 1; i < 10; i += 1) {"
            + "}"
            + "}"
            + "}";
        assertCodeIsOk(a, "/syntax/stmt/nested_loops.xml");
    }

    @Test
    public void check_loopsWithBreakAndContinue_returnsNoLabeledLoops() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "float item2[3];"
            + "task main() {"
            + "float __item2[] = {0, 0, 0};"
            + "item2=__item2;"
            + MASMETHOD
            + "while (true) {"
            + "if (30 == 20) {"
            + "     break;"
            + "} else if (30 == 12) {"
            + "     continue;"
            + "}"
            + "}"
            + "for (int i = 1; i < 10; i += 1) {"
            + "if (30 == 20) {"
            + "     continue;"
            + "} else if (30 == 12) {"
            + "     break;"
            + "}"
            + "}"
            + "for (float item=0; item<sizeof(item2)/sizeof(item2[0]); item++) {"
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
            + "for (int k0 = 0; k0 < 10; k0 += 1) {"
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
        assertCodeIsOk(a, "/syntax/stmt/loops_with_break_and_continue.xml");
    }

    @Test
    public void check_loopWithBreakAndContinueInWait_returnsOneLabeledLoop() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
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
        assertCodeIsOk(a, "/syntax/stmt/loop_with_break_and_continue_inside_wait.xml");
    }

    @Test
    public void check_loopsWithBreakAndContinueFitstInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
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
            + "for (int i = 1; i < 10; i += 1) {"
            + "     if (i < 10) {"
            + "         continue;"
            + "     }"
            + "}"
            + "}";

        assertCodeIsOk(a, "/syntax/stmt/two_loop_with_break_and_continue_one_inside_wait_another_not.xml");
    }

    @Test
    public void check_twoNestedloopsFirstWithBreakAndContinueInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
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
            + "for (int i = 1; i < 10; i += 1) {"
            + "     if (i < 10) {"
            + "        continue;"
            + "     }"
            + "}"
            + "continue_loop1:"
            + "}"
            + "break_loop1:"
            + "}";
        assertCodeIsOk(a, "/syntax/stmt/two_nested_loops_first_with_break_in_wait_second_not.xml");
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWait_returnsFirstLoopLabeled() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
            + "while (true) {"
            + "   while (true) {"
            + "     if (Sensor(S1) == true) {"
            + "         for (int i = 1; i < 10; i += 1) {"
            + "             if (i < 10) {"
            + "                 continue;"
            + "             }"
            + "         }"
            + "         goto break_loop1;"
            + "         break;"
            + "     }"
            + "     if (Sensor(S1) == true) {"
            + "         for (int j = 1; j < 10; j += 1) {"
            + "             if (j < 10) {"
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

        assertCodeIsOk(a, "/syntax/stmt/loop_with_nested_two_loops_inside_wait.xml");
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstAndThirdLoopLabeled() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
            + "while (true) {"
            + "   while (true) {"
            + "     if (Sensor(S1) == true) {"
            + "         for (int j = 1; j < 10; j += 1) {"
            + "             if (j < 10) {"
            + "                 continue;"
            + "             }"
            + "         }"
            + "         goto continue_loop1;"
            + "         break;"
            + "     }"
            + "     if (Sensor(S1) == true) {"
            + "         for (int i = 1; i < 10; i += 1) {"
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
        assertCodeIsOk(a, "/syntax/stmt/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml");
    }

    @Test
    public void check_threeLoopsWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstThirdAndFourthLoopLabeled() throws Exception {
        String a = "" //
            + IMPORTS_CONSTANTS
            + "task main() {"
            + MASMETHOD
            + "while (true) {"
            + "   while (true) {"
            + "     if (Sensor(S1) == true) {"
            + "         for (int j = 1; j < 10; j += 1) {"
            + "             if (j < 10) {"
            + "                 continue;"
            + "             }"
            + "         }"
            + "         goto continue_loop1;"
            + "         break;"
            + "     }"
            + "     if (Sensor(S1) == true) {"
            + "         for (int i = 1; i < 10; i += 1) {"
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
        assertCodeIsOk(a, "/syntax/stmt/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a.replaceAll("\\s+", ""), this.h.generateNXC(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
