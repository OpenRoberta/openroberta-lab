package de.fhg.iais.roberta.util.test.ardu;

import java.util.HashMap;
import java.util.Properties;

import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.UnoFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class HelperArduinoForXmlTest extends AbstractHelperForXmlTest {

    public HelperArduinoForXmlTest() {
        super(
            new UnoFactory(new PluginProperties("uno", "", "", Util1.loadProperties("classpath:uno.properties"))),
            new ArduinoConfiguration.Builder()
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.BUZZER, "buzzer", new HashMap<String, String>() {
                    {
                        put("+", "5");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.DROP, "drop", new HashMap<String, String>() {
                    {
                        put("S", "A0");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.ENCODER, "encoder", new HashMap<String, String>() {
                    {
                        put("OUTPUT", "2");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.HUMIDITY, "humidity", new HashMap<String, String>() {
                    {
                        put("OUTPUT", "2");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.INFRARED, "IR", new HashMap<String, String>() {
                    {
                        put("OUTPUT", "11");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.KEY, "key", new HashMap<String, String>() {
                    {
                        put("PIN1", "1");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LCD, "L", new HashMap<String, String>() {
                    {
                        put("RS", "12");
                        put("E", "11");
                        put("D4", "5");
                        put("D5", "6");
                        put("D6", "3");
                        put("D7", "2");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LCDI2C, "L", new HashMap<String, String>()))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LED, "LED", new HashMap<String, String>() {
                    {
                        put("INPUT", "13");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.RGBLED, "RGBLED", new HashMap<String, String>() {
                    {
                        put("RED", "5");
                        put("GREEN", "6");
                        put("BLUE", "3");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LIGHT, "light", new HashMap<String, String>() {
                    {
                        put("OUTPUT", "A0");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.MOISTURE, "moisture", new HashMap<String, String>() {
                    {
                        put("S", "A0");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.MOTION, "motion", new HashMap<String, String>() {
                    {
                        put("OUTPUT", "7");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.SERVOMOTOR, "servo", new HashMap<String, String>() {
                    {
                        put("PULSE", "8");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.STEPMOTOR, "step", new HashMap<String, String>() {
                    {
                        put("IN1", "6");
                        put("IN2", "5");
                        put("IN3", "4");
                        put("IN4", "3");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.POTENTIOMETER, "potentiometer", new HashMap<String, String>() {
                    {
                        put("OUTPUT", "A0");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.PULSE, "pulse", new HashMap<String, String>() {
                    {
                        put("S", "A0");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.RELAY, "pulse", new HashMap<String, String>() {
                    {
                        put("IN", "6");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.RFID, "RFID", new HashMap<String, String>() {
                    {
                        put("RST", "9");
                        put("SDA", "10");
                        put("SCK", "13");
                        put("MOSI", "11");
                        put("MISO", "12");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.TEMPERATURE, "temperature", new HashMap<String, String>() {
                    {
                        put("OUTPUT", "A0");
                    }
                }))
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.ULTRASONIC, "ultrasonic", new HashMap<String, String>() {
                    {
                        put("TRIG", "6");
                        put("ECHO", "7");
                    }
                }))
                .build());
        Properties robotProperties = Util1.loadProperties("classpath:Robot.properties");
        AbstractRobotFactory.addBlockTypesFromProperties("Robot", robotProperties);
    }
}
