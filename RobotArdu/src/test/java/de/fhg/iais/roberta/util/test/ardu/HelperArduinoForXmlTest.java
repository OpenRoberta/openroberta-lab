package de.fhg.iais.roberta.util.test.ardu;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.UnoFactory;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.ArduinoCppVisitor;

public class HelperArduinoForXmlTest extends AbstractHelperForXmlTest {

    public HelperArduinoForXmlTest() {
        super(new UnoFactory(new PluginProperties("uno", "", "", Util1.loadProperties("classpath:/uno.properties"))), makeConfiguration());
    }

    private static Configuration makeConfiguration() {
        Map<String, String> buzzerP = createMap("+", "5");
        ConfigurationComponent buzzer = new ConfigurationComponent("BUZZER", true, "buzzer", BlocklyConstants.NO_SLOT, null, buzzerP);

        Map<String, String> dropP = createMap("S", "A0");
        ConfigurationComponent drop = new ConfigurationComponent("DROP", true, "drop", BlocklyConstants.NO_SLOT, null, dropP);

        Map<String, String> encoderP = createMap("OUTPUT", "2");
        ConfigurationComponent encoder = new ConfigurationComponent("ENCODER", true, "encoder", BlocklyConstants.NO_SLOT, null, encoderP);

        Map<String, String> humidityP = createMap("OUTPUT", "2");
        ConfigurationComponent humidity = new ConfigurationComponent("HUMIDITY", true, "humidity", BlocklyConstants.NO_SLOT, null, humidityP);

        Map<String, String> infraredP = createMap("OUTPUT", "11");
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", true, "IR", BlocklyConstants.NO_SLOT, null, infraredP);

        Map<String, String> keyP = createMap("PIN1", "1");
        ConfigurationComponent key = new ConfigurationComponent("KEY", true, "key", BlocklyConstants.NO_SLOT, null, keyP);

        Map<String, String> lcdP = createMap("RS", "12", "E", "11", "D4", "5", "D5", "6", "D6", "3", "D7", "2");
        ConfigurationComponent lcd = new ConfigurationComponent("LCD", true, "L", BlocklyConstants.NO_SLOT, null, lcdP);

        Map<String, String> lcdi2cP = createMap();
        ConfigurationComponent lcdi2c = new ConfigurationComponent("LCDI2C", true, "L", BlocklyConstants.NO_SLOT, null, lcdi2cP);

        Map<String, String> ledP = createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", BlocklyConstants.NO_SLOT, null, ledP);

        Map<String, String> rgbledP = createMap("RED", "5", "GREEN", "6", "BLUE", "3");
        ConfigurationComponent rgbled = new ConfigurationComponent("RGBLED", true, "RGBLED", BlocklyConstants.NO_SLOT, null, rgbledP);

        Map<String, String> lightP = createMap("OUTPUT", "A0");
        ConfigurationComponent light = new ConfigurationComponent("LIGHT", true, "light", BlocklyConstants.NO_SLOT, null, lightP);

        Map<String, String> moistureP = createMap("S", "A0");
        ConfigurationComponent moisture = new ConfigurationComponent("MOISTURE", true, "moisture", BlocklyConstants.NO_SLOT, null, moistureP);

        Map<String, String> motionP = createMap("OUTPUT", "7");
        ConfigurationComponent motion = new ConfigurationComponent("MOTION", true, "motion", BlocklyConstants.NO_SLOT, null, motionP);

        Map<String, String> pulseP = createMap("PULSE", "8");
        ConfigurationComponent pulse = new ConfigurationComponent("PULSE", true, "pulse", BlocklyConstants.NO_SLOT, null, pulseP);

        Map<String, String> stepP = createMap("IN1", "6", "IN2", "5", "IN3", "4", "IN4", "3");
        ConfigurationComponent step = new ConfigurationComponent("STEPMOTOR", true, "step", BlocklyConstants.NO_SLOT, null, stepP);

        Map<String, String> potentiometerP = createMap("OUTPUT", "A0");
        ConfigurationComponent potentiometer =
            new ConfigurationComponent("POTENTIOMETER", true, "potentiometer", BlocklyConstants.NO_SLOT, null, potentiometerP);

        Map<String, String> pulseAP = createMap("S", "A0");
        ConfigurationComponent pulseA = new ConfigurationComponent("PULSE", true, "pulse", BlocklyConstants.NO_SLOT, null, pulseAP);

        Map<String, String> relayP = createMap("IN", "6");
        ConfigurationComponent relay = new ConfigurationComponent("RELAY", true, "relay", BlocklyConstants.NO_SLOT, null, relayP);

        Map<String, String> rfidP = createMap("RST", "9", "SDA", "10", "SCK", "13", "MOSI", "11", "MISO", "12");
        ConfigurationComponent rfid = new ConfigurationComponent("RFID", true, "rfid", BlocklyConstants.NO_SLOT, null, rfidP);

        Map<String, String> temperatureP = createMap("OUTPUT", "A0");
        ConfigurationComponent temperature = new ConfigurationComponent("TEMPERATURE", true, "temperature", BlocklyConstants.NO_SLOT, null, temperatureP);

        Map<String, String> ultrasonicP = createMap("TRIG", "6", "ECHO", "7");
        ConfigurationComponent ultrasonic = new ConfigurationComponent("ULTRASONIC", true, "ultrasonic", BlocklyConstants.NO_SLOT, null, ultrasonicP);

        final Configuration.Builder builder = new Configuration.Builder();
        builder
            .setTrackWidth(17f)
            .setWheelDiameter(5.6f)
            .addComponents(
                Arrays
                    .asList(
                        buzzer,
                        drop,
                        encoder,
                        humidity,
                        infrared,
                        key,
                        lcd,
                        lcdi2c,
                        led,
                        rgbled,
                        light,
                        moisture,
                        motion,
                        pulse,
                        step,
                        potentiometer,
                        pulseA,
                        relay,
                        rfid,
                        temperature,
                        ultrasonic));
        return builder.build();
    }

    public String generateCpp(String pathToProgramXml, Configuration configuration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = ArduinoCppVisitor.generate(configuration, transformer.getTree(), true);
        return code;
    }

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename, Configuration configuration) throws Exception {
        Assert
            .assertEquals(Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""), generateCpp(xmlFilename, configuration).replaceAll("\\s+", ""));
    }
}
