package de.fhg.iais.roberta.syntax.codegen.sensebox;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperSenseboxForXmlTest;

public class SenseboxSensorTest {
    private final HelperSenseboxForXmlTest senseboxHelper = new HelperSenseboxForXmlTest();

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
