package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;

public class JavaGenerateCodeTest {
    private static final String MAIN_CLASS = "" //
        + "public class Test {\n";
    private static final String IMPORTS = "" //
        + "package generated.main;\n\n"
        + "import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;\n"
        + "import de.fhg.iais.roberta.ast.syntax.HardwareComponent;\n"
        + "import de.fhg.iais.roberta.ast.syntax.action.ActorPort;\n"
        + "import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;\n"
        + "import de.fhg.iais.roberta.codegen.lejos.Hal;\n\n";
    private static final String BRICK_CONFIGURATION = "" //
        + "    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n"
        + "    .addActor(ActorPort.A, HardwareComponent.EV3MediumRegulatedMotor)\n"
        + "    .addActor(ActorPort.D, HardwareComponent.EV3LargeRegulatedMotor)\n"
        + "    .addSensor(SensorPort.S1, HardwareComponent.EV3TouchSensor)\n"
        + "    .addSensor(SensorPort.S2, HardwareComponent.EV3UltrasonicSensor)\n"
        + "    .build();\n\n";
    private static final String MAIN_METHOD = "" //
        + "    public static void main(String[] args) {\n"
        + "        new Test().run();\n"
        + "    }\n\n";
    private static BrickConfiguration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        BrickConfiguration.Builder builder = new BrickConfiguration.Builder();
        builder.addActor(ActorPort.A, HardwareComponent.EV3MediumRegulatedMotor).addActor(ActorPort.D, HardwareComponent.EV3LargeRegulatedMotor);
        builder.addSensor(SensorPort.S1, HardwareComponent.EV3TouchSensor).addSensor(SensorPort.S2, HardwareComponent.EV3UltrasonicSensor);
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
            + "    }\n"
            + "}\n";

        Assert.assertEquals(a, Helper.generateString("/syntax/code_generator/java_code_generator.xml", brickConfiguration));
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
            + "    }\n"
            + "}\n";

        Assert.assertEquals(a, Helper.generateString("/syntax/code_generator/java_code_generator1.xml", brickConfiguration));
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
            + "            hal.ledOn(BrickLedColor.GREEN, true);\n"
            + "        } else if ( PickColor.RED == hal.getColorSensorValue(SensorPort.S3) ) {\n"
            + "            while ( true ) {\n"
            + "                hal.drawPicture(\"SMILEY1\", 0, 0);\n\n"
            + "            }\n"
            + "        }\n"
            + "        hal.playFile(\"SOUNDFILE1\");\n"
            + "        hal.setVolume(50);\n"
            + "        for ( int i = 1; i <= 10; i += 1 ) {\n\n"
            + "        }\n"
            + "    }\n"
            + "}\n";

        Assert.assertEquals(a, Helper.generateString("/syntax/code_generator/java_code_generator2.xml", brickConfiguration));
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
            + "            hal.ledOn(BrickLedColor.GREEN, true);\n"
            + "        } else {\n"
            + "            if ( hal.isPressed(SensorPort.S1) ) {\n"
            + "                hal.ledOn(BrickLedColor.GREEN, true);\n"
            + "            } else if ( 0 == hal.getUltraSonicSensorValue(SensorPort.S4) ) {\n"
            + "                hal.drawPicture(\"SMILEY3\", 15, 15);\n"
            + "            } else {\n"
            + "                hal.setUltrasonicSensorMode(SensorPort.S4, UltrasonicSensorMode.DISTANCE);\n"
            + "                while ( !hal.isPressedAndReleased(BrickKey.UP) ) {\n\n"
            + "                }\n"
            + "            }\n"
            + "        }\n"
            + "    }\n"
            + "}\n";

        Assert.assertEquals(a, Helper.generateString("/syntax/code_generator/java_code_generator3.xml", brickConfiguration));
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
                + "            hal.ledOn(BrickLedColor.GREEN, true);\n"
                + "        }\n"
                + "    }\n"
                + "}\n";

        Assert.assertEquals(a, Helper.generateString("/syntax/code_generator/java_code_generator4.xml", brickConfiguration));
    }
}
