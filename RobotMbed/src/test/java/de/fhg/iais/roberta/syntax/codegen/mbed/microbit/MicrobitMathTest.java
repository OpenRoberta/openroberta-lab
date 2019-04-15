package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMicrobitForXmlTest;

public class MicrobitMathTest {
    private final HelperMicrobitForXmlTest microbitHelper = new HelperMicrobitForXmlTest();

    @Test
    public void mathOnListsTest() throws Exception {
        this.microbitHelper
            .compareExistingAndGeneratedSource(
                "/function/microbit_math_constants_and_functions_test.py",
                "/function/microbit_math_constants_and_functions_test.xml");
    }

    @Test
    public void mathOperationsTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("/function/microbit_math_operations_test.py", "/function/microbit_math_operations_test.xml");
    }

}
