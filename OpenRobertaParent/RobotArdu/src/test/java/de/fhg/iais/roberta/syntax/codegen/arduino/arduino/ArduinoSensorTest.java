package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.test.ardu.HelperArduinoForXmlTest;

public class ArduinoSensorTest {
    private final HelperArduinoForXmlTest arduinoHelper = new HelperArduinoForXmlTest();

    @Test
    public void analogDigitalReadTest() throws Exception {
        Map<String, String> analogInputPins = HelperArduinoForXmlTest.createMap("OUTPUT", "A0");
        Map<String, String> digitalInputPins = HelperArduinoForXmlTest.createMap("OUTPUT", "0");
        ConfigurationComponent analogInput = new ConfigurationComponent("ANALOG_PIN", true, "ANALOG_PIN", BlocklyConstants.NO_SLOT, "S2", analogInputPins);
        ConfigurationComponent digitalInput = new ConfigurationComponent("DIGITAL_PIN", true, "DIGITAL_PIN", BlocklyConstants.NO_SLOT, "S", digitalInputPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(analogInput, digitalInput));
        this.arduinoHelper
            .compareExistingAndGeneratedSource(
                "ast/sensors/arduino_analog_digital_input_test.ino",
                "/ast/sensors/arduino_analog_digital_input_test.xml",
                builder.build());
    }

    @Test
    public void timerTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", BlocklyConstants.NO_SLOT, "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/sensors/arduino_timer_test.ino", "/ast/sensors/arduino_timer_test.xml", builder.build());
    }

    @Test
    public void buttonTest() throws Exception {
        Map<String, String> buttonPins = HelperArduinoForXmlTest.createMap("PIN1", "2");
        ConfigurationComponent button = new ConfigurationComponent("KEY", true, "key", BlocklyConstants.NO_SLOT, "B", buttonPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(button));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/sensors/arduino_button_test.ino", "/ast/sensors/arduino_button_test.xml", builder.build());
    }

    @Test
    public void presenceTest() throws Exception {
        Map<String, String> motionPins = HelperArduinoForXmlTest.createMap("OUTPUT", "7");
        ConfigurationComponent motion = new ConfigurationComponent("MOTION", true, "motion", BlocklyConstants.NO_SLOT, "M", motionPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motion));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/sensors/arduino_presence_test.ino", "/ast/sensors/arduino_presence_test.xml", builder.build());
    }

    @Test
    public void lightTest() throws Exception {
        Map<String, String> lightPins = HelperArduinoForXmlTest.createMap("OUTPUT", "A0");
        ConfigurationComponent light = new ConfigurationComponent("LIGHT", true, "light", BlocklyConstants.NO_SLOT, "L", lightPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(light));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/sensors/arduino_light_test.ino", "/ast/sensors/arduino_light_test.xml", builder.build());
    }

    @Test
    public void infraredTest() throws Exception {
        Map<String, String> infraredP = HelperArduinoForXmlTest.createMap("OUTPUT", "11");
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", true, "IR", BlocklyConstants.NO_SLOT, "I", infraredP);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(infrared));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/sensors/arduino_infrared_test.ino", "/ast/sensors/arduino_infrared_test.xml", builder.build());
    }

    @Test
    public void infraredPresenceTest() throws Exception {
        Map<String, String> infraredP = HelperArduinoForXmlTest.createMap("OUTPUT", "11");
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", true, "IR", BlocklyConstants.NO_SLOT, "I", infraredP);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(infrared));
        this.arduinoHelper
            .compareExistingAndGeneratedSource(
                "ast/sensors/arduino_infrared_presence_test.ino",
                "/ast/sensors/arduino_infrared_presence_test.xml",
                builder.build());
    }

    @Test
    public void temperatureTest() throws Exception {
        Map<String, String> temperaturePins = HelperArduinoForXmlTest.createMap("OUTPUT", "A0");
        ConfigurationComponent temperature = new ConfigurationComponent("TEMPERATURE", true, "temperature", BlocklyConstants.NO_SLOT, "T", temperaturePins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(temperature));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/sensors/arduino_temperature_test.ino", "/ast/sensors/arduino_temperature_test.xml", builder.build());
    }

    @Test
    public void humidityTest() throws Exception {
        Map<String, String> humidityPins = HelperArduinoForXmlTest.createMap("OUTPUT", "2");
        ConfigurationComponent humidity = new ConfigurationComponent("HUMIDITY", true, "humidity", BlocklyConstants.NO_SLOT, "H", humidityPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(humidity));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/sensors/arduino_humidity_test.ino", "/ast/sensors/arduino_humidity_test.xml", builder.build());
    }

    @Test
    public void dropTest() throws Exception {
        Map<String, String> dropPins = HelperArduinoForXmlTest.createMap("S", "A0");
        ConfigurationComponent drop = new ConfigurationComponent("DROP", true, "drop", BlocklyConstants.NO_SLOT, "D", dropPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(drop));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/sensors/arduino_drop_test.ino", "/ast/sensors/arduino_drop_test.xml", builder.build());
    }

    @Test
    public void pulseTest() throws Exception {
        Map<String, String> pulsePins = HelperArduinoForXmlTest.createMap("S", "A0");
        ConfigurationComponent pulse = new ConfigurationComponent("PULSE", true, "pulse", BlocklyConstants.NO_SLOT, "P", pulsePins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(pulse));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/sensors/arduino_pulse_test.ino", "/ast/sensors/arduino_pulse_test.xml", builder.build());
    }

    @Test
    public void potentiometerTest() throws Exception {
        Map<String, String> potentiometerPins = HelperArduinoForXmlTest.createMap("OUTPUT", "A0");
        ConfigurationComponent potentiometer =
            new ConfigurationComponent("POTENTIOMETER", true, "potentiometer", BlocklyConstants.NO_SLOT, "P2", potentiometerPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(potentiometer));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/sensors/arduino_potentiometer_test.ino", "/ast/sensors/arduino_potentiometer_test.xml", builder.build());
    }

    @Test
    public void moistureTest() throws Exception {
        Map<String, String> moisturePins = HelperArduinoForXmlTest.createMap("S", "A0");
        ConfigurationComponent moisture = new ConfigurationComponent("MOISTURE", true, "moisture", BlocklyConstants.NO_SLOT, "M", moisturePins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(moisture));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/sensors/arduino_moisture_test.ino", "/ast/sensors/arduino_moisture_test.xml", builder.build());
    }

    @Test
    public void ultrasonicTest() throws Exception {
        Map<String, String> ultrasonicPins = HelperArduinoForXmlTest.createMap("TRIG", "7", "ECHO", "6");
        ConfigurationComponent ultrasonic = new ConfigurationComponent("ULTRASONIC", true, "ultrasonic", BlocklyConstants.NO_SLOT, "U", ultrasonicPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(ultrasonic));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("ast/sensors/arduino_ultrasonic_test.ino", "/ast/sensors/arduino_ultrasonic_test.xml", builder.build());
    }

    @Test
    public void rfidTest() throws Exception {
        Map<String, String> rfidPins = HelperArduinoForXmlTest.createMap("RST", "9", "SDA", "10", "SCK", "13", "MOSI", "11", "MISO", "12");
        ConfigurationComponent rfid = new ConfigurationComponent("RFID", true, "rfid", BlocklyConstants.NO_SLOT, "R", rfidPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(rfid));
        this.arduinoHelper.compareExistingAndGeneratedSource("ast/sensors/arduino_rfid_test.ino", "/ast/sensors/arduino_rfid_test.xml", builder.build());
    }
}
