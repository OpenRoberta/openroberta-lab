package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class Ast2BotNrollVisitorTest {
    HelperBotNroll h = new HelperBotNroll();

    private static final String MAIN_METHOD1 = ""
        + "#include<math.h>\n"
        + "#include<CountUpDownTimer.h>\n"
        + "#include<BnrOneA.h>//Bot'nRollONEAlibrary"
        + "#include<BnrRescue.h>//Bot'nRollCoSpaceRescueModulelibrary"
        + "#include<RobertaFunctions.h>//OpenRobertalibrary"
        + "#include<BnrRoberta.h>//OpenRobertalibrary"
        + "#include<SPI.h>//SPIcommunicationlibraryrequiredbyBnrOne.cpp"
        + "#include<Wire.h>//alibraryrequiredbyBnrRescue.cppfortheadditionalsonar"
        + "BnrOneAone;"
        + "BnrRescuebrm;"
        + "RobertaFunctionsrob;"
        + "BnrRoberta bnr(one, brm);";

    private static final String DEFINES = "" + "#defineSSPIN2" + "#defineMODULE_ADDRESS0x2C" + "bytecolorsLeft[3]={0,0,0};" + "bytecolorsRight[3]={0,0,0};";

    private static final String MAIN_METHOD2 = ""
        + "voidsetup(){"
        + "Wire.begin();"
        + "Serial.begin(9600);//setsbaudrateto9600bpsforprintingvaluesatserialmonitor."
        + "one.spiConnect(SSPIN);//startstheSPIcommunicationmodule"
        + "brm.i2cConnect(MODULE_ADDRESS);//startsI2Ccommunication"
        + "brm.setModuleAddress(0x2C);"
        + "one.stop();"
        + "bnr.setOne(one);"
        + "bnr.setBrm(brm);";

    @Test
    public void test() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + MAIN_METHOD2
            + "}"
            + "voidloop(){"
            + "        one.lcd1(\"Hallo\");\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator.xml", true);
    }

    @Test
    public void test1() throws Exception {

        final String a = ""
            + MAIN_METHOD1
            + DEFINES
            + MAIN_METHOD2
            + "}"
            + "voidloop(){"
            + "        for ( int k0 = 0; k0 < 10; k0+=1 ) {\n"
            + "           one.lcd1(\"Hallo\");"
            + "        }\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator1.xml", true);
    }

    @Test
    public void test2() throws Exception {

        final String a = ""
            + MAIN_METHOD1
            + DEFINES
            + "String item;"
            + "int item2SysLen;"
            + "double *item2;"
            + MAIN_METHOD2
            + "one.obstacleEmitters(ON);"
            + "item = \"yyy\";"
            + "item2=(double*)malloc(sizeof(double)*0);"
            + "item2SysLen=0;"
            + "}"
            + "voidloop(){"
            + "item += \"zzz\";"
            + "while (true) {"
            + "    if (bnr.infraredSensorObstacle(1) == true) {"
            + "        break;"
            + "    }"
            + "    delay(15);"
            + "}"
            + "while (true) {"
            + "    if (( bnr.buttonIsPressed(2) ) ? bnr.buttonIsPressed(123) : bnr.buttonIsPressed(3)) {"
            + "        break;"
            + "    }"
            + "    delay(15);}"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator2.xml", true);
    }

    @Test
    public void test3() throws Exception {

        final String a = ""
            + MAIN_METHOD1
            + "CountUpDownTimer T(UP, HIGH);"
            + DEFINES
            + "int itemSysLen;"
            + "bool*item;"
            + "intitem2SysLen;"
            + "String*item2;"
            + "double item3;"
            + MAIN_METHOD2
            + "one.obstacleEmitters(ON);"
            + "T.StartTimer();"
            + "itemSysLen=3;"
            + "item=(bool*)malloc(sizeof(bool)*itemSysLen);"
            + "rob.createArray(item,itemSysLen,true,true,true);"
            + "item2SysLen=3;"
            + "item2=(String*)malloc(sizeof(String)*item2SysLen);"
            + "rob.createArray(item2,item2SysLen,\"\",\"\",\"\");"
            + "item3 = T.ShowSeconds();"
            + "}"
            + "voidloop(){"
            + "T.Timer();"
            + "for(double  item4 = 0; item4 < itemSysLen;  item4++) {"
            + "    T.ResetTimer();"
            + "    item3 = one.readAdc(1) / 10.23;"
            + "}"
            + "for(double  item5 = 0; item5 < item2SysLen;  item5++) {"
            + "    one.movePID(bnr.readBearing(), bnr.readBearing());"
            + "if (bnr.infraredSensorPresence(3)) {"
            + "    one.stop();}}"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator3.xml", true);
    }

    @Test
    public void test4() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + MAIN_METHOD2
            + "one.obstacleEmitters(ON);brm.setSonarStatus(ENABLE);"
            + "}"
            + "voidloop(){"
            + "if (bnr.infraredSensorObstacle(1)) {"
            + "bnr.move1mTime(1, 30, 100);"
            + "bnr.moveTimePID(10, 30, 100);"
            + "}"
            + "if (bnr.ultrasonicDistance(0) == 55) {"
            + "while (bnr.buttonIsPressed(1)) {"
            + "tone(9, 300, 100);}}"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator4.xml", true);
    }

    @Test
    public void test5() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + MAIN_METHOD2
            + "}"
            + "voidloop(){"
            + "       one.move1m(1, 0);"
            + "       bnr.move1mTime(1,30,0);"
            + "       one.movePID(0,-0);"
            + "       tone(9,0,0);"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator5.xml", true);
    }

    @Test
    public void test6() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + MAIN_METHOD2
            + "}"
            + "voidloop(){"
            + "        one.lcd1(\"Hallo\");\n"
            + "        tone(9,300, 3000);\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator6.xml", true);
    }

    @Test
    public void test7() throws Exception {
        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + MAIN_METHOD2
            + "}"
            + "voidloop(){"
            + "          one.move1m(1,30);\n"
            + "          bnr.move1mTime(1,30,1);\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator7.xml", true);
    }

    @Test
    public void test8() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "        double item;\n"
            + "        String item2;\n"
            + "        bool item3;\n"
            + MAIN_METHOD2
            + "        item = 10;\n"
            + "        item2 = \"TTTT\";\n"
            + "        item3 = true;\n"
            + "}"
            + "voidloop(){"
            + "        one.lcd1(item);\n"
            + "        one.lcd1(item2.c_str());\n"
            + "        one.lcd1(bnr.boolToString(item3));\n"
            + "        item3 = false;\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator8.xml", true);
    }

    @Test
    public void test11() throws Exception {
        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "void test() {\n"
            + "    one.led(HIGH);"
            + "}"
            + MAIN_METHOD2
            + "}"

            + "voidloop(){"
            + "    test();"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/methods/method_void_2.xml", true);
    }

    @Test
    public void test12() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "void test(bool x) {\n"
            + "    if (x) return;"
            + "    one.led(HIGH);"
            + "}"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "test(true);"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/methods/method_if_return_1.xml", true);
    }

    @Test
    public void test13() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "    double variablenName;\n"
            + "    bool variablenName2;\n"
            + "     void test1(double x, double x2) {\n"
            + "        one.lcd1(\"Hallo\");\n"
            + "    }\n\n"
            + "    void test2() {\n"
            + "        if (variablenName2) return;"
            + "        one.led(HIGH);"
            + "    }"

            + MAIN_METHOD2
            + "    variablenName=0;\n"
            + "    variablenName2=true;\n"
            + "}"
            + "void loop(){"
            + "        test1(0, 0);"
            + "        test2();"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/methods/method_void_3.xml", true);
    }

    @Test
    public void test14() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "int variablenNameSysLen;\n"
            + "String *variablenName;"
            + "     double test(double x, Stringx2[]) {\n"
            + "       one.lcd1(x2);\n"
            + "        return x;\n"
            + "    }"
            + MAIN_METHOD2
            + "variablenNameSysLen=3;\n"
            + "variablenName = (String*)malloc(sizeof(String)*variablenNameSysLen);"
            + "rob.createArray(variablenName,variablenNameSysLen,\"a\",\"b\",\"c\");}"
            + "void loop(){"
            + "one.lcd1(test(0,variablenName));"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/methods/method_return_1.xml", true);
    }

    @Test
    public void test17() throws Exception {
        // regression test for https://mp-devel.iais.fraunhofer.de/jira/browse/ORA-610
        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "    String message;\n"
            + MAIN_METHOD2
            + "    message=\"exit\";\n"

            + "}"
            + "voidloop(){"
            + "        if (message == \"exit\") {\n"
            + "           one.lcd1(\"done\");"
            + "        }\n"

            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/stmt/if_stmt4.xml", true);
    }

    @Test
    public void test18() throws Exception {
        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "    double item;\n"
            + "    String item2;\n"
            + MAIN_METHOD2
            + "    item=0;\n"
            + "    item2=\"cc\";\n"
            + "}"
            + "voidloop(){"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/botnroll/java_code_generator11.xml", true);
    }

    @Test
    public void testStmtForEach() throws Exception {
        final String a = "" //
            + MAIN_METHOD1
            + DEFINES
            + "int item2SysLen;"
            + "double*item2;"
            + MAIN_METHOD2
            + "item2SysLen=3;"
            + "item2=(double*)malloc(sizeof(double)*item2SysLen);"
            + "rob.createArray(item2,item2SysLen,0,0,0);"
            + "}"

            + "voidloop(){"
            + "for(double  item = 0; item < item2SysLen;  item++) {"
            + "    bnr.moveTimePID(item, item, 100);}"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml", true);
    }

}
