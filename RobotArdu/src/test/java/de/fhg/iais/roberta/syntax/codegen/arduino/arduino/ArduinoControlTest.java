package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ArduinoControlTest extends ArduinoAstTest {

    @Test
    public void listsTest() throws Exception {
        Map<String, String> ledPins = Util.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst configuration = ArduTestHelper.mkConfigurationAst(led);
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/control/arduino_loops_test.ino",
                "/ast/control/arduino_loops_test.xml",
                configuration);
    }
}
