package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;
import org.junit.Assert;
import org.junit.Test;

public class SwitchLedMatrixActionTest {
    private final HelperCalliopeForXmlTest h = new HelperCalliopeForXmlTest();

    @Test
    public void calliopeGenerateSource_GivenXml_ShouldGenerateSameSource() throws Exception {
        this.h.compareExistingAndGeneratedSource("/action/switch_led_matrix.cpp", "/action/switch_led_matrix.xml");
    }
}
