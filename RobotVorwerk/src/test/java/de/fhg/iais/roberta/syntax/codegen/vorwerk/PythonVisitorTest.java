package de.fhg.iais.roberta.syntax.codegen.vorwerk;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.syntax.VorwerkAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PythonVisitorTest extends VorwerkAstTest {

    private static final String IMPORTS =
        "" //
            + "#!/usr/bin/python\n\n"
            + "from __future__ import absolute_import\n"
            + "from roberta import Hal\n"
            + "import math\n\n"
            + "class BreakOutOfALoop(Exception): pass\n"
            + "class ContinueLoop(Exception): pass\n\n";

    private static final String GLOBALS = "hal = Hal()\n\n";

    private static final String MAIN_METHOD =
        "" //
            + "def main():\n"
            + "    try:\n"
            + "        run()\n"
            + "    except Exception as e:\n"
            + "        print('Fehler im Vorwerk')\n"
            + "        print(e.__class__.__name__)\n"
            + "        print(e)\n"
            + "\n"
            + "if __name__ == \"__main__\":\n"
            + "    main()";
    private static ConfigurationAst brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        VorwerkConfiguration.Builder builder = new VorwerkConfiguration.Builder();
        brickConfiguration = builder.build();
    }

    @Test
    public void visitTouchSensor_GetValuesFromAllPortsAndSlots_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\n___item = True\n"
                + "def run():\n"
                + "    global ___item\n"
                + "    ___item = hal.sample_touch_sensor('left', 'front')\n"
                + "    ___item = hal.sample_touch_sensor('left', 'side')\n"
                + "    ___item = hal.sample_touch_sensor('right', 'front')\n"
                + "    ___item = hal.sample_touch_sensor('right', 'side')\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensors/touch.xml", brickConfiguration, true);

    }

    @Test
    public void visitUltrasonicsSensor_GetValuesFromAllPortsAndSlots_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\n___item = 0\n"
                + "def run():\n"
                + "    global ___item\n"
                + "    ___item = hal.sample_ultrasonic_sensor('left_ultrasonic', 'left')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('left_ultrasonic', 'center')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('left_ultrasonic', 'right')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('center_ultrasonic', 'left')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('center_ultrasonic', 'center')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('center_ultrasonic', 'right')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('right_ultrasonic', 'left')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('right_ultrasonic', 'center')\n"
                + "    ___item = hal.sample_ultrasonic_sensor('right_ultrasonic', 'right')\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensors/ultrasonic.xml", brickConfiguration, true);

    }

    @Test
    public void visitAccelerometerSensor_GetValuesFromAllPortsAndSlots_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\n___item = 0\n"
                + "def run():\n"
                + "    global ___item\n"
                + "    ___item = hal.sample_accelerometer_sensor('x')\n"
                + "    ___item = hal.sample_accelerometer_sensor('y')\n"
                + "    ___item = hal.sample_accelerometer_sensor('z')\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensors/accelerometer.xml", brickConfiguration, true);

    }

    @Test
    public void visitDropOffSensor_GetValuesFromAllPortsAndSlots_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\n___item = 0\n"
                + "def run():\n"
                + "    global ___item\n"
                + "    ___item = hal.sample_dropoff_sensor('left')\n"
                + "    ___item = hal.sample_dropoff_sensor('right')\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensors/drop_off.xml", brickConfiguration, true);

    }

    @Test
    public void visitWallSensor_GetValuesFromAllPortsAndSlots_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\n___item = 0\n"
                + "def run():\n"
                + "    global ___item\n"
                + "    ___item = hal.sample_wall_sensor()\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensors/wall.xml", brickConfiguration, true);

    }

    @Test
    public void visitDriveForward_GetValuesFromAllPortsAndSlots_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.drive_distance('foreward', 30, 20)\n"
                + "    hal.drive_distance('backward', 30, 20)\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/drive.xml", brickConfiguration, true);

    }

    @Test
    public void visitMotorStop_GetValuesFromAllPortsAndSlots_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.stop_motors()\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/stop.xml", brickConfiguration, true);

    }

    @Test
    public void visitPlayFile_PlaySounds1to4_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.play_sound(0)\n"
                + "    hal.play_sound(1)\n"
                + "    hal.play_sound(2)\n"
                + "    hal.play_sound(3)\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/play_sound.xml", brickConfiguration, true);

    }

    @Test
    public void visitBrushOn_TurnsOnTheBrushSpeed50_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.brush_on(50)\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/brush_on.xml", brickConfiguration, true);

    }

    @Test
    public void visitBrushOff_TurnsOffTheBrush_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.brush_off()\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/brush_off.xml", brickConfiguration, true);

    }

    @Test
    public void visitVacuumOn_TurnsOnTheVacuumSpeed60_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.vacuum_on(60)\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/vacuum_on.xml", brickConfiguration, true);

    }

    @Test
    public void visitVacuumOff_TurnsOffTheVacuum_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.vacuum_off()\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/vacuum_off.xml", brickConfiguration, true);

    }

    @Test
    public void visitMotorOn_LeftRightDistance20Power30_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.left_motor_on(30, 20)\n"
                + "    hal.right_motor_on(30, 20)\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/motor_on.xml", brickConfiguration, true);
    }

    @Test
    public void visitMotorStop_LeftRightStop_ReturnsCorrectPythonProgram() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + "\ndef run():\n"
                + "    hal.left_motor_stop()\n"
                + "    hal.right_motor_stop()\n"
                + "\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/motor_stop.xml", brickConfiguration, true);

    }
}
