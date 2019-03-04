package de.fhg.iais.roberta.syntax.codegen.sensebox;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperSenseboxForXmlTest;

public class SenseboxActorTest {
    private final HelperSenseboxForXmlTest senseboxHelper = new HelperSenseboxForXmlTest();

    @Test
    public void buzzerLedRgbTest() throws Exception {
        Configuration config = this.senseboxHelper.regenerateConfiguration(Util1.readResourceContent("/ast/actions/sensebox_buzzer_led_rgb_test_config.xml"));
        this.senseboxHelper
            .compareExistingAndGeneratedSource("/ast/actions/sensebox_buzzer_led_rgb_test.ino", "/ast/actions/sensebox_buzzer_led_rgb_test.xml", config);
    }

    @Test
    public void serialOledSendDataTest() throws Exception {
        Configuration config =
            this.senseboxHelper.regenerateConfiguration(Util1.readResourceContent("/ast/actions/sensebox_serial_oled_upload_test_config.xml"));
        this.senseboxHelper
            .compareExistingAndGeneratedSource(
                "/ast/actions/sensebox_serial_oled_upload_test.ino",
                "/ast/actions/sensebox_serial_oled_upload_test.xml",
                config);
    }
}
