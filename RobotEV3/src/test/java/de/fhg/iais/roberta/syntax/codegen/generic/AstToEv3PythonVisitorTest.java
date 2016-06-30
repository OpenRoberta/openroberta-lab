package de.fhg.iais.roberta.syntax.codegen.generic;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.shared.action.ActorPort;
import de.fhg.iais.roberta.shared.action.DriveDirection;
import de.fhg.iais.roberta.shared.action.MotorSide;
import de.fhg.iais.roberta.shared.sensor.SensorPort;
import de.fhg.iais.roberta.testutil.ev3.Helper;

public class AstToEv3PythonVisitorTest {

    private static final String IMPORTS = "" //
        + "#!/usr/bin/python\n\n"
        + "from __future__ import absolute_import\n"
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
        + "        'B':Hal.makeLargeMotor(ev3dev.OUTPUT_B, 'on', 'foreward', 'right'),\n"
        + "    },\n"
        + "    'sensors': {\n"
        + "        '1':Hal.makeTouchSensor(ev3dev.INPUT_1),\n"
        + "        '2':Hal.makeUltrasonicSensor(ev3dev.INPUT_2),\n"
        + "    },\n"
        + "}\n"
        + "_usedSensors = Set([])\n"
        + "hal = Hal(_brickConfiguration, _usedSensors)\n\n";

    private static final String MAIN_METHOD = "" //
        + "def main():\n"
        + "    try:\n"
        + "        run()\n"
        + "    except Exception as e:\n"
        + "        hal.drawText('Fehler im EV3', 0, 0)\n"
        + "        hal.drawText(e.__class__.__name__, 0, 1)\n"
        + "        if e.message:\n"
        + "            hal.drawText(e.message, 0, 2)\n"
        + "        hal.drawText('Press any key', 0, 4)\n"
        + "        while not hal.isKeyPressed('any'): hal.waitFor(500)\n"
        + "        raise\n\n"
        + "if __name__ == \"__main__\":\n"
        + "    main()";
    private static Configuration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17).setWheelDiameter(5.6);
        builder.addActor(ActorPort.A, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT)).addActor(
            ActorPort.B,
            new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT));
        builder.addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH)).addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC));
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
            + "    for k0 in xrange(0, 10, 1):\n"
            + "        hal.drawText(\"Hallo\", 0, 3)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator1.xml");
    }

    @Test
    public void testCondition1() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    if hal.isPressed('1'):\n"
            + "        hal.ledOn('green', 'on')\n"
            + "    elif 'red' == hal.getColorSensorColour('3'):\n"
            + "        if TRUE:\n"
            + "            while True:\n"
            + "                hal.drawPicture('eyesopen', 0, 0)\n"
            + "                hal.turnOnRegulatedMotor('B', 30)\n"
            + "    hal.playFile(1)\n"
            + "    hal.setVolume(50)\n"
            + "    for i in xrange(1, 10, 1):\n"
            + "        hal.rotateRegulatedMotor('B', 30, 'rotations', 1)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator2.xml");
    }

    @Test
    public void testCondition2() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    if hal.isPressed('1'):\n"
            + "        hal.ledOn('green', 'on')\n"
            + "    else:\n"
            + "        if hal.isPressed('1'):\n"
            + "            hal.ledOn('green', 'on')\n"
            + "        elif 0 == hal.getUltraSonicSensorDistance('4'):\n"
            + "            hal.drawPicture('flowers', 15, 15)\n"
            + "        else:\n"
            + "            if TRUE:\n"
            + "                while not hal.isKeyPressed('up'):\n"
            + "                    hal.turnOnRegulatedMotor('B', 30)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator3.xml");
    }

    @Test
    public void testCondition3() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    if 5 < hal.getRegulatedMotorSpeed('B'):\n"
            + "        hal.turnOnRegulatedMotor('B', 30)\n"
            + "        hal.rotateRegulatedMotor('B', 30, 'rotations', 1)\n"
            + "        hal.rotateDirectionRegulated('A', 'B', False, 'right', 50)\n"
            + "    if ( hal.getMotorTachoValue('A', 'rotation') + hal.getInfraredSensorDistance('4') ) == hal.getUltraSonicSensorDistance('4'):\n"
            + "        hal.ledOff()\n"
            + "    else:\n"
            + "        hal.resetGyroSensor('2')\n"
            + "        if TRUE:\n"
            + "            while hal.isPressed('1'):\n"
            + "                hal.drawPicture('oldglasses', 0, 0)\n"
            + "                hal.clearDisplay()\n"
            + "        hal.ledOn('green', 'on')\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator4.xml");
    }

    @Test
    public void testMultipleStatements() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    hal.turnOnRegulatedMotor('B', 0)\n"
            + "    hal.rotateRegulatedMotor('B', 30, 'rotations', 0)\n"
            + "    hal.rotateDirectionRegulated('A', 'B', False, 'right', 0)\n"
            + "    hal.setVolume(50)\n"
            + "    hal.playTone(0, 0)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator5.xml");
    }

    // Skip "{6,7}.xml" since they only test various different statements

    @Test
    public void testVariables() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "item = 10\n"
            + "item2 = \"TTTT\"\n"
            + "item3 = True\n"
            + "def run():\n"
            + "    hal.drawText(str(item), 0, 0)\n"
            + "    hal.drawText(str(item2), 0, 0)\n"
            + "    hal.drawText(str(item3), 0, 0)\n"
            + "    item3 = False\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator8.xml");
    }

    @Test
    public void testUnusedVariable() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "variablenName = 0\n"
            + "def run():\n"
            + "    hal.regulatedDrive('A', 'B', False, 'foreward', 50)\n"
            + "    hal.drawPicture('oldglasses', 0, 0)\n"
            + "    \n\n" // FIXME: where is this whitespace coming from?
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator9.xml");
    }

    // Skip "{6,7}.xml" since it only tests color sensor modes

    @Test
    public void testShadow() throws Exception {

        String a = "" //
            + IMPORTS
            + GLOBALS
            + "item = 0\n"
            + "item2 = \"cc\"\n"
            + "def run():\n"
            + "\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator11.xml");
    }

    @Test
    public void testExpr1() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "8 + ( -3 + 5 )\n"
            + "88 - ( 8 + ( -3 + 5 ) )\n"
            + "( 88 - ( 8 + ( -3 + 5 ) ) ) - ( 88 - ( 8 + ( -3 + 5 ) ) )\n"
            + "2 * ( 2 - 2 )\n"
            + "2 - ( 2 * 2 )\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/expr/expr1.xml");
    }

    @Test
    public void testLogicExpr() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "False == True\n"
            + "True != False\n"
            + "False == False\n"
            + "( 5 <= 7 ) == ( 8 > 9 )\n"
            + "( 5 != 7 ) >= ( 8 == 9 )\n"
            + "( 5 + 7 ) >= ( ( 8 + 4 ) / float(( 9 + 3 )) )\n"
            + "( ( 5 + 7 ) == ( 5 + 7 ) ) >= ( ( 8 + 4 ) / float(( 9 + 3 )) )\n"
            + "( ( 5 + 7 ) == ( 5 + 7 ) ) >= ( ( ( 5 + 7 ) == ( 5 + 7 ) ) and ( ( 5 + 7 ) <= ( 5 + 7 ) ) )\n"
            + "not (( 5 + 7 ) == ( 5 + 7 )) == True\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/expr/logic_expr.xml");
    }

    @Test
    public void testLogicNegate() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "not (( 0 != 0 ) and False)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/expr/logic_negate.xml");
    }

    @Test
    public void testLogicNull() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "None\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/expr/logic_null.xml");
    }

    @Test
    public void testLogicTernary() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "False if ( 0 == 0 ) else True\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/expr/logic_ternary.xml");
    }

    @Test
    public void testFunctionsTextConcat() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "BlocklyMethods.textJoin(0, 0)\n"
            + "BlocklyMethods.textJoin(0, \"16561\")\n"
            + "BlocklyMethods.textJoin(0, BlocklyMethods.createListWith(\"16561\", \"16561\"))\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/functions/text_concat.xml");
    }

    // TODO: add tests for files from "/syntax/{lists,math}/*.xml"

    @Test
    public void testMethodIfReturn1() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    test(True)\n"
            + "    \n"
            + "def test(x):\n"
            + "    if x: return None\n"
            + "    hal.ledOn('green', 'on')\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_if_return_1.xml");
    }

    @Test
    public void testMethodIfReturn2() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "variablenName = BlocklyMethods.createListWith(\"a\", \"b\", \"c\")\n"
            + "def run():\n"
            + "    hal.drawText(str(test()), 0, 0)\n"
            + "    \n"
            + "def test():\n"
            + "    if True: return 'red'\n"
            + "    hal.drawText(str(variablenName), 0, 0)\n"
            + "    return 'none'\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_if_return_2.xml");
    }

    @Test
    public void testMethodReturn1() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "variablenName = BlocklyMethods.createListWith(\"a\", \"b\", \"c\")\n"
            + "def run():\n"
            + "    hal.drawText(str(test(0, variablenName)), 0, 0)\n"
            + "    \n"
            + "def test(x, x2):\n"
            + "    hal.drawText(str(x2), x, 0)\n"
            + "    return x\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_return_1.xml");
    }

    @Test
    public void testMethodReturn2() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "variablenName = BlocklyMethods.createListWith(\"a\", \"b\", \"c\")\n"
            + "def run():\n"
            + "    hal.drawText(str(test()), 0, 0)\n"
            + "    \n"
            + "def test():\n"
            + "    hal.drawText(str(variablenName), 0, 0)\n"
            + "    return 'none'\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_return_2.xml");
    }

    @Test
    public void testMethodReturn3() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    hal.drawText(str(macheEtwas(hal.getColorSensorColour('3'))), 0, 0)\n"
            + "    \n"
            + "def macheEtwas(x):\n"
            + "    if hal.isPressed('1'): return hal.getInfraredSensorDistance('4')\n"
            + "    hal.drawText(str(hal.getGyroSensorValue('2, 'angle'')), 0, 0)\n"
            + "    return hal.getUltraSonicSensorDistance('4')\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_return_3.xml");
    }

    @Test
    public void testMethodVoid1() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    hal.rotateRegulatedMotor('B', 30, 'rotations', 1)\n"
            + "    macheEtwas(10, 10)\n"
            + "    \n"
            + "def macheEtwas(x, x2):\n"
            + "    hal.drawPicture('oldglasses', x, x2)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_void_1.xml");
    }

    @Test
    public void testMethodVoid2() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "def run():\n"
            + "    test()\n"
            + "    \n"
            + "def test():\n"
            + "    hal.ledOn('green', 'on')\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_void_2.xml");
    }

    @Test
    public void testMethodVoid3() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "variablenName = 0\n"
            + "variablenName2 = True\n"
            + "def run():\n"
            + "    test1(0, 0)\n"
            + "    test2()\n"
            + "    \n"
            + "def test1(x, x2):\n"
            + "    hal.drawText(\"Hallo\", x, x2)\n"
            + "    \n"
            + "def test2():\n"
            + "    if variablenName2: return None\n"
            + "    hal.ledOn('green', 'on')\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_void_3.xml");
    }

    @Test
    public void testMethodVoid4() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "variablenName = hal.getColorSensorColour('3')\n"
            + "def run():\n"
            + "    macheEtwas(hal.getInfraredSensorDistance('4'))\n"
            + "    \n"
            + "def macheEtwas(x):\n"
            + "    hal.drawText(str(hal.getUltraSonicSensorDistance('4')), 0, 0)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/methods/method_void_4.xml");
    }

    @Test
    public void testStmtFlowControl() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "if TRUE:\n"
            + "    while 0 == 0:\n"
            + "        print(\"123\")\n"
            + "        print(\"123\")\n"
            + "        if TRUE:\n"
            + "            while not (0 == 0):\n"
            + "                print(\"123\")\n"
            + "                print(\"123\")\n"
            + "                break\n"
            + "        break\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    }

    @Test
    public void testStmtFor() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "for k0 in xrange(0, 10, 1):\n"
            + "    pass\n"
            + "for k1 in xrange(0, 10, 1):\n"
            + "    print(\"15\")\n"
            + "    print(\"15\")\n"
            + "for k2 in xrange(0, 10, 1):\n"
            + "    for k3 in xrange(0, 10, 1):\n"
            + "        print(\"15\")\n"
            + "        print(\"15\")\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/for_stmt.xml");
    }

    @Test
    public void testStmtForCount() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "for i in xrange(1, 10, 15):\n"
            + "    pass\n"
            + "for i in xrange(1, 10, 15):\n"
            + "    print(\"\")\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/forCount_stmt.xml");
    }

    @Test
    public void testStmtForEach() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "variablenName = BlocklyMethods.createListWith('none', 'red', 'blue')\n"
            + "def run():\n"
            + "    for variablenName2 in variablenName:\n"
            + "        hal.drawText(str(variablenName2), 0, 0)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    }

    @Test
    public void testStmtIf() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "if True:\n"
            + "    pass\n"
            + "if False:\n"
            + "    pass\n"
            + "if True:\n"
            + "    if False:\n"
            + "        pass\n"
            + "if False:\n"
            + "    item = 6 + 8\n"
            + "    item = 6 + 8\n"
            + "else:\n"
            + "    item = 3 * 9\n"
            + "if True:\n"
            + "    item = 6 + 8\n"
            + "    item = 6 + 8\n"
            + "if False:\n"
            + "    item = 6 + 8\n"
            + "    item = 6 + 8\n"
            + "    item = 3 * 9\n"
            + "elif True:\n"
            + "    item = 3 * 9\n"
            + "    item = 3 * 9\n"
            + "else:\n"
            + "    item = 3 * 9\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/if_stmt.xml");
    }

    @Test
    public void testStmtIf1() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "if ( ( 5 + 7 ) == ( 5 + 7 ) ) >= ( ( ( 5 + 7 ) == ( 5 + 7 ) ) and ( ( 5 + 7 ) <= ( 5 + 7 ) ) ):\n"
            + "    pass\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/if_stmt1.xml");
    }

    @Test
    public void testStmtIf2() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "if True:\n"
            + "    print(\"1\")\n"
            + "    print(\"8\")\n"
            + "elif False:\n"
            + "    print(\"2\")\n"
            + "else:\n"
            + "    print(\"3\")\n"
            + "if True:\n"
            + "    print(\" 1\")\n"
            + "else:\n"
            + "    print(\" else\")\n"
            + "    print(0)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/if_stmt2.xml");
    }

    @Test
    public void testStmtIf3() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "if True:\n"
            + "    if False:\n"
            + "        pass\n"
            + "if False:\n"
            + "    item = 6 + 8\n"
            + "    item = 6 + 8\n"
            + "else:\n"
            + "    item = 3 * 9\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/if_stmt3.xml");
    }

    @Test
    public void testStmtIf4() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "message = \"exit\"\n"
            + "def run():\n"
            + "    if message == \"exit\":\n"
            + "        hal.drawText(\"done\", 0, 0)\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/if_stmt4.xml");
    }

    @Test
    public void testStmtWhileUntil() throws Exception {
        String a = "" //
            + IMPORTS
            + GLOBALS
            + "if TRUE:\n"
            + "    while True:\n"
            + "        pass\n"
            + "if TRUE:\n"
            + "    while not (0 == 0):\n"
            + "        pass\n"
            + "if TRUE:\n"
            + "    while not True:\n"
            + "        pass\n"
            + "if TRUE:\n"
            + "    while not (15 == 20):\n"
            + "        variablenName += 1\n"
            + "if TRUE:\n"
            + "    while not True:\n"
            + "        if TRUE:\n"
            + "            while not (15 == 20):\n"
            + "                variablenName += 1\n\n"
            + MAIN_METHOD;

        assertCodeIsOk(a, "/syntax/stmt/whileUntil_stmt.xml");
    }

    // TODO: add tests for files from "/syntax/text/*.xml"

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        String b = Helper.generatePython(fileName, brickConfiguration);
        Assert.assertEquals(a, b);
        //Assert.assertEquals(a.replaceAll("\\s+", ""), b.replaceAll("\\s+", ""));
    }
}
