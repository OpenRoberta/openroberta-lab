package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ArduinoSensorTest extends ArduinoAstTest {

    @Test
    public void analogDigitalReadTest() throws Exception {
        Map<String, String> analogInputPins = createMap("OUTPUT", "A0");
        Map<String, String> digitalInputPins = createMap("OUTPUT", "0");
        ConfigurationComponent analogInput = new ConfigurationComponent("ANALOG_PIN", true, "ANALOG_PIN", "S2", analogInputPins);
        ConfigurationComponent digitalInput = new ConfigurationComponent("DIGITAL_PIN", true, "DIGITAL_PIN", "S", digitalInputPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(analogInput, digitalInput));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_analog_digital_input_test.ino",
                "/ast/sensors/arduino_analog_digital_input_test.xml",
                builder.build());
    }

    @Test
    public void timerTest() throws Exception {
        Map<String, String> ledPins = createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_timer_test.ino",
                "/ast/sensors/arduino_timer_test.xml",
                builder.build());
    }

    @Test
    public void buttonTest() throws Exception {
        Map<String, String> buttonPins = createMap("PIN1", "2");
        ConfigurationComponent button = new ConfigurationComponent("KEY", true, "key", "B", buttonPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(button));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_button_test.ino",
                "/ast/sensors/arduino_button_test.xml",
                builder.build());
    }

    @Test
    public void presenceTest() throws Exception {
        Map<String, String> motionPins = createMap("OUTPUT", "7");
        ConfigurationComponent motion = new ConfigurationComponent("MOTION", true, "motion", "M", motionPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motion));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_presence_test.ino",
                "/ast/sensors/arduino_presence_test.xml",
                builder.build());
    }

    @Test
    public void lightTest() throws Exception {
        Map<String, String> lightPins = createMap("OUTPUT", "A0");
        ConfigurationComponent light = new ConfigurationComponent("LIGHT", true, "light", "L", lightPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(light));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_light_test.ino",
                "/ast/sensors/arduino_light_test.xml",
                builder.build());
    }

    @Test
    public void infraredTest() throws Exception {
        Map<String, String> infraredP = createMap("OUTPUT", "11");
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", true, "IR", "I", infraredP);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(infrared));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_infrared_test.ino",
                "/ast/sensors/arduino_infrared_test.xml",
                builder.build());
    }

    @Test
    public void infraredPresenceTest() throws Exception {
        Map<String, String> infraredP = createMap("OUTPUT", "11");
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", true, "IR", "I", infraredP);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(infrared));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_infrared_presence_test.ino",
                "/ast/sensors/arduino_infrared_presence_test.xml",
                builder.build());
    }

    @Test
    public void temperatureTest() throws Exception {
        Map<String, String> temperaturePins = createMap("OUTPUT", "A0");
        ConfigurationComponent temperature = new ConfigurationComponent("TEMPERATURE", true, "temperature", "T", temperaturePins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(temperature));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_temperature_test.ino",
                "/ast/sensors/arduino_temperature_test.xml",
                builder.build());
    }

    @Test
    public void humidityTest() throws Exception {
        Map<String, String> humidityPins = createMap("OUTPUT", "2");
        ConfigurationComponent humidity = new ConfigurationComponent("HUMIDITY", true, "humidity", "H", humidityPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(humidity));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_humidity_test.ino",
                "/ast/sensors/arduino_humidity_test.xml",
                builder.build());
    }

    @Test
    public void dropTest() throws Exception {
        Map<String, String> dropPins = createMap("S", "A0");
        ConfigurationComponent drop = new ConfigurationComponent("DROP", true, "drop", "D", dropPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(drop));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_drop_test.ino",
                "/ast/sensors/arduino_drop_test.xml",
                builder.build());
    }

    @Test
    public void pulseTest() throws Exception {
        Map<String, String> pulsePins = createMap("S", "A0");
        ConfigurationComponent pulse = new ConfigurationComponent("PULSE", true, "pulse", "P", pulsePins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(pulse));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_pulse_test.ino",
                "/ast/sensors/arduino_pulse_test.xml",
                builder.build());
    }

    @Test
    public void potentiometerTest() throws Exception {
        Map<String, String> potentiometerPins = createMap("OUTPUT", "A0");
        ConfigurationComponent potentiometer = new ConfigurationComponent("POTENTIOMETER", true, "potentiometer", "P2", potentiometerPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(potentiometer));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_potentiometer_test.ino",
                "/ast/sensors/arduino_potentiometer_test.xml",
                builder.build());
    }

    @Test
    public void moistureTest() throws Exception {
        Map<String, String> moisturePins = createMap("S", "A0");
        ConfigurationComponent moisture = new ConfigurationComponent("MOISTURE", true, "moisture", "M", moisturePins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(moisture));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_moisture_test.ino",
                "/ast/sensors/arduino_moisture_test.xml",
                builder.build());
    }

    @Test
    public void ultrasonicTest() throws Exception {
        Map<String, String> ultrasonicPins = createMap("TRIG", "7", "ECHO", "6");
        ConfigurationComponent ultrasonic = new ConfigurationComponent("ULTRASONIC", true, "ultrasonic", "U", ultrasonicPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(ultrasonic));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_ultrasonic_test.ino",
                "/ast/sensors/arduino_ultrasonic_test.xml",
                builder.build());
    }

    @Test
    public void rfidTest() throws Exception {
        Map<String, String> rfidPins = createMap("RST", "9", "SDA", "10", "SCK", "13", "MOSI", "11", "MISO", "12");
        ConfigurationComponent rfid = new ConfigurationComponent("RFID", true, "rfid", "R", rfidPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(rfid));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/sensors/arduino_rfid_test.ino",
                "/ast/sensors/arduino_rfid_test.xml",
                builder.build());
    }
}
