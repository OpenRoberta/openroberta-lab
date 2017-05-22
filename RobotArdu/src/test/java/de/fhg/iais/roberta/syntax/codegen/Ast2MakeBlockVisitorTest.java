package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperMakeBlock;

public class Ast2MakeBlockVisitorTest {
    HelperMakeBlock h = new HelperMakeBlock();

    private static final String MAIN_METHOD1 = ""
        + "#include <math.h> \n"
        + "#include <MeOrion.h> \n"
        + "#include <Wire.h>\n"
        + "#include <SoftwareSerial.h>\n"
        + "#include <CountUpDownTimer.h>\n"
        + "#include <RobertaFunctions.h>\n"
        + "#include \"MeDrive.h\"\n\n";

    private static final String MAIN_METHOD2 = "" + "void setup(){" + "Serial.begin(9600);";

    @Test
    public void motor1m1Test() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "        motor1.run(30);\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/motor1m1.xml", true);
    }
    
    @Test
    public void ultrasonicSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + "MeUltrasonicSensor ultraSensor(PORT_4);"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "        motor1.run(ultraSensor.distanceCm());\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_ultrasonic_sensor.xml", true);
    }
    
    @Test
    public void LightSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeLightSensor myLight1(PORT_1);"
            + MAIN_METHOD2
            + "}"
            + "double item;"
            + "void loop(){"
            + "if (myLight1.read() != 0) {"
            + "        delay(500);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_light_sensor.xml", true);
    }
    
    public void TouchSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeTouchSensor myTouch1(PORT_1);"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (myTouch1.touched()) {"
            + "        delay(500);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_touch_sensor.xml", true);
    }
    
    public void SoundSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeSoundSensor mySound3(PORT_3);"
            + MAIN_METHOD2
            + "}"
            + "double item;"
            + "void loop(){"
            + "if (mySound3.strength() > 0) {"
            + "        delay(500);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_sound_sensor.xml", true);
    }

}
