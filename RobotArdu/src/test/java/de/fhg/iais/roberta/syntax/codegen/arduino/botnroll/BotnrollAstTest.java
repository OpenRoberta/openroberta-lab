package de.fhg.iais.roberta.syntax.codegen.arduino.botnroll;

import java.util.Arrays;
import java.util.Map;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BotnrollFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class BotnrollAstTest extends AstTest {

    public static ConfigurationAst makeConfiguration() {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("MEDIUM", true, "B", "B", motorBproperties);

        Map<String, String> motorCproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        Map<String, String> motorDproperties = createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorD = new ConfigurationComponent("MEDIUM", true, "D", "D", motorDproperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorC, motorD));
        return builder.build();
    }

    @BeforeClass
    public static void setup() {
        testFactory = new BotnrollFactory(new PluginProperties("botnroll", "", "", Util1.loadProperties("classpath:/botnroll.properties")));
    }
}
