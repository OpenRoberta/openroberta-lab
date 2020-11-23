package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ArduinoTextTest extends ArduinoAstTest {

    @Test
    public void listsTest() throws Exception {
        Map<String, String> ledPins = Util.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst configuration = ArduTestHelper.mkConfigurationAst(led);

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/text/arduino_append_text_test.ino",
                "/ast/text/arduino_append_text_test.xml",
                configuration);
    }
}
