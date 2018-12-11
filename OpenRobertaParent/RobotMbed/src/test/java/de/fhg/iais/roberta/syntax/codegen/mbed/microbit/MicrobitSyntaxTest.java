package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperMicrobitForXmlTest;

public class MicrobitSyntaxTest {
    private final HelperMicrobitForXmlTest microbitHelper = new HelperMicrobitForXmlTest();

    @Test
    public void emptyValuesTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("stmts/microbit_emtpy_values_test.py", "/stmts/microbit_emtpy_values_test.xml");
    }

    @Test
    public void waitTimeConditionTest() throws Exception {
        this.microbitHelper.compareExistingAndGeneratedSource("stmts/microbit_wait_test.py", "/stmts/microbit_wait_test.xml");
    }
}
