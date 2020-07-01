package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.MicrobitAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitActorsTest extends MicrobitAstTest {

    @Test
    public void radioTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(testFactory, "/action" + "/microbit_radio_test.py", "/action/microbit_radio_test.xml", configuration);
    }

    @Test
    public void setGetPixelBrightnessTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/action/microbit_set_get_pixel_test.py",
                "/action/microbit_set_get_pixel_test.xml",
                configuration);
    }

}
