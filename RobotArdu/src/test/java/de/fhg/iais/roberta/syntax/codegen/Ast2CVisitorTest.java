package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.ArduConfiguration;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.arduino.ActorPort;
import de.fhg.iais.roberta.testutil.Helper;

public class Ast2CVisitorTest {

    private static final String MAIN_METHOD = ""
        + "#include<math.h>\n"
        + "#include<CountUpDownTimer.h>\n"
        + "#include<BnrOneA.h>//Bot'nRollONEAlibrary"
        + "#include<BnrRescue.h>//Bot'nRollCoSpaceRescueModulelibrary"
        + "#include<RobertaFunctions.h>//OpenRobertalibrary"
        + "#include<SPI.h>//SPIcommunicationlibraryrequiredbyBnrOne.cpp"
        + "#include<Wire.h>//alibraryrequiredbyBnrRescue.cppfortheadditionalsonar"
        + "BnrOneAone;"
        + "BnrRescuebrm;"
        + "RobertaFunctionsrob(one,brm);"
        + "#defineSSPIN2"
        + "#defineMODULE_ADDRESS0x2C"
        + "bytecolorsLeft[3]={0,0,0};"
        + "bytecolorsRight[3]={0,0,0};"
        + "voidsetup(){"
        + "Wire.begin();"
        + "Serial.begin(9600);//setsbaudrateto9600bpsforprintingvaluesatserialmonitor."
        + "one.spiConnect(SSPIN);//startstheSPIcommunicationmodule"
        + "brm.i2cConnect(MODULE_ADDRESS);//startsI2Ccommunication"
        + "brm.setModuleAddress(0x2C);"
        + "one.stop();"
        + "rob.setOne(one);"
        + "rob.setBrm(brm);";

    private static ArduConfiguration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        final ArduConfiguration.Builder builder = new ArduConfiguration.Builder();
        builder.addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        brickConfiguration = (ArduConfiguration) builder.build();
    }

    @Test
    public void test() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "voidloop(){"
            + "        one.lcd1(\"Hallo\");\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        final String a = ""
            + MAIN_METHOD
            + "}"
            + "voidloop(){"
            + "        for ( float k0 = 0; k0 < 10; k0+=1 ) {\n"
            + "           one.lcd1(\"Hallo\");"
            + "        }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator1.xml");
    }

    @Test
    public void test4() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "one.obstacleEmitters(ON);brm.setSonarStatus(ENABLE);"
            + "}"
            + "voidloop(){"
            + "if (rob.infraredSensorObstacle(1)) {"
            + "rob.move1mTime(1, 30, 100);"
            + "rob.moveTimePID(10, 30, 100);"
            + "}"
            + "if (rob.ultrasonicDistance(0) == 55) {"
            + "while (rob.buttonIsPressed(1)) {"
            + "tone(9, 300, 100);}}"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator4.xml");
    }

    @Test
    public void test5() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "voidloop(){"
            + "       one.move1m(1, 0);"
            + "       rob.move1mTime(1,30,0);"
            + "       one.movePID(0,-0);"
            + "       tone(9,0,0);"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "voidloop(){"
            + "        one.lcd1(\"Hallo\");\n"
            + "        tone(9,300, 3000);\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator6.xml");
    }

    @Test
    public void test7() throws Exception {
        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "voidloop(){"
            + "          one.move1m(1,30);\n"
            + "          rob.move1mTime(1,30,1);\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator7.xml");
    }

    @Test
    public void test8() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "        double item = 10;\n"
            + "        String item2 = \"TTTT\";\n"
            + "        bool item3 = true;\n"
            + "voidloop(){"
            + "        one.lcd1(item);\n"
            + "        one.lcd1(item2.c_str());\n"
            + "        one.lcd1(rob.boolToString(item3));\n"
            + "        item3 = false;\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator8.xml");
    }

    @Test
    public void test11() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "void test() {\n"
            + "    one.led(HIGH);"
            + "}"
            + "voidloop(){"
            + "    test();"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_void_2.xml");
    }

    @Test
    public void test12() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "void test(bool x) {\n"
            + "    if (x) return;"
            + "    one.led(HIGH);"
            + "}"
            + "void loop(){"
            + "test(true);"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_if_return_1.xml");
    }

    @Test
    public void test13() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "    double variablenName=0;\n"
            + "    bool variablenName2=true;\n"
            + "     void test1(double x, double x2) {\n"
            + "        one.lcd1(\"Hallo\");\n"
            + "    }\n\n"
            + "    void test2() {\n"
            + "        if (variablenName2) return;"
            + "        one.led(HIGH);"
            + "    }"

            + "void loop(){"
            + "        test1(0, 0);"
            + "        test2();"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_void_3.xml");
    }

    @Test
    public void test14() throws Exception {

        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "String variablenNameRaw[3]={\"a\",\"b\",\"c\"};\n"
            + "String *variablenName = variablenNameRaw;"
            + "     double test(double x, Stringx2[]) {\n"
            + "       one.lcd1(x2);\n"
            + "        return x;\n"
            + "    }"
            + "void loop(){"
            + "one.lcd1(test(0,variablenName));"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_return_1.xml");
    }

    @Test
    public void test17() throws Exception {
        // regression test for https://mp-devel.iais.fraunhofer.de/jira/browse/ORA-610
        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "    String message=\"exit\";\n"
            + "voidloop(){"
            + "        if (message == \"exit\") {\n"
            + "           one.lcd1(\"done\");"
            + "        }\n"

            + "}\n";

        assertCodeIsOk(a, "/syntax/stmt/if_stmt4.xml");
    }

    @Test
    public void test18() throws Exception {
        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "    double item=0;\n"
            + "    String item2=\"cc\";\n"
            + "voidloop(){"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator11.xml");
    }

    @Test
    public void testStmtForEach() throws Exception {
        final String a = "" //
            + MAIN_METHOD
            + "}"
            + "double item2Raw[3] = {0, 0, 0};"
            + "double* item2 = item2Raw;"
            + "voidloop(){"
            + "for(double  item = 0; item < sizeof(item2Raw)/sizeof(item2Raw[0]);  item++) {"
            + "    rob.moveTimePID(item, item, 100);}"
            + "}\n";

        assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        // Assert.assertEquals(a, Helper.generateString(fileName, brickConfiguration));
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateString(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
