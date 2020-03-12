package de.fhg.iais.roberta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

public class NxtAstTest extends AstTest {

    protected static ConfigurationAst brickConfiguration;
    protected static ConfigurationAst brickConfigurationBC;
    protected static ConfigurationAst brickConfigurationAC;
    protected static ConfigurationAst brickConfigurationUS2US4;
    protected static ConfigurationAst brickConfigurationC1C3C4;
    protected static ConfigurationAst brickConfigurationHTC1HTC2HTC3HTC4;

    protected static String DEFINES_INCLUDES =
        "#define WHEELDIAMETER 5.6"
            + "#define TRACKWIDTH 11.0"
            + "#define MAXLINES 8"
            + "#define MIN(X,Y)(((X)<(Y))?(X):(Y))"
            + "#define MAX(X,Y)(((X)>(Y))?(X):(Y))"
            + "#define M_PI PI"
            + "#define M_E 2.718281828459045"
            + "#define M_GOLDEN_RATIO 1.61803398875"
            + "#define M_SQRT2 1.41421356237"
            + "#define M_SQRT1_2 0.707106781187"
            + "#define M_INFINITY 0x7f800000"
            + "#include \"NEPODefs.h\"//containsNEPOdeclarationsfortheNXCNXTAPIresources";

    @BeforeClass
    public static void setup() {
        testFactory = new RobotFactory(new PluginProperties("nxt", "", "", Util.loadProperties("classpath:/nxt.properties")));

        Map<String, String> motorAproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorBLproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorBL = new ConfigurationComponent("LARGE", true, "B", "B", motorBLproperties);

        Map<String, String> motorCproperties = Util.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", "C", motorCproperties);

        ConfigurationComponent sensorS1 = new ConfigurationComponent("TOUCH", true, "S1", "1", Collections.emptyMap());
        ConfigurationComponent sensorS2 = new ConfigurationComponent("SOUND", true, "S2", "2", Collections.emptyMap());
        ConfigurationComponent sensorS3 = new ConfigurationComponent("COLOR", true, "S3", "3", Collections.emptyMap());
        ConfigurationComponent sensorS4 = new ConfigurationComponent("LIGHT", true, "S4", "4", Collections.emptyMap());

        ConfigurationComponent sensorC1 = new ConfigurationComponent("COLOR", true, "S1", "1", Collections.emptyMap());
        ConfigurationComponent sensorC3 = new ConfigurationComponent("COLOR", true, "S3", "3", Collections.emptyMap());
        ConfigurationComponent sensorC4 = new ConfigurationComponent("COLOR", true, "S4", "4", Collections.emptyMap());

        ConfigurationComponent sensorHTC1 = new ConfigurationComponent("HT_COLOR", true, "S1", "1", Collections.emptyMap());
        ConfigurationComponent sensorHTC2 = new ConfigurationComponent("HT_COLOR", true, "S2", "2", Collections.emptyMap());
        ConfigurationComponent sensorHTC3 = new ConfigurationComponent("HT_COLOR", true, "S3", "3", Collections.emptyMap());
        ConfigurationComponent sensorHTC4 = new ConfigurationComponent("HT_COLOR", true, "S4", "4", Collections.emptyMap());

        ConfigurationComponent sensorUS2 = new ConfigurationComponent("ULTRASONIC", true, "S2", "2", Collections.emptyMap());
        ConfigurationComponent sensorUS4 = new ConfigurationComponent("ULTRASONIC", true, "S4", "4", Collections.emptyMap());

        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(11.0f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, sensorS1, sensorS2, sensorS3, sensorS4));
        brickConfiguration = builder.build();

        ConfigurationAst.Builder builderBC = new ConfigurationAst.Builder();
        builderBC.setTrackWidth(11.0f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorBL, motorC, sensorS1, sensorS2, sensorS3, sensorS4));
        brickConfigurationBC = builderBC.build();

        ConfigurationAst.Builder builderAC = new ConfigurationAst.Builder();
        builderAC.setTrackWidth(11.0f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorC, sensorS1, sensorS2, sensorS3, sensorS4));
        brickConfigurationAC = builderAC.build();

        ConfigurationAst.Builder builderUS2US4 = new ConfigurationAst.Builder();
        builderUS2US4.setTrackWidth(11.0f).setWheelDiameter(5.6f).addComponents(Arrays.asList(sensorUS2, sensorUS4));
        brickConfigurationUS2US4 = builderUS2US4.build();

        ConfigurationAst.Builder builderC1C3C4 = new ConfigurationAst.Builder();
        builderC1C3C4.setTrackWidth(11.0f).setWheelDiameter(5.6f).addComponents(Arrays.asList(sensorC1, sensorC3, sensorC4));
        brickConfigurationC1C3C4 = builderC1C3C4.build();

        ConfigurationAst.Builder builderHTC1HTC2HTC3HTC4 = new ConfigurationAst.Builder();
        builderHTC1HTC2HTC3HTC4.setTrackWidth(11.0f).setWheelDiameter(5.6f).addComponents(Arrays.asList(sensorHTC1, sensorHTC2, sensorHTC3, sensorHTC4));
        brickConfigurationHTC1HTC2HTC3HTC4 = builderHTC1HTC2HTC3HTC4.build();
    }
}
