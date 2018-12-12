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

    @Test
    public void listsOccuranceTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", BlocklyConstants.NO_SLOT, "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/lists/arduino_occurance_lists_test.ino", "/ast/lists/arduino_occurance_lists_test.xml", builder.build());
    }

    @Test
    public void listsRepeatTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", BlocklyConstants.NO_SLOT, "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/lists/arduino_list_repeat_test.ino", "/ast/lists/arduino_list_repeat_test.xml", builder.build());
    }

    @Test
    public void listsSublistTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", BlocklyConstants.NO_SLOT, "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/lists/arduino_sublist_test.ino", "/ast/lists/arduino_sublist_test.xml", builder.build());
    }

    @Test
    public void listsGetSetTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", BlocklyConstants.NO_SLOT, "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/lists/arduino_lists_get_set_test.ino", "/ast/lists/arduino_lists_get_set_test.xml", builder.build());
    }

}
