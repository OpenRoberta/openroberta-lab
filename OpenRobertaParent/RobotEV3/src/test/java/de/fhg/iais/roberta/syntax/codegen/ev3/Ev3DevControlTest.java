package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class Ev3DevControlTest {
    private final HelperEv3ForXmlTest ev3DevHelper = new HelperEv3ForXmlTest();
    private final Configuration configuration = HelperEv3ForXmlTest.makeStandardEv3DevConfiguration();

    @Test
    public void ev3DevWaitTest() throws Exception {
        this.ev3DevHelper.compareExistingAndGeneratedPythonSource("/ast/control/ev3dev_wait_test.py", "/ast/control/ev3dev_wait_test.xml", this.configuration);
    }
}
