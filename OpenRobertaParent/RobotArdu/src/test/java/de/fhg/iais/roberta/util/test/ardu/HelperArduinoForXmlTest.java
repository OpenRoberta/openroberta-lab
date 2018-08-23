package de.fhg.iais.roberta.util.test.ardu;

import java.util.ArrayList;
import java.util.Properties;

import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.ConfigurationBlockType;
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.arduino.uno.Factory;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class HelperArduinoForXmlTest extends AbstractHelperForXmlTest {

    public HelperArduinoForXmlTest() {
        super(
            new Factory(new RobertaProperties(Util1.loadProperties(null))),
            new ArduinoConfiguration.Builder()
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.BUZZER), "buzzer", null, new ArrayList<String>() {
                    {
                        add("5");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.DROP), "drop", null, new ArrayList<String>() {
                    {
                        add("A0");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.ENCODER), "encoder", null, new ArrayList<String>() {
                    {
                        add("2");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.HUMIDITY), "humidity", null, new ArrayList<String>() {
                    {
                        add("2");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.INFRARED), "IR", null, new ArrayList<String>() {
                    {
                        add("11");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.KEY), "key", null, new ArrayList<String>() {
                    {
                        add("1");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LCD), "L", null, new ArrayList<String>() {
                    {
                        add("12");
                        add("11");
                        add("5");
                        add("6");
                        add("3");
                        add("2");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LCDI2C), "L", null, null)
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LED), "LED", null, new ArrayList<String>() {
                    {
                        add("13");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.RGBLED), "RGBLED", null, new ArrayList<String>() {
                    {
                        add("5");
                        add("6");
                        add("3");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.LIGHT), "light", null, new ArrayList<String>() {
                    {
                        add("A0");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.MOISTURE), "moisture", null, new ArrayList<String>() {
                    {
                        add("A0");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.MOTION), "motion", null, new ArrayList<String>() {
                    {
                        add("7");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.SERVOMOTOR), "servo", null, new ArrayList<String>() {
                    {
                        add("8");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.STEPMOTOR), "step", null, new ArrayList<String>() {
                    {
                        add("6");
                        add("5");
                        add("4");
                        add("3");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.POTENTIOMETER), "potentiometer", null, new ArrayList<String>() {
                    {
                        add("A0");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.PULSE), "pulse", null, new ArrayList<String>() {
                    {
                        add("A0");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.RELAY), "pulse", null, new ArrayList<String>() {
                    {
                        add("6");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.RFID), "RFID", null, new ArrayList<String>() {
                    {
                        add("9");
                        add("10");
                        add("13");
                        add("11");
                        add("12");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.TEMPERATURE), "temperature", null, new ArrayList<String>() {
                    {
                        add("A0");
                    }
                })
                .addConfigurationBlock(new ConfigurationBlock(ConfigurationBlockType.ULTRASONIC), "ultrasonic", null, new ArrayList<String>() {
                    {
                        add("6");
                        add("7");
                    }
                })
                .build());
        Properties robotProperties = Util1.loadProperties("classpath:Robot.properties");
        AbstractRobotFactory.addBlockTypesFromProperties("Robot.properties", robotProperties);
    }
}
