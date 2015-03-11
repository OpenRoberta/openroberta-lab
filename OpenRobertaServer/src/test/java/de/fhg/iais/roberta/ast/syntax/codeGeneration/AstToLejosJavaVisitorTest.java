package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ev3.EV3Actors;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Sensors;
import de.fhg.iais.roberta.ev3.components.EV3Actor;
import de.fhg.iais.roberta.ev3.components.EV3Sensor;

public class AstToLejosJavaVisitorTest {
    private static final String MAIN_CLASS = "" //
        + "public class Test {\n"
        + "private static final boolean TRUE = true;";
    private static final String IMPORTS = "" //
        + "package generated.main;\n\n"
        + "import de.fhg.iais.roberta.ast.syntax.*;\n"
        + "import de.fhg.iais.roberta.codegen.lejos.BlocklyMethods;\n"
        + "import de.fhg.iais.roberta.codegen.lejos.Hal;\n\n"
        + "import de.fhg.iais.roberta.ast.syntax.action.*;\n"
        + "import de.fhg.iais.roberta.ast.syntax.sensor.*;\n"
        + "import de.fhg.iais.roberta.ev3.*;\n"
        + "import de.fhg.iais.roberta.ev3.components.*;\n\n"
        + "import java.util.LinkedHashSet;\n"
        + "import java.util.Set;\n"
        + "import java.util.List;\n"
        + "import java.util.ArrayList;\n"
        + "import java.util.Arrays;\n";

    private static final String BRICK_CONFIGURATION = "" //
        + "    private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()\n"
        + "    .setWheelDiameter(5.6)\n"
        + "    .setTrackWidth(17.0)\n"
        + "    .addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT))\n"
        + "    .addActor(ActorPort.B, new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT))\n"
        + "    .addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR))\n"
        + "    .addSensor(SensorPort.S2, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR))\n"
        + "    .build();\n\n";

    private static final String MAIN_METHOD = "" //
        + "    private Hal hal = new Hal(brickConfiguration, usedSensors);\n\n"
        + "    public static void main(String[] args) {\n"
        + "        try {\n"
        + "            new Test().run();\n"
        + "        } catch ( Exception e ) {\n"
        + "            lejos.hardware.lcd.TextLCD lcd = lejos.hardware.ev3.LocalEV3.get().getTextLCD();\n"
        + "            lcd.clear();\n"
        + "            lcd.drawString(\"Fehler im EV3\", 0, 2);\n"
        + "            if (e.getMessage() != null) {\n"
        + "                lcd.drawString(\"Fehlermeldung\", 0, 4);\n"
        + "                lcd.drawString(e.getMessage(), 0, 5);\n"
        + "            }\n"
        + "            lcd.drawString(\"Press any key\", 0, 7);\n"
        + "            lejos.hardware.Button.waitForAnyPress();\n"
        + "        }\n"
        + "    }\n\n";
    private static final String SUFFIX = "";
    private static EV3BrickConfiguration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        EV3BrickConfiguration.Builder builder = new EV3BrickConfiguration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR)).addSensor(SensorPort.S2, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR));
        brickConfiguration = builder.build();
    }

    @Test
    public void test() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            // + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.drawText(\"Hallo\", 0, 3);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            //       + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        for ( int i0 = 0; i0 < 10; i0++ ) {\n"
            + "            hal.drawText(\"Hallo\", 0, 3);\n"
            + "        }\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator1.xml");
    }

    @Test
    public void test2() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>(Arrays.asList(EV3Sensors.EV3_TOUCH_SENSOR, EV3Sensors.EV3_COLOR_SENSOR));\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            //   + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        if ( hal.isPressed(SensorPort.S1) ) {\n"
            + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "        } else if ( Pickcolor.RED == hal.getColorSensorColour(SensorPort.S3) ) {\n"
            + "        if ( TRUE ) {\n"
            + "            while ( true ) {\n"
            + "                hal.drawPicture(ShowPicture.EYESOPEN, 0, 0);\n\n"
            + "                hal.turnOnRegulatedMotor(ActorPort.B,30);"
            + "            }\n"
            + "        }\n"
            + "        }\n"
            + "        hal.playFile(1);\n"
            + "        hal.setVolume(50);\n"
            + "        for ( int i = 1; i <= 10; i += 1 ) {\n\n"
            + "           hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);"
            + "        }\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator2.xml");
    }

    @Test
    public void test3() throws Exception {

        String a =
            "" //
                + IMPORTS
                + MAIN_CLASS
                + BRICK_CONFIGURATION
                + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>(Arrays.asList(EV3Sensors.EV3_TOUCH_SENSOR, EV3Sensors.EV3_ULTRASONIC_SENSOR));\n"
                + MAIN_METHOD
                + "    public void run() {\n"
                //          + "        hal = new Hal(brickConfiguration, usedSensors);\n"
                + "        if ( hal.isPressed(SensorPort.S1) ) {\n"
                + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
                + "        } else {\n"
                + "            if ( hal.isPressed(SensorPort.S1) ) {\n"
                + "                hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
                + "            } else if ( 0==hal.getUltraSonicSensorDistance(SensorPort.S4) ) {\n"
                + "                hal.drawPicture(ShowPicture.FLOWERS, 15, 15);\n"
                + "            } else {\n"
                + "            if ( TRUE ) {\n"
                + "                while ( !hal.isPressed(BrickKey.UP) ) {\n\n"
                + "                     hal.turnOnRegulatedMotor(ActorPort.B,30);"
                + "                }\n"
                + "            }\n"
                + "            }\n"
                + "        }\n"
                + SUFFIX
                + "    }\n"
                + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator3.xml");
    }

    @Test
    public void test4() throws Exception {

        String a =
            "" //
                + IMPORTS
                + MAIN_CLASS
                + BRICK_CONFIGURATION
                + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>(Arrays.asList(EV3Sensors.EV3_IR_SENSOR,EV3Sensors.EV3_ULTRASONIC_SENSOR,EV3Sensors.EV3_GYRO_SENSOR,EV3Sensors.EV3_TOUCH_SENSOR));\n"
                + MAIN_METHOD
                + "    public void run() {\n"
                //       + "        hal = new Hal(brickConfiguration, usedSensors);\n"
                + "        if ( 5 < hal.getRegulatedMotorSpeed(ActorPort.B) ) {\n\n\n"
                + "            hal.turnOnRegulatedMotor(ActorPort.B,30);\n"
                + "            hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);\n"
                + "            hal.rotateDirectionRegulated(ActorPort.A, ActorPort.B, false, TurnDirection.RIGHT, 50);\n"
                + "        }\n"
                + "        if ( hal.getRegulatedMotorTachoValue(ActorPort.A, MotorTachoMode.ROTATION) + hal.getInfraredSensorDistance(SensorPort.S4) == hal.getUltraSonicSensorDistance(SensorPort.S4) ) {\n"
                + "            hal.ledOff();\n"
                + "        } else {\n"
                + "            hal.resetGyroSensor(SensorPort.S2);\n"
                + "        if ( TRUE ) {\n"
                + "            while ( hal.isPressed(SensorPort.S1) ) {\n"
                + "                hal.drawPicture(ShowPicture.OLDGLASSES, 0, 0);\n"
                + "                hal.clearDisplay();\n"
                + "            }\n"
                + "         }\n"
                + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
                + "        }\n"
                + SUFFIX
                + "    }\n"
                + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator4.xml");
    }

    @Test
    public void test5() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            //        + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.turnOnRegulatedMotor(ActorPort.B,0);"
            + "        hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,0);"
            + "        hal.rotateDirectionRegulated(ActorPort.A,ActorPort.B,false,TurnDirection.RIGHT,0);"
            + "        hal.setVolume(50);"
            + "        hal.playTone(0,0);"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            //       + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.drawText(\"Hallo\", 0, 0);\n"
            + "        hal.playTone(300, 3000);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator6.xml");
    }

    @Test
    public void test7() throws Exception {
        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            //    + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.turnOnRegulatedMotor(ActorPort.B,30);\n"
            + "        hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator7.xml");
    }

    @Test
    public void test8() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "        float item = 10;\n"
            + "        String item2 = \"TTTT\";\n"
            + "        boolean item3 = true;\n"
            + "    public void run() {\n"
            //   + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.drawText(String.valueOf(item), 0, 0);\n"
            + "        hal.drawText(String.valueOf(item2), 0, 0);\n"
            + "        hal.drawText(String.valueOf(item3), 0, 0);\n"
            + "        item3 = false;\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator8.xml");
    }

    @Test
    public void test9() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "        floatitem=0;"
            + "        Stringitem2=\"ss\";"
            + "        booleanitem3=true;"
            + "        ArrayList<Float>item4=BlocklyMethods.createListWith(1,2,3);"
            + "        ArrayList<String>item5=BlocklyMethods.createListWith(\"a\",\"b\");"
            + "        ArrayList<Boolean>item6=BlocklyMethods.createListWith(true,false);"
            + "        ArrayList<Pickcolor>item7=BlocklyMethods.createListWith(Pickcolor.RED,Pickcolor.MAGENTA,Pickcolor.NONE);"
            + "        Pickcoloritem8=Pickcolor.NONE;"
            + "    public void run() {\n"
            //  + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/ast/task/task_mainTask.xml");
    }

    @Test
    public void test10() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    floatvariablenName=0;\n"
            + "    floatvariablenName2=0;\n"
            + "    public void run() {\n"
            // + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);"
            + "        macheEtwas(10, 10);"
            + "    }\n\n"
            + "    private void macheEtwas(float x, float x2) {\n"
            + "        hal.drawPicture(ShowPicture.OLDGLASSES, x, x2);\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_void_1.xml");
    }

    @Test
    public void test11() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            // + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        test();"
            + "    }\n\n"
            + "    private void test() {\n"
            + "        hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_void_2.xml");
    }

    @Test
    public void test12() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    public void run() {\n"
            // + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        test(true);"
            + "    }\n\n"
            + "    private void test(boolean x) {\n"
            + "        if (x) return;"
            + "        hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_if_return_1.xml");
    }

    @Test
    public void test13() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    float variablenName=0;\n"
            + "    boolean variablenName2=true;\n"
            + "    public void run() {\n"
            //  + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        test1(0, 0);"
            + "        test2();"
            + "    }\n\n"
            + "    private void test1(float x, float x2) {\n"
            + "        hal.drawText(\"Hallo\", x, x2);\n"
            + "    }\n\n"
            + "    private void test2() {\n"
            + "        if (variablenName2) return;"
            + "        hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_void_3.xml");
    }

    @Test
    public void test14() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    ArrayList<String> variablenName=BlocklyMethods.createListWith(\"a\", \"b\", \"c\");\n"
            + "    public void run() {\n"
            // + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.drawText(String.valueOf(test(0, variablenName)), 0, 0);"
            + "    }\n\n"
            + "    private float test(float x, ArrayList<String> x2) {\n"
            + "        hal.drawText(String.valueOf(x2), x, 0);\n"
            + "        return x;\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_return_1.xml");
    }

    @Test
    public void test15() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    ArrayList<String> variablenName=BlocklyMethods.createListWith(\"a\", \"b\", \"c\");\n"
            + "    public void run() {\n"
            // + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.drawText(String.valueOf(test()), 0, 0);"
            + "    }\n\n"
            + "    private Pickcolor test() {\n"
            + "        hal.drawText(String.valueOf(variablenName), 0, 0);\n"
            + "        return Pickcolor.NONE;\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_return_2.xml");
    }

    @Test
    public void test16() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + "private Set<EV3Sensors> usedSensors = new LinkedHashSet<EV3Sensors>();\n"
            + MAIN_METHOD
            + "    ArrayList<String> variablenName=BlocklyMethods.createListWith(\"a\", \"b\", \"c\");\n"
            + "    public void run() {\n"
            //  + "        hal = new Hal(brickConfiguration, usedSensors);\n"
            + "        hal.drawText(String.valueOf(test()), 0, 0);"
            + "    }\n\n"
            + "    private Pickcolor test() {\n"
            + "        if (true) return Pickcolor.RED;\n"
            + "        hal.drawText(String.valueOf(variablenName), 0, 0);\n"
            + "        return Pickcolor.NONE;\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_if_return_2.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        // Assert.assertEquals(a, Helper.generateString(fileName, brickConfiguration));
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateString(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
