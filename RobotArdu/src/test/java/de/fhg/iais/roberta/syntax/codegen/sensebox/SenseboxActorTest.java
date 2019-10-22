package de.fhg.iais.roberta.syntax.codegen.sensebox;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SenseboxActorTest extends SenseboxAstTest {

    @Test
    public void sdCardOledOffTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/sensebox_write_sdcard_display_clear_test.ino",
                "/ast/actions/sensebox_write_sdcard_display_clear_test.xml",
                "/ast/actions/sensebox_write_sdcard_display_clear_test_config.xml");
    }

    @Test
    public void buzzerLedRgbTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/sensebox_buzzer_led_rgb_test.ino",
                "/ast/actions/sensebox_buzzer_led_rgb_test.xml",
                "/ast/actions/sensebox_buzzer_led_rgb_test_config.xml");
    }

    @Test
    public void serialOledSendDataTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/sensebox_serial_oled_upload_test.ino",
                "/ast/actions/sensebox_serial_oled_upload_test.xml",
                "/ast/actions/sensebox_serial_oled_upload_test_config.xml");
    }

    @Test
    public void plottingAccelerometerValuesTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/sensebox_plotting_test.ino",
                "/ast/actions/sensebox_plotting_test.xml",
                "/ast/actions/sensebox_plotting_test_config.xml");
    }
}
