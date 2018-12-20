package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMicrobitForXmlTest;

public class MicrobitTextTest {
    private final HelperMicrobitForXmlTest microbitHelper = new HelperMicrobitForXmlTest();

    @Test
    public void mathOnListsTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("/function/microbit_text_join_test.py", "/function/microbit_text_join_test.xml");
    }

}
