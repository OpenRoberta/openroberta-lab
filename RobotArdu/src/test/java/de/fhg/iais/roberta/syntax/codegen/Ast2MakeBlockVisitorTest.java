package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperMakeBlock;

public class Ast2MakeBlockVisitorTest {
    HelperMakeBlock h = new HelperMakeBlock();

    private static final String MAIN_METHOD1 = ""
        + "#include <math.h> \n"
        + "#include <MeMCore.h> \n"
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
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
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
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
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
            + "MeLineFollower lineFinder(PORT_1);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "bool item;"
            + MAIN_METHOD2
            + "item=lineFinder.readSensors()&1;"
            + "}"
            + "void loop(){"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_light_sensor2.xml", true);
    }

    @Test
    public void TouchSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeTouchSensor myTouch1(PORT_1);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (myTouch1.touched()) {"
            + "        delay(500);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_touch_sensor.xml", true);
    }

    @Test
    public void PIRMotionSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MePIRMotionSensor pir(PORT_3);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (pir.isHumanDetected()) {"
            + "        delay(500);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_pir_motion_sensor.xml", true);
    }

    @Test
    public void JoyStickTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + "MeJoystick myJoystick2(PORT_2);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (myJoystick2.readX() == 0 ) {"
            + "        motor1.run(30);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_joystick_sample.xml", true);
    }
    
    @Test
    public void GyroscopeTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + "MeGyro myGyro(PORT_1);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (myGyro.getGyroX() > 10 ) {"
            + "        motor1.run(30);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_gyroscope_sensor.xml", true);
    }
    
    @Test
    public void AccelerometerTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + "MeGyro myGyro(PORT_1);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (myGyro.getAngleX() > 10 ) {"
            + "        motor1.run(30);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_accelerometer_sensor.xml", true);
    }
    
    @Test
    public void FlameSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + "MeFlameSensor flameSensor1(PORT_1);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (flameSensor1.readAnalog() > 20 ) {"
            + "        motor1.run(30);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_flame_sensor.xml", true);
    }
    
    @Test
    public void AmbientLightSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor1(M1);"
            + "MeLightSensor myLight0(PORT_0);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (myLight0.read() > 10 ) {"
            + "        motor1.run(30);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_ambientlight_sensor.xml", true);
    }
    
    @Test
    public void SoundSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeSoundSensor mySound3(PORT_3);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "if (mySound3.strength() > 0) {"
            + "        delay(500);}\n"

            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_sound_sensor.xml", true);
    }

    @Test
    public void TemperatureSensorTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "RobertaFunctions rob;"
            + "MeDCMotor motor2(M2);"
            + "MeDCMotor motor1(M1);"
            + "MeDrive myDrive(M2,M1);"
            + "MeHumiture myTemp1(PORT_1);"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "}"
            + "void loop(){"
            + "myTemp1.update();"
            + "if (myTemp1.getTemperature() < 20) {"
            + "        myDrive.drive(30, 1);}\n"

            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_temperature_sensor.xml", true);
    }
    
    @Test
    public void TimerTest() throws Exception {

        final String a = "" //
            + MAIN_METHOD1
            + "#include<CountUpDown.h>"
            + "CountUpDownTimer T(UP,HIGH);"
            + "RobertaFunctions rob;"
            + "MeRGBLedrgbled_7(7,7==7?2:4);"
            + "double item;"
            + MAIN_METHOD2
            + "T.StartTimer();"
            + "}"
            + "void loop(){"
            + "T.Timer();"
            + "if (T.ShowSeconds() > 60 ) {"
            + "       delay(500);}\n"
            + "}\n";

        this.h.assertCodeIsOk(a, "/syntax/code_generator/java/makeblock/get_timer.xml", true);
    }

}
