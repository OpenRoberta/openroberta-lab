package de.fhg.iais.roberta.syntax.codegen.arduino.botnroll;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class BotnrollSensorTest {
    HelperBotNrollForXmlTest botnrollHelper = new HelperBotNrollForXmlTest();

    Configuration configuration = HelperBotNrollForXmlTest.makeConfiguration();

    @Test
    public void botnrollSensorTest() throws Exception {
        this.botnrollHelper
            .compareExistingAndGeneratedSource("ast/sensors/botnroll_sensors_test.ino", "/ast/sensors/botnroll_sensors_test.xml", this.configuration);
    }
}
