package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.test.ardu.HelperArduinoForXmlTest;

public class ArduinoControlTest {
    private final HelperArduinoForXmlTest arduinoHelper = new HelperArduinoForXmlTest();

    @Test
    public void listsTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper.compareExistingAndGeneratedSource("/ast/control/arduino_loops_test.ino", "/ast/control/arduino_loops_test.xml", builder.build());
    }
}
