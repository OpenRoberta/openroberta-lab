package de.fhg.iais.roberta.syntax.codegen.festobionic;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CppVisitorTest extends FestiobionicAstTest {

    private static final String INCLUDE =
        ""
            + "#define _ARDUINO_STL_NOT_NEEDED"
            + "#define LED_BUILTIN 13"
            + "#include <Arduino.h>"
            + "#include <NEPODefs.h>";

    private static final String DEFINES =
        ""
            + "int_led_L=LED_BUILTIN;";

    @Test
    public void visitLightActionLedTest() throws Exception {

        final String a =
            "" //
                + INCLUDE
                + DEFINES
                + "void setup(){"
                + "pinMode(_led_L,OUTPUT);"
                + "}"
                + "void loop() {"
                + "digitalWrite(_led_L,HIGH);"
                + "digitalWrite(_led_L,LOW);"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithExportXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/festobionic/led.xml");
    }

    @Test
    public void visitMotorOnActionServoTest() throws Exception {

        final String a =
            "" //
                + INCLUDE
                + "#include <ESP32Servo/src/ESP32Servo.h>"
                + "Servo _servo_S;"
                + DEFINES
                + "Servo _servo_S2;"
                + "void setup(){"
                + "_servo_S.attach(25);"
                + "pinMode(_led_L,OUTPUT);"
                + "_servo_S2.attach(26);"
                + "}"
                + "void loop() {"
                + "_servo_S.write(90);"
                + "_servo_S.write(30);" 
                + "_servo_S2.write(90);"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithExportXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/festobionic/servo.xml");
    }

    @Test
    public void visitTimeSensorTest() throws Exception {

        final String a =
            "" //
                + INCLUDE
                + "double ___Element;"
                + DEFINES
                + "unsignedlong__time_1=millis();"
                + "void setup(){"
                + "pinMode(_led_L,OUTPUT);"
                + "___Element=(int)(millis()-__time_1);"
                + "}"
                + "void loop() {"
                + "__time_1=millis();"
                + "while(true) {"
                + "if((int)(millis()-__time_1)==50) {"
                + "break;"
                + "}"
                + "delay(1);"
                + "}"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithExportXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/festobionic/timer.xml");
    }

    @Test
    public void visitSerialWriteActionTest() throws Exception {

        final String a =
            "" //
                + INCLUDE
                + DEFINES
                + "void setup(){"
                + "Serial.begin(9600);"
                + "pinMode(_led_L,OUTPUT);"
                + "}"
                + "void loop(){"
                + "Serial.println(\"Hallo\");"
                + "Serial.println(0);"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithExportXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/festobionic/serial_print.xml");
    }
}
