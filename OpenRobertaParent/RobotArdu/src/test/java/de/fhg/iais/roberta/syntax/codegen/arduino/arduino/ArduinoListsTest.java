package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.test.ardu.HelperArduinoForXmlTest;

public class ArduinoListsTest {
    private final HelperArduinoForXmlTest arduinoHelper = new HelperArduinoForXmlTest();

    @Test
    public void listsTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", BlocklyConstants.NO_SLOT, "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/lists/arduino_lists_test.ino", "/ast/lists/arduino_lists_test.xml", builder.build());
    }

}
