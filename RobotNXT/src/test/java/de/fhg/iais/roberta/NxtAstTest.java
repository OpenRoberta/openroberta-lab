package de.fhg.iais.roberta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.NxtFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class NxtAstTest extends AstTest {

    protected static ConfigurationAst brickConfiguration;
    protected static ConfigurationAst brickConfigurationBC;
    protected static ConfigurationAst brickConfigurationAC;
    protected static ConfigurationAst brickConfigurationUS2US4;

    @BeforeClass
    public static void setup() {
        testFactory = new NxtFactory(new PluginProperties("nxt", "", "", Util1.loadProperties("classpath:/nxt.properties")));

        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorBLproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorBL = new ConfigurationComponent("LARGE", true, "B", "B", motorBLproperties);

        Map<String, String> motorCproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        ConfigurationComponent sensorS1 = new ConfigurationComponent("TOUCH", true, "S1", "1", Collections.emptyMap());
        ConfigurationComponent sensorS2 = new ConfigurationComponent("SOUND", true, "S2", "2", Collections.emptyMap());
        ConfigurationComponent sensorS3 = new ConfigurationComponent("COLOR", true, "S3", "3", Collections.emptyMap());
        ConfigurationComponent sensorS4 = new ConfigurationComponent("LIGHT", true, "S4", "4", Collections.emptyMap());

        ConfigurationComponent sensorUS2 = new ConfigurationComponent("ULTRASONIC", true, "S2", "2", Collections.emptyMap());
        ConfigurationComponent sensorUS4 = new ConfigurationComponent("ULTRASONIC", true, "S4", "4", Collections.emptyMap());

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, sensorS1, sensorS2, sensorS3, sensorS4));
        brickConfiguration = builder.build();

        final ConfigurationAst.Builder builderBC = new ConfigurationAst.Builder();
        builderBC.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorBL, motorC, sensorS1, sensorS2, sensorS3, sensorS4));
        brickConfigurationBC = builderBC.build();

        final ConfigurationAst.Builder builderAC = new ConfigurationAst.Builder();
        builderAC.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorC, sensorS1, sensorS2, sensorS3, sensorS4));
        brickConfigurationAC = builderAC.build();

        final ConfigurationAst.Builder builderUS2US4 = new ConfigurationAst.Builder();
        builderUS2US4.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(sensorUS2, sensorUS4));
        brickConfigurationUS2US4 = builderUS2US4.build();
    }
}
