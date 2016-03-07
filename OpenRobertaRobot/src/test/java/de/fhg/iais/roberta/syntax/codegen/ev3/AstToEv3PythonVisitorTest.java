package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Actors;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.action.ev3.DriveDirection;
import de.fhg.iais.roberta.shared.action.ev3.MotorSide;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.testutil.Helper;

public class AstToEv3PythonVisitorTest {

    private static final String IMPORTS = "" //
        + "#!/usr/bin/python\n\n"
        + "from __future__ import absolute_import"
        + "from roberta.ev3 import Hal\n"
        + "from roberta.BlocklyMethods import BlocklyMethods\n"
        + "from sets import Set\n"
        + "from ev3dev import ev3 as ev3dev\n"
        + "import math\n\n"
        + "TRUE = True\n";

    private static final String GLOBALS = "" //
        + "_brickConfiguration = {\n"
        + "    'wheel-diameter': 5.6,\n"
        + "    'track-width': 17.0,\n"
        + "    'actors': {\n"
        + "        'A':Hal.makeMediumMotor(ev3dev.OUTPUT_A, 'on', 'foreward', 'left'),\n"
        + "        'B':Hal.makeLargeMotor(ev3dev.OUTPUT_B,'on','foreward','right'),\n"
        + "    },\n"
        + "    'sensors':{\n"
        + "        '1':Hal.makeTouchSensor(ev3dev.INPUT_1),\n"
        + "        '2':Hal.makeUltrasonicSensor(ev3dev.INPUT_2),\n"
        + "    },\n"
        + "}\n"
        + "_usedSensors = Set([])\n"
        + "hal = Hal(_brickConfiguration, _usedSensors)\n";

    private static final String MAIN_METHOD = "" //
        + "def main():\n"
        + "    try:\n"
        + "        run()\n"
        + "    except Exception as e:\n"
        + "        hal.drawText('Fehler im EV3', 0, 0)\n"
        + "        hal.drawText(e.__class__.__name, 0, 1)\n"
        + "        if e.message:\n"
        + "            hal.drawText(e.message, 0, 2)\n"
        + "        hal.drawText('Press any key', 0, 4)\n"
        + "        while not hal.isKeyPressed('any'): hal.waitFor(500)\n"
        + "        raise\n\n"
        + "if __name__ == \"__main__\":\n"
        + "    main()\n";
    private static Ev3Configuration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        Ev3Configuration.Builder builder = new Ev3Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new EV3Actor(EV3Actors.EV3_MEDIUM_MOTOR, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new EV3Actor(EV3Actors.EV3_LARGE_MOTOR, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new EV3Sensor(EV3Sensors.EV3_TOUCH_SENSOR)).addSensor(SensorPort.S2, new EV3Sensor(EV3Sensors.EV3_ULTRASONIC_SENSOR));
        brickConfiguration = builder.build();
    }

    @Test
    public void testSingleStatement() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    hal.drawText(\"Hallo\", 0, 3)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator.xml");
    }

    @Test
    public void testRangeLoop() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    for k0 in xrange(0,10,1):\n"
            + "        hal.drawText(\"Hallo\", 0, 3)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator1.xml");
    }

    @Test
    public void test() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    if hal.isPressed('1'):\n"
            + "        hal.ledOn('green','on')\n"
            + "    elif 'red'==hal.getColorSensorColour('3'):\n"
            + "        if TRUE:\n"
            + "            while True:\n"
            + "                hal.drawPicture('eyesopen',0,0)\n"
            + "                hal.turnOnRegulatedMotor('B',30)\n"
            + "    hal.playFile(1)\n"
            + "    hal.setVolume(50)\n"
            + "    for i in xrange(1,10,1):\n"
            + "        hal.rotateRegulatedMotor('B',30,'rotations',1)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator2.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        String b = Helper.generatePython(fileName, brickConfiguration);
        Assert.assertEquals(a.replaceAll("\\s+", ""), b.replaceAll("\\s+", ""));
    }
}
