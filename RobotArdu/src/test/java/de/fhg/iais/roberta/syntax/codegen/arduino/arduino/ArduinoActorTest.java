package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.ardu.HelperArduinoForXmlTest;

public class ArduinoActorTest {
    private final HelperArduinoForXmlTest arduinoHelper = new HelperArduinoForXmlTest();

    @Test
    public void relayOnTest() throws Exception {
        Map<String, String> relayPins = HelperArduinoForXmlTest.createMap("IN", "6");
        ConfigurationComponent relay = new ConfigurationComponent("RELAY", true, "relay", "R", relayPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(relay));
        this.arduinoHelper.compareExistingAndGeneratedSource("/ast/actions/arduino_relay_test.ino", "/ast/actions/arduino_relay_test.xml", builder.build());
    }

    @Test
    public void stepperMotorTest() throws Exception {
        Map<String, String> stepperMotorPins = HelperArduinoForXmlTest.createMap("IN1", "6", "IN2", "5", "IN3", "4", "IN4", "3");
        ConfigurationComponent stepperMotor = new ConfigurationComponent("STEPMOTOR", true, "step", "S2", stepperMotorPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(stepperMotor));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("/ast/actions/arduino_stepper_motor_test.ino", "/ast/actions/arduino_stepper_motor_test.xml", builder.build());
    }

    @Test
    public void stepperMotorDegreeTest() throws Exception {
        Map<String, String> stepperMotorPins = HelperArduinoForXmlTest.createMap("IN1", "6", "IN2", "5", "IN3", "4", "IN4", "3");
        ConfigurationComponent stepperMotor = new ConfigurationComponent("STEPMOTOR", true, "step", "S2", stepperMotorPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(stepperMotor));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("/ast/actions/arduino_stepper_degree_test.ino", "/ast/actions/arduino_stepper_degree_test.xml", builder.build());
    }

    @Test
    public void servoMotorTest() throws Exception {
        Map<String, String> servoMotorPins = HelperArduinoForXmlTest.createMap("PULSE", "8");
        ConfigurationComponent servoMotor = new ConfigurationComponent("SERVOMOTOR", true, "servo", "S", servoMotorPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(servoMotor));
        this.arduinoHelper
            .compareExistingAndGeneratedSource("/ast/actions/arduino_servo_motor_test.ino", "/ast/actions/arduino_servo_motor_test.xml", builder.build());
    }

    @Test
    public void serialPrintWithLEDTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper.compareExistingAndGeneratedSource(
            "/ast/actions/arduino_show_on_serial_with_led_test.ino",
            "/ast/actions/arduino_show_on_serial_with_led_test.xml",
            builder.build());
    }

    @Test
    public void lcd1602ShowClearTest() throws Exception {
        Map<String, String> screenPins = HelperArduinoForXmlTest.createMap("RS", "12", "E", "11", "D4", "5", "D5", "4", "D6", "3", "D7", "2");
        ConfigurationComponent screen = new ConfigurationComponent("LCD", true, "L", "L2", screenPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(screen));
        this.arduinoHelper.compareExistingAndGeneratedSource(
            "/ast/actions/arduino_lcd1602_show_clear_test.ino",
            "/ast/actions/arduino_lcd1602_show_clear_test.xml",
            builder.build());
    }

    @Test
    public void lcd1602i2cShowClearTest() throws Exception {
        Map<String, String> screenPins = HelperArduinoForXmlTest.createMap();
        ConfigurationComponent screen = new ConfigurationComponent("LCDI2C", true, "L", "L", screenPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(screen));
        this.arduinoHelper.compareExistingAndGeneratedSource(
            "/ast/actions/arduino_lcd1602i2c_show_clear_test.ino",
            "/ast/actions/arduino_lcd1602i2c_show_clear_test.xml",
            builder.build());
    }

    @Test
    public void buzzerTest() throws Exception {
        Map<String, String> buzzerPins = HelperArduinoForXmlTest.createMap("+", "5");
        ConfigurationComponent buzzer = new ConfigurationComponent("BUZZER", true, "buzzer", "B", buzzerPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(buzzer));
        this.arduinoHelper.compareExistingAndGeneratedSource("/ast/actions/arduino_buzzer_test.ino", "/ast/actions/arduino_buzzer_test.xml", builder.build());
    }

    @Test
    public void ledTest() throws Exception {
        Map<String, String> ledPins = HelperArduinoForXmlTest.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        this.arduinoHelper.compareExistingAndGeneratedSource("/ast/actions/arduino_led_test.ino", "/ast/actions/arduino_led_test.xml", builder.build());
    }

    @Test
    public void rgbLedTest() throws Exception {
        Map<String, String> rgbLedPins = HelperArduinoForXmlTest.createMap("RED", "5", "GREEN", "6", "BLUE", "3");
        ConfigurationComponent rgbLed = new ConfigurationComponent("RGBLED", true, "RGBLED", "R", rgbLedPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(rgbLed));
        this.arduinoHelper.compareExistingAndGeneratedSource("/ast/actions/arduino_rgb_led_test.ino", "/ast/actions/arduino_rgb_led_test.xml", builder.build());
    }

    @Test
    public void analogDigitalPinWriteTest() throws Exception {
        Map<String, String> analogOuputPins = HelperArduinoForXmlTest.createMap("INPUT", "3");
        Map<String, String> digitalOuputPins = HelperArduinoForXmlTest.createMap("INPUT", "0");
        ConfigurationComponent analogOutput = new ConfigurationComponent("ANALOG_INPUT", true, "ANALOG_INPUT", "A2", analogOuputPins);
        ConfigurationComponent digitalOutput =
            new ConfigurationComponent("DIGITAL_INPUT", true, "DIGITAL_INPUT", "A", digitalOuputPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(digitalOutput, analogOutput));
        this.arduinoHelper.compareExistingAndGeneratedSource(
            "/ast/actions/arduino_analog_digital_output_test.ino",
            "/ast/actions/arduino_analog_digital_output_test.xml",
            builder.build());
    }

    @Test
    public void multiIncludeTest() throws Exception {
        Map<String, String> servoMotorPins = HelperArduinoForXmlTest.createMap("PULSE", "8");
        ConfigurationComponent servoMotor1 = new ConfigurationComponent("SERVOMOTOR", true, "servo", "S", servoMotorPins);
        ConfigurationComponent servoMotor2 = new ConfigurationComponent("SERVOMOTOR", true, "servo", "S2", servoMotorPins);
        Map<String, String> rfidPins = HelperArduinoForXmlTest.createMap("RST", "9", "SDA", "10", "SCK", "13", "MOSI", "11", "MISO", "12");
        ConfigurationComponent rfid1 = new ConfigurationComponent("RFID", true, "rfid", "R6", rfidPins);
        ConfigurationComponent rfid2 = new ConfigurationComponent("RFID", true, "rfid", "R7", rfidPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(rfid1, rfid2, servoMotor1, servoMotor2));
        this.arduinoHelper.compareExistingAndGeneratedSource(
            "/ast/brickConfiguration/arduino_multi_include_test.ino",
            "/ast/brickConfiguration/arduino_multi_include_test.xml",
            builder.build());
    }

    @Test(expected = DbcException.class)
    public void negativeIncludeTest() throws Exception {
        Map<String, String> servoMotorPins = HelperArduinoForXmlTest.createMap("PULSE", "8");
        ConfigurationComponent servoMotor1 = new ConfigurationComponent("NON-EXISTING-COMPONENT", true, "servo", "S", servoMotorPins);
        ConfigurationComponent servoMotor2 =
            new ConfigurationComponent("NON-EXISTING-COMPONENT2", true, "servo", "S2", servoMotorPins);
        Map<String, String> rfidPins = HelperArduinoForXmlTest.createMap("RST", "9", "SDA", "10", "SCK", "13", "MOSI", "11", "MISO", "12");
        ConfigurationComponent rfid1 = new ConfigurationComponent("NON-EXISTING-COMPONENT", true, "rfid", "R6", rfidPins);
        ConfigurationComponent rfid2 = new ConfigurationComponent("NON-EXISTING-COMPONENT2", true, "rfid", "R7", rfidPins);
        Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(rfid1, rfid2, servoMotor1, servoMotor2));
        this.arduinoHelper.compareExistingAndGeneratedSource(
            "/ast/brickConfiguration/arduino_multi_include_test.ino",
            "/ast/brickConfiguration/arduino_multi_include_test.xml",
            builder.build());
    }
}
