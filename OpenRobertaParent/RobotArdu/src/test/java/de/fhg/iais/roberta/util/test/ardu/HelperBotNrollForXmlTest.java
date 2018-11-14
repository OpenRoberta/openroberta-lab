package de.fhg.iais.roberta.util.test.ardu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BotnrollFactory;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class HelperBotNrollForXmlTest extends AbstractHelperForXmlTest {

    public HelperBotNrollForXmlTest() {
        super(new BotnrollFactory(new PluginProperties("botnroll", "", "", Util1.loadProperties("classpath:botnroll.properties"))), makeConfiguration());
    }

    private static Configuration makeConfiguration() {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", BlocklyConstants.NO_SLOT, "MA", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("MEDIUM", true, "B", BlocklyConstants.NO_SLOT, "MB", motorBproperties);

        Map<String, String> motorCproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", BlocklyConstants.NO_SLOT, "MC", motorCproperties);

        Map<String, String> motorDproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorD = new ConfigurationComponent("MEDIUM", true, "D", BlocklyConstants.NO_SLOT, "MD", motorDproperties);

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorC, motorD));
        return builder.build();
    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }
}
