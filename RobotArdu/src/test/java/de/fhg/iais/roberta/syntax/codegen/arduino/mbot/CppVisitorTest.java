package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore // TODO: reactivate this test REFACTORING
public class CppVisitorTest extends MbotAstTest {

    private static final String MAIN_METHOD1 =
        ""
            + "#include <MeMCore.h> \n"
            + "#include <Wire.h>\n"
            + "#include <SoftwareSerial.h>\n"
            + "#include <NEPODefs.h>"
            + "#include \"MeDrive.h\"\n\n";

    private static final String MAIN_METHOD2 = "" + "void setup(){" + "Serial.begin(9600);";

    @Test
    public void motor1m1Test() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeDCMotor motor1(M1);"
                + MAIN_METHOD2
                + "}"
                + " voidloop(){motor1.run((60)*255/100);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/motor1m1.xml", false);
    }

    @Test
    public void visitUltrasonicSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeUltrasonicSensor ultraSensor4(PORT_4);"
                + "MeDCMotor motor1(M1);"
                + MAIN_METHOD2
                + "}"
                + "void loop(){"
                + "        motor1.run((ultraSensor4.distanceCm())*255/100);\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/makeblock/get_ultrasonic_sensor.xml",
                false);
    }

    @Test
    public void visitInfraredSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeLineFollower lineFinder2(PORT_2);"
                + "MeDCMotormotor1(M1);"
                + MAIN_METHOD2
                + "}"
                + "void loop(){"
                + "if(lineFinder2.readSensors()&1){"
                + "motor1.run((60)*255/100);}"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/get_light_sensor2.xml", false);
    }

    @Test
    public void visitTouchSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeTouchSensor myTouch1(PORT_1);"
                + MAIN_METHOD2
                + "}"
                + "void loop(){"
                + "if (myTouch1.touched()) {"
                + "        delay(500);}\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/get_touch_sensor.xml", false);
    }

    @Test
    public void visitMotionSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MePIRMotionSensor pir3(PORT_3);"
                + "MeDCMotormotor1(M1);"
                + MAIN_METHOD2
                + "}"
                + "void loop(){"
                + "if (pir3.isHumanDetected()) {"
                + "        motor1.run((60)*255/100);}\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/get_motion_sensor.xml", false);
    }

    @Test
    public void visitJoystickSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeJoystick myJoystick3(PORT_3);"
                + "MeDCMotormotor1(M1);"
                + MAIN_METHOD2
                + "}"
                + "void loop(){"
                + "if (myJoystick3.readX() == 0 ) {"
                + "        motor1.run((30)*255/100);}\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/makeblock/get_joystick_sample.xml",
                false);
    }

    @Test
    public void visitGyroscopeTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeGyro myGyro1(PORT_1);"
                + "MeDCMotor motor1(M1);"
                + MAIN_METHOD2
                + "myGyro1.begin();"
                + "}"
                + "myGyro1.update();"
                + "void loop(){"
                + "if (myGyro1.getGyroX() > 10 ) {"
                + "        motor1.run((60)*255/100);}\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/makeblock/get_gyroscope_sensor.xml",
                false);
    }

    @Test
    public void visitAccelerometerTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeGyro myGyro1(PORT_1);"
                + "MeDCMotor motor1(M1);"
                + MAIN_METHOD2
                + "myGyro1.begin();"
                + "}"
                + "myGyro1.update();"
                + "void loop () {"
                + "if (myGyro1.getAngleX() > 10 ) {"
                + "        motor1.run((30)*255/100);}}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/makeblock/get_accelerometer_sensor.xml",
                false);
    }

    @Test
    public void visitFlameSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeFlameSensor flameSensor1(PORT_1);"
                + "MeDCMotor motor1(M1);"
                + MAIN_METHOD2
                + "}"
                + "void loop(){"
                + "if (flameSensor1.readAnalog() > 20 ) {"
                + "        motor1.run((60)*255/100);}\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/get_flame_sensor.xml", false);
    }

    @Test
    public void visitLightSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeLightSensormyLight0(0);"
                + "MeDCMotor motor1(M1);"
                + MAIN_METHOD2
                + "}"
                + "void loop() {"
                + "if (myLight0.read()*100/1023 == 0 ) {"
                + "        motor1.run((60)*255/100);}}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/get_light_sensor.xml", false);
    }

    @Test
    public void visitSoundSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeSoundSensor mySound3(PORT_3);"
                + "double item;"
                + MAIN_METHOD2
                + "item=0;"
                + "}"
                + "void loop () {"
                + "if (mySound3.strength() > 0) {"
                + "        delay(500);}}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/get_sound_sensor.xml", false);
    }

    @Test
    public void visitTemperatureSensorTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "MeHumiture myTemp3(PORT_3);"
                + "MeDCMotormotor1(M1);"
                + MAIN_METHOD2
                + "}"
                + "myTemp3.update();"
                + "void loop(){"
                + "if (myTemp3.getTemperature() < 20) {"
                + "        motor1.run((30)*255/100);}}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/syntax/code_generator/java/makeblock/get_temperature_sensor.xml",
                false);
    }

    @Test
    public void visitTimerTest() throws Exception {

        final String a =
            "" //
                + MAIN_METHOD1
                + "double item;"
                + "unsigned long __time = millis();"
                + MAIN_METHOD2
                + "item=0;"
                + "}"
                + "void loop(){"
                + "if ((int)(millis()-__time)  > 60 ) {"
                + "       delay(500);}\n"
                + "}\n";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/code_generator/java/makeblock/get_timer.xml", false);
    }

    @Test
    public void visitToneAction_PlayTone50Hz500ms_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + MAIN_METHOD1
                + "MeBuzzer buzzer;"
                + MAIN_METHOD2
                + "}"
                + "buzzer.tone(8, 50, 500);"
                + "delay(20);";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/ast/actions/action_PlayTone.xml", false);
    }

    @Test
    public void visitPlayNoteAction_PlayNote261dot626Hz2000ms_ReturnsCorrectCppProgram() throws Exception {
        String expectedResult =
            "" //
                + MAIN_METHOD1
                + "MeBuzzer buzzer;"
                + MAIN_METHOD2
                + "}"
                + "buzzer.tone(8, 261.626, 2000);"
                + "delay(20);"
                + "\n";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/ast/actions/action_PlayNote.xml", false);
    }

    @Test
    public void visitLedAction() throws Exception {
        String expectedResult =
            "" //
                + MAIN_METHOD1
                + "MeRGBLedrgbled_7(7,7==7?2:4);"
                + MAIN_METHOD2
                + "}"
                + "voidloop(){"
                + "rgbled_7.setColor(0,0,120,120);"
                + "rgbled_7.show();"
                + "delay(1000);"
                + "rgbled_7.setColor(0,120,0,120);"
                + "rgbled_7.show();"
                + "delay(1000);}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/code_generator/java/makeblock/led.xml", false);
    }

    @Test
    public void visitSteerAction() throws Exception {
        String expectedResult =
            "" //
                + MAIN_METHOD1
                + "MeDrivemyDrive(M2,M1);"
                + MAIN_METHOD2
                + "}"
                + "voidloop(){"
                + "myDrive.steer(50*255/100,80*255/100,1,2000);"
                + "delay(5000);}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/code_generator/java/makeblock/steer.xml", false);
    }

    @Test
    public void visitDriveAction() throws Exception {
        String expectedResult =
            "" //
                + MAIN_METHOD1
                + "MeDrivemyDrive(M2,M1);"
                + MAIN_METHOD2
                + "}"
                + "voidloop(){"
                + "myDrive.drive(60*255/100,1,2000);"
                + "delay(2000);}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/syntax/code_generator/java/makeblock/drive.xml", false);
    }

    @Test
    public void visitButtonSensor() throws Exception {
        String expectedResult =
            "" //
                + MAIN_METHOD1
                + "MeBuzzerbuzzer;"
                + MAIN_METHOD2
                + "}"
                + "voidloop(){"
                + "if((analogRead(7)<100))"
                + "{buzzer.tone(8,261.626,2000);"
                + "delay(20);}}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/syntax/code_generator/java/makeblock/get_button.xml",
                false);
    }

    @Test
    public void visitPotentoimeterSensor() throws Exception {
        String expectedResult =
            "" //
                + MAIN_METHOD1
                + "MePotentiometermyVoltageSensor4(PORT_4);"
                + "MeBuzzerbuzzer;"
                + MAIN_METHOD2
                + "}"
                + "voidloop(){"
                + "if(myVoltageSensor4.read()*5/970==3)"
                + "{buzzer.tone(8,261.626,2000);"
                + "delay(20);}}";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/syntax/code_generator/java/makeblock/potentiometer.xml",
                false);
    }

}
