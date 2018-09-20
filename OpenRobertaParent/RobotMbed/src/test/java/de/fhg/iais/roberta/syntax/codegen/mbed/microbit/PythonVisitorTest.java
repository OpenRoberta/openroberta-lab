package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.mbed.MicrobitConfiguration;
import de.fhg.iais.roberta.util.test.mbed.HelperMicrobitForXmlTest;

public class PythonVisitorTest {
    private final HelperMicrobitForXmlTest h = new HelperMicrobitForXmlTest();

    private static final String IMPORTS =
        "" //
            + "import microbit\n"
            + "import random\n"
            + "import math\n\n"
            + "_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2\n"
            + "class BreakOutOfALoop(Exception): pass\n"
            + "class ContinueLoop(Exception): pass\n\n"
            + "timer1 = microbit.running_time()\n";

    private static Configuration brickConfiguration;

    @BeforeClass
    public static void setupConfigurationForAllTests() {
        @SuppressWarnings("rawtypes")
        Configuration.Builder configuration = new MicrobitConfiguration.Builder();
        brickConfiguration = configuration.build();
    }

    @Test
    public void visitMainTask_ByDefault_ReturnsEmptyMicroPythonScript() throws Exception {
        String expectedResult =
            "import microbit\n"
                + "import random\n"
                + "import math\n"
                + "\n"
                + "_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2\n"
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

        assertCodeIsOk(expectedResult, "/task/main_task_no_variables_empty.xml");
    }

    @Test
    public void visitDisplayText_ShowHelloScript_ReturnsMicroPythonScriptWithShowTextCall() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\ndef run():\n"
                + "    global timer1\n"
                + "    microbit.display.scroll('Hallo')\n"
                + "    microbit.display.show('H')\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        assertCodeIsOk(expectedResult, "/action/display_text_show_hello.xml");
    }

    @Test
    public void visitPredefinedImage_ScriptWithToImageVariables_ReturnsMicroPythonScriptWithTwoImageVariables() throws Exception {
        String expectedResult =
            "" //
                + IMPORTS
                + "\n"
                + "Element = microbit.Image.HEART\n"
                + "Element2 = microbit.Image.FABULOUS\n"
                + "def run():\n"
                + "    global timer1, Element, Element2\n"
                + "\n"
                + "def main():\n"
                + "    try:\n"
                + "        run()\n"
                + "    except Exception as e:\n"
                + "        raise\n"
                + "\n"
                + "if __name__ == \"__main__\":\n"
                + "    main()";

        assertCodeIsOk(expectedResult, "/expr/image_get_image_defined_as_global_variables.xml");
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

        assertCodeIsOk(expectedResult, "/action/display_image_show_imag_and_animation.xml");
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

        assertCodeIsOk(expectedResult, "/action/display_image_missing_image_name.xml");
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

        assertCodeIsOk(expectedResult, "/action/display_clear.xml");
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

        assertCodeIsOk(expectedResult, "/function/image_shift_up_down.xml");
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
        assertCodeIsOk(expectedResult, "/action/write_value_to_pin_microbit.xml");
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

        assertCodeIsOk(expectedResult, "/function/image_shift_missing_image_and_position.xml");
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

        assertCodeIsOk(expectedResult, "/function/image_invert_heart_image.xml");
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

        assertCodeIsOk(expectedResult, "/function/image_invert_missing_image.xml");
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

        assertCodeIsOk(expectedResult, "/sensor/check_if_key_A_is_pressed.xml");
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

        assertCodeIsOk(expectedResult, "/sensor/get_compass_orientation_value.xml");
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

        assertCodeIsOk(expectedResult, "/expr/image_create.xml");
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

        assertCodeIsOk(expectedResult, "/sensor/check_gesture.xml");
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

        assertCodeIsOk(expectedResult, "/sensor/get_temperature.xml");
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

        assertCodeIsOk(expectedResult, "/sensor/pin_is_touched.xml");
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

        assertCodeIsOk(expectedResult, "/sensor/read_value_from_pin.xml");
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

        assertCodeIsOk(expectedResult, "/sensor/acceleration_sensor.xml");
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

        assertCodeIsOk(a, "/stmts/no_loops.xml");
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
                + "        for i in range(int(1), int(10), int(1)):\n"
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

        assertCodeIsOk(a, "/stmts/nested_loops.xml");
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

        assertCodeIsOk(a, "/stmts/loop_with_break_and_continue_inside_wait.xml");
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
                + "    for i in range(int(1), int(10), int(1)):\n"
                + "        if i < 10:\n"
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

        assertCodeIsOk(a, "/stmts/two_loop_with_break_and_continue_one_inside_wait_another_not.xml");
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
                + "            for i in range(int(1), int(10), int(1)):\n"
                + "                if i < 10:\n"
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

        assertCodeIsOk(a, "/stmts/two_nested_loops_first_with_break_in_wait_second_not.xml");
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
                + "                    for i in range(int(1), int(10), int(1)):\n"
                + "                        if i < 10:\n"
                + "                            continue\n"
                + "                    raise BreakOutOfALoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for j in range(int(1), int(10), int(1)):\n"
                + "                        if j < 10:\n"
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

        assertCodeIsOk(a, "/stmts/loop_with_nested_two_loops_inside_wait.xml");
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
                + "                    for j in range(int(1), int(10), int(1)):\n"
                + "                        if j < 10:\n"
                + "                            continue\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for i in range(int(1), int(10), int(1)):\n"
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

        assertCodeIsOk(a, "/stmts/loop_with_nested_two_loops_inside_wait_second_contain_wait.xml");
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
                + "                    for j in range(int(1), int(10), int(1)):\n"
                + "                        if j < 10:\n"
                + "                            continue\n"
                + "                    raise ContinueLoop\n"
                + "                    break\n"
                + "                if microbit.button_a.is_pressed() == True:\n"
                + "                    for i in range(int(1), int(10), int(1)):\n"
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
        assertCodeIsOk(a, "/stmts/three_loops_with_nested_two_loops_inside_wait_second_contain_wait.xml");
    }

    //    @Test
    //    public void testSingleStatement() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "def run():\n"
    //            + "    hal.drawText(\"Hallo\", 0, 3)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator.xml");
    //    }
    //
    //    @Test
    //    public void testRangeLoop() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "def run():\n"
    //            + "    for k0 in xrange(0, 10, 1):\n"
    //            + "        hal.drawText(\"Hallo\", 0, 3)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator1.xml");
    //    }
    //
    //    @Test
    //    public void testCondition1() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + "def run():\n"
    //            + "    if hal.isPressed('1'):\n"
    //            + "        hal.ledOn('green', 'on')\n"
    //            + "    elif 'red' == hal.getColorSensorColour('3'):\n"
    //            + "        while True:\n"
    //            + "            hal.drawPicture('eyesopen', 0, 0)\n"
    //            + "            hal.turnOnRegulatedMotor('B', 30)\n"
    //            + "    hal.playFile(1)\n"
    //            + "    hal.setVolume(50)\n"
    //            + "    for i in xrange(1, 10, 1):\n"
    //            + "        hal.rotateRegulatedMotor('B', 30, 'rotations', 1)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator2.xml");
    //    }
    //
    //    @Test
    //    public void testCondition2() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //
    //            + "def run():\n"
    //            + "    if hal.isPressed('1'):\n"
    //            + "        hal.ledOn('green', 'on')\n"
    //            + "    else:\n"
    //            + "        if hal.isPressed('1'):\n"
    //            + "            hal.ledOn('green', 'on')\n"
    //            + "        elif 0 == hal.getUltraSonicSensorDistance('4'):\n"
    //            + "            hal.drawPicture('flowers', 15, 15)\n"
    //            + "        else:\n"
    //            + "            while not hal.isKeyPressed('up'):\n"
    //            + "                hal.turnOnRegulatedMotor('B', 30)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator3.xml");
    //    }
    //
    //    @Test
    //    public void testCondition3() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //
    //            + "def run():\n"
    //            + "    if 5 < hal.getRegulatedMotorSpeed('B'):\n"
    //            + "        hal.turnOnRegulatedMotor('B', 30)\n"
    //            + "        hal.rotateRegulatedMotor('B', 30, 'rotations', 1)\n"
    //            + "        hal.rotateDirectionRegulated('A', 'B', False, 'right', 50)\n"
    //            + "    if ( hal.getMotorTachoValue('A', 'rotation') + hal.getInfraredSensorDistance('4') ) == hal.getUltraSonicSensorDistance('4'):\n"
    //            + "        hal.ledOff()\n"
    //            + "    else:\n"
    //            + "        hal.resetGyroSensor('2')\n"
    //            + "        while hal.isPressed('1'):\n"
    //            + "            hal.drawPicture('oldglasses', 0, 0)\n"
    //            + "            hal.clearDisplay()\n"
    //            + "        hal.ledOn('green', 'on')\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator4.xml");
    //    }
    //
    //    @Test
    //    public void testMultipleStatements() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //
    //            + "def run():\n"
    //            + "    hal.turnOnRegulatedMotor('B', 0)\n"
    //            + "    hal.rotateRegulatedMotor('B', 30, 'rotations', 0)\n"
    //            + "    hal.rotateDirectionRegulated('A', 'B', False, 'right', 0)\n"
    //            + "    hal.setVolume(50)\n"
    //            + "    hal.playTone(0, 0)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator5.xml");
    //    }
    //
    //    // Skip "{6,7}.xml" since they only test various different statements
    //
    //    @Test
    //    public void testVariables() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "item = 10\n"
    //            + "item2 = \"TTTT\"\n"
    //            + "item3 = True\n"
    //            + "def run():\n"
    //            + "    global item, item2, item3\n"
    //            + "    hal.drawText(str(item), 0, 0)\n"
    //            + "    hal.drawText(str(item2), 0, 0)\n"
    //            + "    hal.drawText(str(item3), 0, 0)\n"
    //            + "    item3 = False\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator8.xml");
    //    }
    //
    //    @Test
    //    public void testUnusedVariable() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + "variablenName = 0\n"
    //            + "def run():\n"
    //            + "    global variablenName\n"
    //            + "    hal.regulatedDrive('A', 'B', False, 'foreward', 50)\n"
    //            + "    hal.drawPicture('oldglasses', 0, 0)\n"
    //            + "    \n\n" // FIXME: where is this whitespace coming from?
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator9.xml");
    //    }
    //
    //    // Skip "{6,7}.xml" since it only tests color sensor modes
    //
    //    @Test
    //    public void testShadow() throws Exception {
    //
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "item = 0\n"
    //            + "item2 = \"cc\"\n"
    //            + "def run():\n"
    //            + "    global item, item2\n"
    //            + "\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/code_generator/java/java_code_generator11.xml");
    //    }
    //
    //    @Test
    //    public void testExpr1() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "8 + ( -3 + 5 )\n"
    //            + "88 - ( 8 + ( -3 + 5 ) )\n"
    //            + "( 88 - ( 8 + ( -3 + 5 ) ) ) - ( 88 - ( 8 + ( -3 + 5 ) ) )\n"
    //            + "2 * ( 2 - 2 )\n"
    //            + "2 - ( 2 * 2 )\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/expr/expr1.xml");
    //    }
    //
    //    @Test
    //    public void testLogicExpr() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "False == True\n"
    //            + "True != False\n"
    //            + "False == False\n"
    //            + "( 5 <= 7 ) == ( 8 > 9 )\n"
    //            + "( 5 != 7 ) >= ( 8 == 9 )\n"
    //            + "( 5 + 7 ) >= ( ( 8 + 4 ) / float(( 9 + 3 )) )\n"
    //            + "( ( 5 + 7 ) == ( 5 + 7 ) ) >= ( ( 8 + 4 ) / float(( 9 + 3 )) )\n"
    //            + "( ( 5 + 7 ) == ( 5 + 7 ) ) >= ( ( ( 5 + 7 ) == ( 5 + 7 ) ) and ( ( 5 + 7 ) <= ( 5 + 7 ) ) )\n"
    //            + "not (( 5 + 7 ) == ( 5 + 7 )) == True\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/expr/logic_expr.xml");
    //    }
    //
    //    @Test
    //    public void testLogicNegate() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "not (( 0 != 0 ) and False)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/expr/logic_negate.xml");
    //    }
    //
    //    @Test
    //    public void testLogicNull() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "None\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/expr/logic_null.xml");
    //    }
    //
    //    @Test
    //    public void testLogicTernary() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "False if ( 0 == 0 ) else True\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/expr/logic_ternary.xml");
    //    }
    //
    //    @Test
    //    public void testFunctionsTextConcat() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "BlocklyMethods.textJoin(0, 0)\n"
    //            + "BlocklyMethods.textJoin(0, \"16561\")\n"
    //            + "BlocklyMethods.textJoin(0, BlocklyMethods.createListWith(\"16561\", \"16561\"))\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/functions/text_concat.xml");
    //    }
    //
    //    // TODO: add tests for files from "/syntax/{lists,math}/*.xml"
    //
    //    @Test
    //    public void testMethodIfReturn1() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "def run():\n"
    //            + "    test(True)\n"
    //            + "    \n"
    //            + "def test(x):\n"
    //            + "    if x: return None\n"
    //            + "    hal.ledOn('green', 'on')\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_if_return_1.xml");
    //    }
    //
    //    @Test
    //    public void testMethodIfReturn2() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "variablenName = BlocklyMethods.createListWith(\"a\", \"b\", \"c\")\n"
    //            + "def run():\n"
    //            + "    global variablenName\n"
    //            + "    hal.drawText(str(test()), 0, 0)\n"
    //            + "    \n"
    //            + "def test():\n"
    //            + "    if True: return 'red'\n"
    //            + "    hal.drawText(str(variablenName), 0, 0)\n"
    //            + "    return 'none'\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_if_return_2.xml");
    //    }
    //
    //    @Test
    //    public void testMethodReturn1() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "variablenName = BlocklyMethods.createListWith(\"a\", \"b\", \"c\")\n"
    //            + "def run():\n"
    //            + "    global variablenName\n"
    //            + "    hal.drawText(str(test(0, variablenName)), 0, 0)\n"
    //            + "    \n"
    //            + "def test(x, x2):\n"
    //            + "    hal.drawText(str(x2), x, 0)\n"
    //            + "    return x\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_return_1.xml");
    //    }
    //
    //    @Test
    //    public void testMethodReturn2() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "variablenName = BlocklyMethods.createListWith(\"a\", \"b\", \"c\")\n"
    //            + "def run():\n"
    //            + "    global variablenName\n"
    //            + "    hal.drawText(str(test()), 0, 0)\n"
    //            + "    \n"
    //            + "def test():\n"
    //            + "    hal.drawText(str(variablenName), 0, 0)\n"
    //            + "    return 'none'\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_return_2.xml");
    //    }
    //
    //    @Test
    //    public void testMethodReturn3() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + "def run():\n"
    //            + "    hal.drawText(str(macheEtwas(hal.getColorSensorColour('3'))), 0, 0)\n"
    //            + "    \n"
    //            + "def macheEtwas(x):\n"
    //            + "    if hal.isPressed('1'): return hal.getInfraredSensorDistance('4')\n"
    //            + "    hal.drawText(str(hal.getGyroSensorValue('2', 'angle')), 0, 0)\n"
    //            + "    return hal.getUltraSonicSensorDistance('4')\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_return_3.xml");
    //    }
    //
    //    @Test
    //    public void testMethodVoid1() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + "def run():\n"
    //            + "    hal.rotateRegulatedMotor('B', 30, 'rotations', 1)\n"
    //            + "    macheEtwas(10, 10)\n"
    //            + "    \n"
    //            + "def macheEtwas(x, x2):\n"
    //            + "    hal.drawPicture('oldglasses', x, x2)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_void_1.xml");
    //    }
    //
    //    @Test
    //    public void testMethodVoid2() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "def run():\n"
    //            + "    test()\n"
    //            + "    \n"
    //            + "def test():\n"
    //            + "    hal.ledOn('green', 'on')\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_void_2.xml");
    //    }
    //
    //    @Test
    //    public void testMethodVoid3() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "variablenName = 0\n"
    //            + "variablenName2 = True\n"
    //            + "def run():\n"
    //            + "    global variablenName, variablenName2\n"
    //            + "    test1(0, 0)\n"
    //            + "    test2()\n"
    //            + "    \n"
    //            + "def test1(x, x2):\n"
    //            + "    hal.drawText(\"Hallo\", x, x2)\n"
    //            + "    \n"
    //            + "def test2():\n"
    //            + "    if variablenName2: return None\n"
    //            + "    hal.ledOn('green', 'on')\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_void_3.xml");
    //    }
    //
    //    @Test
    //    public void testMethodVoid4() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "variablenName = hal.getColorSensorColour('3')\n"
    //            + "def run():\n"
    //            + "    global variablenName\n"
    //            + "    macheEtwas(hal.getInfraredSensorDistance('4'))\n"
    //            + "    \n"
    //            + "def macheEtwas(x):\n"
    //            + "    hal.drawText(str(hal.getUltraSonicSensorDistance('4')), 0, 0)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/methods/method_void_4.xml");
    //    }
    //
    //    @Test
    //    public void testStmtFlowControl() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "while 0 == 0:\n"
    //            + "    print(\"123\")\n"
    //            + "    print(\"123\")\n"
    //            + "    while not (0 == 0):\n"
    //            + "        print(\"123\")\n"
    //            + "        print(\"123\")\n"
    //            + "        break\n"
    //            + "    break\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/flowControl_stmt.xml");
    //    }
    //
    //    @Test
    //    public void testStmtFor() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "for k0 in xrange(0, 10, 1):\n"
    //            + "    pass\n"
    //            + "for k1 in xrange(0, 10, 1):\n"
    //            + "    print(\"15\")\n"
    //            + "    print(\"15\")\n"
    //            + "for k2 in xrange(0, 10, 1):\n"
    //            + "    for k3 in xrange(0, 10, 1):\n"
    //            + "        print(\"15\")\n"
    //            + "        print(\"15\")\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/for_stmt.xml");
    //    }
    //
    //    @Test
    //    public void testStmtForCount() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "for i in xrange(1, 10, 15):\n"
    //            + "    pass\n"
    //            + "for i in xrange(1, 10, 15):\n"
    //            + "    print(\"\")\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/forCount_stmt.xml");
    //    }
    //
    //    @Test
    //    public void testStmtForEach() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "variablenName = BlocklyMethods.createListWith('none', 'red', 'blue')\n"
    //            + "def run():\n"
    //            + "    global variablenName\n"
    //            + "    for variablenName2 in variablenName:\n"
    //            + "        hal.drawText(str(variablenName2), 0, 0)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/forEach_stmt.xml");
    //    }
    //
    //    @Test
    //    public void testStmtIf() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "if True:\n"
    //            + "    pass\n"
    //            + "if False:\n"
    //            + "    pass\n"
    //            + "if True:\n"
    //            + "    if False:\n"
    //            + "        pass\n"
    //            + "if False:\n"
    //            + "    item = 6 + 8\n"
    //            + "    item = 6 + 8\n"
    //            + "else:\n"
    //            + "    item = 3 * 9\n"
    //            + "if True:\n"
    //            + "    item = 6 + 8\n"
    //            + "    item = 6 + 8\n"
    //            + "if False:\n"
    //            + "    item = 6 + 8\n"
    //            + "    item = 6 + 8\n"
    //            + "    item = 3 * 9\n"
    //            + "elif True:\n"
    //            + "    item = 3 * 9\n"
    //            + "    item = 3 * 9\n"
    //            + "else:\n"
    //            + "    item = 3 * 9\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/if_stmt.xml");
    //    }
    //
    //    @Test
    //    public void testStmtIf1() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "if ( ( 5 + 7 ) == ( 5 + 7 ) ) >= ( ( ( 5 + 7 ) == ( 5 + 7 ) ) and ( ( 5 + 7 ) <= ( 5 + 7 ) ) ):\n"
    //            + "    pass\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/if_stmt1.xml");
    //    }
    //
    //    @Test
    //    public void testStmtIf2() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "if True:\n"
    //            + "    print(\"1\")\n"
    //            + "    print(\"8\")\n"
    //            + "elif False:\n"
    //            + "    print(\"2\")\n"
    //            + "else:\n"
    //            + "    print(\"3\")\n"
    //            + "if True:\n"
    //            + "    print(\" 1\")\n"
    //            + "else:\n"
    //            + "    print(\" else\")\n"
    //            + "    print(0)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/if_stmt2.xml");
    //    }
    //
    //    @Test
    //    public void testStmtIf3() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "if True:\n"
    //            + "    if False:\n"
    //            + "        pass\n"
    //            + "if False:\n"
    //            + "    item = 6 + 8\n"
    //            + "    item = 6 + 8\n"
    //            + "else:\n"
    //            + "    item = 3 * 9\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/if_stmt3.xml");
    //    }
    //
    //    @Test
    //    public void testStmtIf4() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "message = \"exit\"\n"
    //            + "def run():\n"
    //            + "    global message\n"
    //            + "    if message == \"exit\":\n"
    //            + "        hal.drawText(\"done\", 0, 0)\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/if_stmt4.xml");
    //    }
    //
    //    @Test
    //    public void testStmtWhileUntil() throws Exception {
    //        String a = "" //
    //            + IMPORTS
    //            + GLOBALS
    //            + "while True:\n"
    //            + "    pass\n"
    //            + "while not (0 == 0):\n"
    //            + "    pass\n"
    //            + "while not True:\n"
    //            + "    pass\n"
    //            + "while not (15 == 20):\n"
    //            + "    variablenName += 1\n"
    //            + "while not True:\n"
    //            + "    while not (15 == 20):\n"
    //            + "        variablenName += 1\n\n"
    //            + MAIN_METHOD;
    //
    //        assertCodeIsOk(a, "/syntax/stmt/whileUntil_stmt.xml");
    //    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        String b = this.h.generatePython(fileName, brickConfiguration);
        Assert.assertEquals(a, b);
        //Assert.assertEquals(a.replaceAll("\\s+", ""), b.replaceAll("\\s+", ""));
    }
}
