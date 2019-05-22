package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class SwitchLedMatrixActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void calliopeGenerateSource_GivenXml_ShouldGenerateSameSource() throws Exception {
        this.h.compareExistingAndGeneratedSource("/action/switch_led_matrix.cpp", "/action/switch_led_matrix.xml");
    }
}
