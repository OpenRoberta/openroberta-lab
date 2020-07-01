package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.MicrobitAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PythonVisitorTest extends MicrobitAstTest {

    private static final String IMPORTS =
        "" //
            + "import microbit\n"
            + "import random\n"
            + "import math\n\n"
            + "class BreakOutOfALoop(Exception): pass\n"
            + "class ContinueLoop(Exception): pass\n\n"
            + "timer1 = microbit.running_time()\n";

    @Test
    public void visitMainTask_ByDefault_ReturnsEmptyMicroPythonScript() throws Exception {
        String expectedResult =
            "import microbit\n"
                + "import random\n"
                + "import math\n"
                + "\n"
                + "class BreakOutOfALoop(Exception): pass\n"
                + "class ContinueLoop(Exception): pass\n"
                + "\n"
                + "timer1 = microbit.running_time()\n"
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/task/main_task_no_variables_empty.xml",
                configuration,
                true);
    }

    @Test
    public void visitDisplayText_ShowHelloScript_ReturnsMicroPythonScriptWithShowTextCall() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\ndef run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(\"Hallo\")\n"
                + "    microbit.display.show(\"H\")\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/display_text_show_hello.xml",
                configuration,
                true);
    }

    @Test
    public void visitPredefinedImage_ScriptWithToImageVariables_ReturnsMicroPythonScriptWithTwoImageVariables() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "___Element = microbit.Image.HEART\n"
                + "___Element2 = microbit.Image.FABULOUS\n"
                + "def run():\n"
                + "    global timer1, ___Element, ___Element2\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/expr/image_get_image_defined_as_global_variables.xml",
                configuration,
                true);
    }

    @Test
    public void visitDisplayImageAction_ScriptWithDisplayImageAndAnimation_ReturnsMicroPythonScriptWithDisplayImageAndAnimation() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.show(microbit.Image.HEART)\n"
                + "    microbit.display.show([microbit.Image.HEART_SMALL, microbit.Image.ASLEEP])\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/display_image_show_imag_and_animation.xml",
                configuration,
                true);
    }

    @Test
    public void visitDisplayImageAction_ScriptWithMissinImageToDisplay_ReturnsMicroPythonScriptWithMissingImageToDisplay() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.show(\"\")\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/display_image_missing_image_name.xml",
                configuration,
                true);
    }

    @Test
    public void visitClearDisplayAction_ScriptWithClearDisplay_ReturnsMicroPythonScriptClearDisplay() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.clear()\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/action/display_clear.xml", configuration, true);
    }

    @Test
    public void visitImageShiftFunction_ScriptWithShiftTwoImages_ReturnsMicroPythonScriptShiftTwoImages() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.show(microbit.Image.SILLY.shift_up(1))\n"
                + "    microbit.display.show(microbit.Image.SILLY.shift_down(2))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/function/image_shift_up_down.xml", configuration, true);
    }

    @Test
    public void testVisitPinWriteValueSensor() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.pin0.write_digital(1);\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/action/write_value_to_pin_microbit.xml",
                configuration,
                true);
    }

    @Test
    public void visitImageShiftFunction_ScriptWithMissingPositionImage_ReturnsMicroPythonScriptMissingPositionImage() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.show(microbit.Image.SILLY.shift_up(0))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/image_shift_missing_image_and_position.xml",
                configuration,
                true);
    }

    @Test
    public void visitImageInvertFunction_ScriptWithInvertHeartImage_ReturnsMicroPythonScriptInvertHeartImage() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.show(microbit.Image.HEART.invert())\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/image_invert_heart_image.xml",
                configuration,
                true);
    }

    @Test
    public void visitImageInvertFunction_ScriptWithMissingImage_ReturnsMicroPythonScriptInvertDefaultImage() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.show(microbit.Image.SILLY.invert())\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/function/image_invert_missing_image.xml",
                configuration,
                true);
    }

    @Test
    public void visitBrickSensor_ScriptChecksKeyAStatus_ReturnsMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(microbit.button_a.is_pressed()))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/sensor/check_if_key_A_is_pressed.xml",
                configuration,
                true);
    }

    @Test
    public void visitCompassSensor_ScriptDisplayCompassHeading_ReturnsMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(microbit.compass.heading()))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/sensor/get_compass_orientation_value.xml",
                configuration,
                true);
    }

    @Test
    public void visitLightSensor_ScriptDisplayCompassHeading_ReturnsMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(round(microbit.display.read_light_level() / 2.55)))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/get_light_level.xml", configuration, true);
    }

    @Test
    public void visitImage_ScriptCreatingImage_ReturnsMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.show(microbit.Image('99000:00009:03000:00090:02000'))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/expr/image_create.xml", configuration, true);
    }

    @Test
    public void visitGestureSensor_ScriptGetCurrentGestureAndDisplay_ReturnsCoorectMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(\"face down\" == microbit.accelerometer.current_gesture()))\n"
                + "    microbit.display.scroll(str(\"left\" == microbit.accelerometer.current_gesture()))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/check_gesture.xml", configuration, true);
    }

    @Test
    public void visitTemperatureSensor_ScriptGetCurrentTemperatureAndDisplay_ReturnsCorrectMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(microbit.temperature()))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/get_temperature.xml", configuration, true);
    }

    @Test
    public void visitPinTouchSensor_ScriptDisplayPin0andPin2areTouched_ReturnsCorrectMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(microbit.pin0.is_touched()))\n"
                + "    microbit.display.scroll(str(microbit.pin2.is_touched()))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/pin_is_touched.xml", configuration, true);
    }

    @Test
    public void visitPinValueSensor_ScriptDisplayAnalogReadPin0andDigitalReadPin2_ReturnsCorrectMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(microbit.pin1.read_analog()))\n"
                + "    microbit.display.scroll(str(microbit.pin0.read_digital()))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                expectedResult,
                "/sensor/read_value_from_pin_microbit.xml",
                configuration,
                true);
    }

    @Test
    public void visitAccelerationSensor_DisplayTheAccelerationInEachDirection_ReturnsCorrectMicroPythonScript() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll(str(microbit.accelerometer.get_x()))\n"
                + "    microbit.display.scroll(str(microbit.accelerometer.get_y()))\n"
                + "    microbit.display.scroll(str(microbit.accelerometer.get_z()))\n"
                + "    microbit.display.scroll(str(math.sqrt(microbit.accelerometer.get_x()**2 + microbit.accelerometer.get_y()**2 + microbit.accelerometer.get_z()**2)))\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensor/acceleration_sensor.xml", configuration, true);
    }

    @Test
    public void check_noLoops_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    if 20 == 30:\n"
                + "        while True:\n"
                + "            if microbit.button_a.is_pressed() == True:\n"
                + "                break\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/stmts/microbit/no_loops.xml", configuration, true);
    }

    @Test
    public void check_nestedLoopsNoBreakorContinue_returnsNoLabeledLoops() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    while True:\n"
                + "        if 30 == 20:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    break\n"
                + "        for ___i in range(int(1), int(10), int(1)):\n"
                + "            pass\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/stmts/microbit/nested_loops.xml", configuration, true);
    }

    @Test
    public void check_loopWithBreakAndContinueInWait_returnsOneLabeledLoop() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    while True:\n"
                + "        try:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "        except BreakOutOfALoop:\n"
                + "            break\n"
                + "        except ContinueLoop:\n"
                + "            continue\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/microbit/loop_with_break_and_continue_inside_wait.xml",
                configuration,
                true);
    }

    @Test
    public void check_loopsWithBreakAndContinueFitstInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    while True:\n"
                + "        try:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "        except BreakOutOfALoop:\n"
                + "            break\n"
                + "        except ContinueLoop:\n"
                + "            continue\n"
                + "    for ___i in range(int(1), int(10), int(1)):\n"
                + "        if ___i < 10:\n"
                + "            continue\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/microbit/two_loop_with_break_and_continue_one_inside_wait_another_not.xml",
                configuration,
                true);
    }

    @Test
    public void check_twoNestedloopsFirstWithBreakAndContinueInWaitSecondNot_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    while True:\n"
                + "        try:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "            for ___i in range(int(1), int(10), int(1)):\n"
                + "                if ___i < 10:\n"
                + "                    continue\n"
                + "        except BreakOutOfALoop:\n"
                + "            break\n"
                + "        except ContinueLoop:\n"
                + "            continue\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/microbit/two_nested_loops_first_with_break_in_wait_second_not.xml",
                configuration,
                true);
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWait_returnsFirstLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    while True:\n"
                + "        try:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for ___i in range(int(1), int(10), int(1)):\n"
                + "                        if ___i < 10:\n"
                + "                            continue\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for ___j in range(int(1), int(10), int(1)):\n"
                + "                        if ___j < 10:\n"
                + "                            continue\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "        except BreakOutOfALoop:\n"
                + "            break\n"
                + "        except ContinueLoop:\n"
                + "            continue\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/microbit/loop_with_nested_two_loops_inside_wait.xml",
                configuration,
                true);
    }

    @Test
    public void check_loopWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstAndThirdLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\ndef run():\n"
                + "    global timer1\n"
                + "    while True:\n"
                + "        try:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for ___j in range(int(1), int(10), int(1)):\n"
                + "                        if ___j < 10:\n"
                + "                            continue\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for ___i in range(int(1), int(10), int(1)):\n"
                + "                        try:\n"
                + "                            while True:\n"
                + "                                if microbit.button_a.is_pressed() == True:\n"
                + "                                    raise ContinueLoop\n"
                + "                                    break\n"
                + "                                if microbit.button_a.is_pressed() == True:\n"
                + "                                    raise BreakOutOfALoop\n"
                + "                                    break\n"
                + "                        except BreakOutOfALoop:\n"
                + "                            break\n"
                + "                        except ContinueLoop:\n"
                + "                            continue\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "        except BreakOutOfALoop:\n"
                + "            break\n"
                + "        except ContinueLoop:\n"
                + "            continue\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/microbit/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml",
                configuration,
                true);
    }

    @Test
    public void check_threeLoopsWithNestedTwoLoopsInsideWaitSecondContainWait_returnsFirstThirdAndFourthLoopLabeled() throws Exception {
        String a =
            "" //
                + IMPORTS
                + "\n"
                + "def run():\n"
                + "    global timer1\n"
                + "    while True:\n"
                + "        try:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for ___j in range(int(1), int(10), int(1)):\n"
                + "                        if ___j < 10:\n"
                + "                            continue\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for ___i in range(int(1), int(10), int(1)):\n"
                + "                        try:\n"
                + "                            while True:\n"
                + "                                if microbit.button_a.is_pressed() == True:\n"
                + "                                    raise ContinueLoop\n"
                + "                                    break\n"
                + "                                if microbit.button_a.is_pressed() == True:\n"
                + "                                    raise BreakOutOfALoop\n"
                + "                                    break\n"
                + "                        except BreakOutOfALoop:\n"
                + "                            break\n"
                + "                        except ContinueLoop:\n"
                + "                            continue\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "        except BreakOutOfALoop:\n"
                + "            break\n"
                + "        except ContinueLoop:\n"
                + "            continue\n"
                + "    while True:\n"
                + "        if 10 < 10:\n"
                + "            continue\n"
                + "    while True:\n"
                + "        try:\n"
                + "            while True:\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "        except BreakOutOfALoop:\n"
                + "            break\n"
                + "        except ContinueLoop:\n"
                + "            continue\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(
                testFactory,
                a,
                "/stmts/microbit/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml",
                configuration,
                true);
    }
}
