package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMicrobitForXmlTest;

public class MicrobitActorsTest {
    private final HelperMicrobitForXmlTest microbitHelper = new HelperMicrobitForXmlTest();

    @Test
    public void radioTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("action/microbit_radio_test.py", "/action/microbit_radio_test.xml");
    }

    @Test
    public void setGetPixelBrightnessTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("action/microbit_set_get_pixel_test.py", "/action/microbit_set_get_pixel_test.xml");
    }

}
