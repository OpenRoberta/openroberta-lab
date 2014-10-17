package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;

public class AstToLejosJavaVisitorTest {
    private static final String MAIN_CLASS = "" //
        + "public class Test {\n";
    private static final String IMPORTS = "" //
        + "package generated.main;\n\n"
        + "import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;\n"
        + "import de.fhg.iais.roberta.ast.syntax.HardwareComponent;\n"
        + "import de.fhg.iais.roberta.codegen.lejos.Hal;\n\n"
        + "import de.fhg.iais.roberta.ast.syntax.action.*;\n"
        + "import de.fhg.iais.roberta.ast.syntax.sensor.*;\n";

    private static final String BRICK_CONFIGURATION = "" //
        + "    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n"
        + "    .setWheelDiameter(5.6)\n"
        + "    .setTrackWidth(17.0)\n"
        + "    .addActor(ActorPort.A, new HardwareComponent(HardwareComponentType.EV3MediumRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))\n"
        + "    .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))\n"
        + "    .addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3TouchSensor))\n"
        + "    .addSensor(SensorPort.S2, new HardwareComponent(HardwareComponentType.EV3UltrasonicSensor))\n"
        + "    .build();\n\n";
    private static final String MAIN_METHOD = "" //
        + "    public static void main(String[] args) {\n"
        + "        new Test().run();\n"
        + "    }\n\n";
    private static final String SUFFIX = "" //
        + "        try {\n"
        + "            Thread.sleep(2000);\n"
        + "        } catch ( InterruptedException e ) {\n"
        + "            // ok\n"
        + "        }\n";
    private static BrickConfiguration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        BrickConfiguration.Builder builder = new BrickConfiguration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new HardwareComponent(HardwareComponentType.EV3MediumRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3TouchSensor)).addSensor(
            SensorPort.S2,
            new HardwareComponent(HardwareComponentType.EV3UltrasonicSensor));
        brickConfiguration = builder.build();
    }

    @Test
    public void test() throws Exception {
        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + MAIN_METHOD
            + "    public void run() {\n"
            + "        Hal hal = new Hal(brickConfiguration);\n"
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
            + MAIN_METHOD
            + "    public void run() {\n"
            + "        Hal hal = new Hal(brickConfiguration);\n"
            + "        for ( int i = 0; i < 10; i++ ) {\n"
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
            + MAIN_METHOD
            + "    public void run() {\n"
            + "        Hal hal = new Hal(brickConfiguration);\n"
            + "        if ( hal.isPressed(SensorPort.S1) ) {\n"
            + "            hal.setUltrasonicSensorMode(SensorPort.S4, UltrasonicSensorMode.DISTANCE);\n"
            + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "        } else if ( 0 == hal.getColorSensorValue(SensorPort.S3) ) {\n"
            + "            while ( true ) {\n"
            + "                hal.drawPicture(ShowPicture.EYESOPEN, 0, 0);\n\n"
            + "                hal.turnOnRegulatedMotor(ActorPort.B,30);"
            + "            }\n"
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

        String a = "" //
            + IMPORTS
            + MAIN_CLASS
            + BRICK_CONFIGURATION
            + MAIN_METHOD
            + "    public void run() {\n"
            + "        Hal hal = new Hal(brickConfiguration);\n"
            + "        if ( hal.isPressed(SensorPort.S1) ) {\n"
            + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "        } else {\n"
            + "            if ( hal.isPressed(SensorPort.S1) ) {\n"
            + "                hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
            + "            } else if ( 0 == hal.getUltraSonicSensorValue(SensorPort.S4) ) {\n"
            + "                hal.drawPicture(ShowPicture.FLOWERS, 15, 15);\n"
            + "            } else {\n"
            + "                hal.setUltrasonicSensorMode(SensorPort.S4, UltrasonicSensorMode.DISTANCE);\n"
            + "                while ( !hal.isPressedAndReleased(BrickKey.UP) ) {\n\n"
            + "                     hal.turnOnRegulatedMotor(ActorPort.B,30);"
            + "                }\n"
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
                + MAIN_METHOD
                + "    public void run() {\n"
                + "        Hal hal = new Hal(brickConfiguration);\n"
                + "        if ( 5 < hal.getRegulatedMotorSpeed(ActorPort.B) ) {\n\n\n"
                + "            hal.turnOnRegulatedMotor(ActorPort.B,30);\n"
                + "            hal.rotateRegulatedMotor(ActorPort.B,30,MotorMoveMode.ROTATIONS,1);\n"
                + "            hal.rotateDirectionRegulated(ActorPort.A, ActorPort.B, false, TurnDirection.RIGHT, 50);\n"
                + "        }\n"
                + "        if ( hal.getRegulatedMotorTachoValue(ActorPort.A) + hal.getInfraredSensorValue(SensorPort.S4) == hal.getUltraSonicSensorValue(SensorPort.S4) ) {\n"
                + "            hal.setInfraredSensorMode(SensorPort.S4, InfraredSensorMode.SEEK);\n"
                + "            hal.ledOff();\n"
                + "        } else {\n"
                + "            hal.resetGyroSensor(SensorPort.S2);\n"
                + "            while ( hal.isPressed(SensorPort.S1) ) {\n"
                + "                hal.drawPicture(ShowPicture.OLDGLASSES, 0, 0);\n"
                + "                hal.clearDisplay();\n"
                + "            }\n"
                + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
                + "        }\n"
                + SUFFIX
                + "    }\n"
                + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator4.xml");
    }

    @Ignore
    public void test5() throws Exception {

        String a =
            "" //
                + IMPORTS
                + MAIN_CLASS
                + BRICK_CONFIGURATION
                + MAIN_METHOD
                + "    public void run() {\n"
                + "        Hal hal = new Hal(brickConfiguration);\n"
                + "        if ( 5 < hal.getRegulatedMotorSpeed(ActorPort.B) ) {\n\n\n"
                + "            hal.rotateDirectionRegulated(ActorPort.A, ActorPort.B, TurnDirection.RIGHT, 50);\n"
                + "        }\n"
                + "        if ( hal.getRegulatedMotorTachoValue(ActorPort.A) + hal.getInfraredSensorValue(SensorPort.S4) == hal.getUltraSonicSensorValue(SensorPort.S4) ) {\n"
                + "            hal.setInfraredSensorMode(SensorPort.S4, InfraredSensorMode.SEEK);\n"
                + "            hal.ledOff();\n"
                + "        } else {\n"
                + "            hal.resetGyroSensor(SensorPort.S2);\n"
                + "            while ( hal.isPressed(SensorPort.S1) ) {\n"
                + "                hal.drawPicture(\"SMILEY1\", 0, 0);\n"
                + "                hal.clearDisplay();\n"
                + "            }\n"
                + "            hal.ledOn(BrickLedColor.GREEN, BlinkMode.ON);\n"
                + "        }\n"
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
            + MAIN_METHOD
            + "    public void run() {\n"
            + "        Hal hal = new Hal(brickConfiguration);\n"
            + "        hal.drawText(\"Hallo\", 0, 0);\n"
            + "        hal.playTone(300, 3000);\n"
            + SUFFIX
            + "    }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator6.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        // Assert.assertEquals(a, Helper.generateString(fileName, brickConfiguration));
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateString(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }
}
