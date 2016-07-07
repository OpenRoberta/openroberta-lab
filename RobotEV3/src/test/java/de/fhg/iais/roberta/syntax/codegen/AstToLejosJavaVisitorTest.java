package de.fhg.iais.roberta.syntax.codegen;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.EV3Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.testutil.Helper;

public class AstToLejosJavaVisitorTest {

    private static final String MAIN_CLASS = "" //
        + "public class Test {\n"
        + "private static final boolean TRUE = true;";

    private static final String IMPORTS = "" //
        + "package generated.main;\n\n"
        + "import de.fhg.iais.roberta.runtime.*;\n"
        + "import de.fhg.iais.roberta.runtime.ev3.*;\n\n"
        + "import de.fhg.iais.roberta.mode.general.*;\n"
        + "import de.fhg.iais.roberta.mode.action.*;\n"
        + "import de.fhg.iais.roberta.mode.sensor.*;\n\n"
        + "import de.fhg.iais.roberta.components.*;\n"
        + "import java.util.LinkedHashSet;\n"
        + "import java.util.Set;\n"
        + "import java.util.List;\n"
        + "import java.util.ArrayList;\n"
        + "import java.util.Arrays;\n"
        + "import lejos.remote.nxt.NXTConnection;\n\n";

    private static final String BRICK_CONFIGURATION = "" //
        + "    brickConfiguration = new Configuration.Builder()\n"
        + "    .setWheelDiameter(5.6)\n"
        + "    .setTrackWidth(17.0)\n"
        + "    .addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))\n"
        + "    .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))\n"
        + "    .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))\n"
        + "    .addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC))\n"
        + "    .build();\n\n";

    private static final String BRICK_CONFIGURATION_DECL = "private static Configuration brickConfiguration;\n";

    private static final String USED_SENSORS_DECL = "private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();\n";

    private static final String MAIN_METHOD = ""
        + "    private Hal hal = new Hal(brickConfiguration, usedSensors);\n\n"
        + "    public static void main(String[] args) {\n"
        + "        try {\n"
        + BRICK_CONFIGURATION
        + "            new Test().run();\n"
        + "        } catch ( Exception e ) {\n"
        + "            Hal.displayExceptionWaitForKeyPress(e);\n"
        + "        }\n"
        + "    }\n\n";
    private static final String SUFFIX = "";
    private static Configuration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        Configuration.Builder builder = new EV3Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH)).addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC));
        brickConfiguration = builder.build();
    }

    @Test
    public void test() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
            + "        hal.drawText(\"Hallo\", 0, 3);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
            + "        for ( float k0 = 0; k0 < 10; k0+=1 ) {\n"
            + "            hal.drawText(\"Hallo\", 0, 3);\n"
            + "        }\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator1.xml");
    }

    @Test
    public void test2() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + "private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S1, SensorType.TOUCH, TouchSensorMode.TOUCH), new UsedSensor(SensorPort.S3, SensorType.COLOR, ColorSensorMode.COLOUR)));\n"
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
            + "        if ( hal.isPressed(SensorPort.S1) ) {\n"
            + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "        } else if ( PickColor.RED == hal.getColorSensorColour(SensorPort.S3) ) {\n"
            + "        if ( TRUE ) {\n"
            + "            while ( true ) {\n"
            + "                hal.drawPicture(ShowPicture.EYESOPEN, 0, 0);\n\n"
            + "                hal.turnOnRegulatedMotor(ActorPort.B,30);"
            + "            }\n"
            + "        }\n"
            + "        }\n"
            + "        hal.playFile(1);\n"
            + "        hal.setVolume(50);\n"
            + "        for ( float i = 1; i < 10; i += 1 ) {\n\n"
            + "           hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);"
            + "        }\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator2.xml");
    }

    @Test
    public void test3() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + "private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S1, SensorType.TOUCH, TouchSensorMode.TOUCH), new UsedSensor(SensorPort.S4, SensorType.ULTRASONIC, UltrasonicSensorMode.DISTANCE)));\n"
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
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

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator3.xml");
    }

    @Test
    public void test4() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + "private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>(Arrays.asList(new UsedSensor(SensorPort.S4, SensorType.INFRARED, InfraredSensorMode.DISTANCE), new UsedSensor(SensorPort.S4, SensorType.ULTRASONIC, UltrasonicSensorMode.DISTANCE), new UsedSensor(SensorPort.S2, SensorType.GYRO, GyroSensorMode.RESET)"
            + ", new UsedSensor(SensorPort.S1, SensorType.TOUCH, TouchSensorMode.TOUCH)));\n"
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
            + "        if ( 5 < hal.getRegulatedMotorSpeed(ActorPort.B) ) {\n\n\n"
            + "            hal.turnOnRegulatedMotor(ActorPort.B,30);\n"
            + "            hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);\n"
            + "            hal.rotateDirectionRegulated(TurnDirection.RIGHT, 50);\n"
            + "        }\n"
            + "        if ( (hal.getRegulatedMotorTachoValue(ActorPort.A, MotorTachoMode.ROTATION) + hal.getInfraredSensorDistance(SensorPort.S4)) == hal.getUltraSonicSensorDistance(SensorPort.S4) ) {\n"
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

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator4.xml");
    }

    @Test
    public void test5() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"

            + "        hal.turnOnRegulatedMotor(ActorPort.B,0);"
            + "        hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,0);"
            + "        hal.rotateDirectionRegulated(TurnDirection.RIGHT,0);"
            + "        hal.setVolume(50);"
            + "        hal.playTone(0,0);"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"

            + "        hal.drawText(\"Hallo\", 0, 0);\n"
            + "        hal.playTone(300, 3000);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator6.xml");
    }

    @Test
    public void test7() throws Exception {
        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"

            + "        hal.turnOnRegulatedMotor(ActorPort.B,30);\n"
            + "        hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator7.xml");
    }

    @Test
    public void test8() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "        float item = 10;\n"
            + "        String item2 = \"TTTT\";\n"
            + "        boolean item3 = true;\n"
            + "    public void run() throwsException {\n"
            + "        hal.drawText(String.valueOf(item), 0, 0);\n"
            + "        hal.drawText(String.valueOf(item2), 0, 0);\n"
            + "        hal.drawText(String.valueOf(item3), 0, 0);\n"
            + "        item3 = false;\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator8.xml");
    }

    @Test
    public void test19() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "        float variablenName = 0;\n"
            + "    public void run() throwsException {\n"

            + "hal.regulatedDrive(DriveDirection.FOREWARD,50);"
            + "hal.drawPicture(ShowPicture.OLDGLASSES,0,0);"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator9.xml");
    }

    @Test
    public void test9() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "        floatitem=0;"
            + "        Stringitem2=\"ss\";"
            + "        booleanitem3=true;"
            + "        ArrayList<Float>item4=BlocklyMethods.createListWithNumber(1,2,3);"
            + "        ArrayList<String>item5=BlocklyMethods.createListWithString(\"a\",\"b\");"
            + "        ArrayList<Boolean>item6=BlocklyMethods.createListWithBoolean(true,false);"
            + "        ArrayList<PickColor>item7=BlocklyMethods.createListWithColour(PickColor.RED,PickColor.BLACK,PickColor.NONE);"
            + "        PickColoritem8=PickColor.NONE;"
            + "    public void run() throwsException {\n"

            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/task/task_mainTask.xml");
    }

    @Test
    public void test10() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
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
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
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
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    public void run() throwsException {\n"
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
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    float variablenName=0;\n"
            + "    boolean variablenName2=true;\n"
            + "    public void run() throwsException {\n"
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
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    ArrayList<String> variablenName=BlocklyMethods.createListWithString(\"a\", \"b\", \"c\");\n"
            + "    public void run() throwsException {\n"
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
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    ArrayList<String> variablenName=BlocklyMethods.createListWithString(\"a\", \"b\", \"c\");\n"
            + "    public void run() throwsException {\n"
            + "        hal.drawText(String.valueOf(test()), 0, 0);"
            + "    }\n\n"
            + "    private PickColor test() {\n"
            + "        hal.drawText(String.valueOf(variablenName), 0, 0);\n"
            + "        return PickColor.NONE;\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_return_2.xml");
    }

    @Test
    public void test16() throws Exception {

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    ArrayList<String> variablenName=BlocklyMethods.createListWithString(\"a\", \"b\", \"c\");\n"
            + "    public void run() throwsException {\n"
            + "        hal.drawText(String.valueOf(test()), 0, 0);"
            + "    }\n\n"
            + "    private PickColor test() {\n"
            + "        if (true) return PickColor.RED;\n"
            + "        hal.drawText(String.valueOf(variablenName), 0, 0);\n"
            + "        return PickColor.NONE;\n"
            + "    }"
            + "}\n";

        assertCodeIsOk(a, "/syntax/methods/method_if_return_2.xml");
    }

    @Test
    public void test17() throws Exception {
        // regression test for https://mp-devel.iais.fraunhofer.de/jira/browse/ORA-610
        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    String message=\"exit\";\n"
            + "    public void run() throwsException {\n"
            + "        if (message.equals(\"exit\")) {\n"
            + "            hal.drawText(\"done\", 0, 0);"
            + "        }\n"
            + "    }\n\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/stmt/if_stmt4.xml");
    }

    @Test
    public void test18() throws Exception {
        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "    float item=0;\n"
            + "    String item2=\"cc\";\n"
            + "    public void run() throwsException {\n"
            + "    }\n\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator11.xml");
    }

    @Test
    public void testStmtForEach() throws Exception {
        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION_DECL
            + USED_SENSORS_DECL
            + MAIN_METHOD
            + "ArrayList<PickColor>variablenName=BlocklyMethods.createListWithColour(PickColor.NONE,PickColor.RED,PickColor.BLUE);\n"
            + "    public void run() throwsException {\n"
            + "        for (PickColorvariablenName2 : variablenName) {\n"
            + "            hal.drawText(String.valueOf(variablenName2),0,0);\n"
            + "        }\n"
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        // Assert.assertEquals(a, Helper.generateString(fileName, brickConfiguration));
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateString(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
