package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMicrobitForXmlTest;

public class MicrobitListsTest {
    private final HelperMicrobitForXmlTest microbitHelper = new HelperMicrobitForXmlTest();

    @Test
    public void mathOnListsTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("/lists/microbit_math_on_lists_test.py", "/lists/microbit_math_on_lists_test.xml");
    }

    @Test
    public void fullListsTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("/lists/microbit_lists_full_test.py", "/lists/microbit_lists_full_test.xml");
    }

}
