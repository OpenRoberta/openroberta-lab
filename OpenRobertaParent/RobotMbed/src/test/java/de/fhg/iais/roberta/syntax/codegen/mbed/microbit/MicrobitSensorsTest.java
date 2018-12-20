package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMicrobitForXmlTest;

public class MicrobitSensorsTest {
    private final HelperMicrobitForXmlTest microbitHelper = new HelperMicrobitForXmlTest();

    @Test
    public void waitTimeConditionTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("/sensor/microbit_timer_test.py", "/sensor/microbit_timer_test.xml");
    }

}
