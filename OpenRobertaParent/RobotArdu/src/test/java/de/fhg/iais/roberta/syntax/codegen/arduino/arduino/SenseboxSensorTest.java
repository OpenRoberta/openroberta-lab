package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperSenseboxForXmlTest;

public class SenseboxSensorTest {
    private final HelperSenseboxForXmlTest senseboxHelper = new HelperSenseboxForXmlTest();

    @Test
    public void sensorsAndSenddataTest() throws Exception {
        this.senseboxHelper
            .compareExistingAndGeneratedSource(
                "/ast/sensors/sensebox_i2c_sensor_and_senddata_test.ino",
                "/ast/sensors/sensebox_i2c_sensor_and_senddata_test.xml",
                HelperSenseboxForXmlTest.makeConfiguration());
    }
}
