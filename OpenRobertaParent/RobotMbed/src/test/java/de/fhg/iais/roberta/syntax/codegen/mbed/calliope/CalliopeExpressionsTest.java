package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class CalliopeExpressionsTest {
    HelperCalliopeForXmlTest calliopeHelper = new HelperCalliopeForXmlTest();

    @Test
    public void calliopeBinaryTest() throws Exception {
        this.calliopeHelper.compareExistingAndGeneratedSource("expr/calliope_binary_test.cpp", "/expr/calliope_binary_test.xml");
    }

}
